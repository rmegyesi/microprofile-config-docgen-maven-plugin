package hu.rmegyesi.mpconfig.docgen;

/*-
 * #%L
 * Microprofile Config Docgen Maven Plugin
 * %%
 * Copyright (C) 2024 Róbert Megyesi
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import io.smallrye.config.ConfigMapping;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Mojo(name = "generate", defaultPhase = LifecyclePhase.PROCESS_CLASSES, requiresDependencyResolution = ResolutionScope.RUNTIME)
public class MPConfigDocGeneratorMojo extends AbstractMojo {

    private static final Logger LOGGER = LoggerFactory.getLogger(MPConfigDocGeneratorMojo.class);

    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    private MavenProject project;

    @Parameter(defaultValue = "${project.basedir}/config-properties.adoc", property = "outputFile", required = true)
    private File outputFile;

    @Parameter(property = "packageName", required = true)
    private String packageName;

    ClassLoader classLoader;

    MPConfigAnnotationProcessor mpConfigAnnotationProcessor;
    SmallryeConfigMappingAnnotationProcessor smallryeConfigMappingAnnotationProcessor;

    @Override
    public void execute() throws MojoExecutionException {
        try {
            classLoader = getClassLoader();
            mpConfigAnnotationProcessor = new MPConfigAnnotationProcessor();
            smallryeConfigMappingAnnotationProcessor = new SmallryeConfigMappingAnnotationProcessor();

            generateDocumentation();
        } catch (IOException | ClassNotFoundException e) {
            throw new MojoExecutionException("Error writing documentation", e);
        }
    }

    private void generateDocumentation() throws IOException, ClassNotFoundException {
        Stream<ConfigPropertyDocElement> configPropertyDocElementStream = getAllClasses(packageName)
                .flatMap(this::getElements);

        try (FileWriter writer = new FileWriter(outputFile)) {
            AsciiDocWriter doc = new AsciiDocWriter(writer);
            doc.writeHeader();
            doc.writeTableHeading();

            configPropertyDocElementStream.forEach(doc::writeProperty);

            doc.writeTableEnd();
        }
    }

    private Stream<ConfigPropertyDocElement> getElements(Class<?> clazz) {
        if (clazz.isAnnotationPresent(ConfigMapping.class)) {
            return smallryeConfigMappingAnnotationProcessor.processConfigMappingInterface(clazz);
        } else {
            return mpConfigAnnotationProcessor.processClass(clazz);
        }
    }

    private Stream<Class<?>> getAllClasses(String packageName) {
        String path = packageName.replace('.', '/');
        Iterable<URL> resources = () -> {
            try {
                return classLoader.getResources(path).asIterator();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };

        return StreamSupport.stream(resources.spliterator(), false)
                .map(url -> new File(url.getFile()))
                .flatMap(directory -> findClasses(directory, packageName));
    }

    private Stream<Class<?>> findClasses(File directory, String packageName) {
        LOGGER.info("findClasses in directory: {}", directory.toString());

        if (!directory.exists()) {
            return Stream.empty();
        }
        File[] files = directory.listFiles();
        if (files == null) {
            return Stream.empty();
        }

        return Arrays.stream(files)
                .flatMap(file -> {
                    if (file.isDirectory()) {
                        return findClasses(file, packageName + "." + file.getName());
                    } else if (file.getName().endsWith(".class")) {
                        String className = packageName + '.' + file.getName().substring(0, file.getName().length() - 6);
                        try {
                            Class<?> loadedClass = classLoader.loadClass(className);
                            return Stream.of(loadedClass);
                        } catch (ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        return Stream.empty();
                    }
                });
    }

    // Method courtesy from TabNine From Google Results.
    private ClassLoader getClassLoader() {
        ClassLoader classLoader = null;
        try {
            List<String> classpathElements = project.getRuntimeClasspathElements();
            if (null == classpathElements) {
                return Thread.currentThread().getContextClassLoader();
            }
            LOGGER.info("Classpathelements: {}", classpathElements);

            URL[] urls = new URL[classpathElements.size()];

            for (int i = 0; i < classpathElements.size(); ++i) {
                urls[i] = new File(classpathElements.get(i)).toURI().toURL();
            }
            classLoader = new URLClassLoader(urls, getClass().getClassLoader());
        } catch (Exception e) {
            LOGGER.error("Failed to get classloader", e);
        }
        return classLoader;
    }
}
