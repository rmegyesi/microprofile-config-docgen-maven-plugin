package hu.rmegyesi.mpconfig.docgen.scanner;

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

import org.apache.maven.project.MavenProject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Utility class for finding classes
 */
public class ClassScanner {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClassScanner.class);

    ClassLoader classLoader;

    /**
     * Public constructor
     * @param mavenProject Maven project
     */
    public ClassScanner(MavenProject mavenProject) {
        this.classLoader = getClassLoader(mavenProject);
    }

    /**
     * Recursively finds all classes under a package
     * @param packageName Package name
     * @return Stream of classes
     */
    public Stream<Class<?>> getAllClasses(String packageName) {
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
        LOGGER.debug("findClasses in directory: {}", directory.toString());

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
                            LOGGER.debug("Loading class: {}", className);
                            Class<?> loadedClass = classLoader.loadClass(className);
                            return Stream.of(loadedClass);
                        } catch (Exception e) {
                            throw new RuntimeException("Failed to load class: " + className, e);
                        }
                    } else {
                        return Stream.empty();
                    }
                });
    }

    // Method courtesy from TabNine From Google Results.
    private ClassLoader getClassLoader(MavenProject mavenProject) {
        ClassLoader classLoader = getClass().getClassLoader();
        try {
            List<String> classpathElements = mavenProject.getCompileClasspathElements();

            LOGGER.debug("Classpath elements: {}", classpathElements);

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
