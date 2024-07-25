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

import hu.rmegyesi.mpconfig.docgen.data.ConfigPropertyDocElement;
import hu.rmegyesi.mpconfig.docgen.data.DocumentType;
import hu.rmegyesi.mpconfig.docgen.mpconfig.MPConfigAnnotationProcessor;
import hu.rmegyesi.mpconfig.docgen.scanner.ClassScanner;
import hu.rmegyesi.mpconfig.docgen.smallryeconfig.SmallryeConfigMappingAnnotationProcessor;
import hu.rmegyesi.mpconfig.docgen.writer.DocumentWriter;
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
import java.util.List;
import java.util.stream.Stream;

/**
 * Document generator mojo.
 */
@Mojo(name = "generate", defaultPhase = LifecyclePhase.PROCESS_CLASSES, requiresDependencyResolution = ResolutionScope.RUNTIME)
public class MPConfigDocGeneratorMojo extends AbstractMojo {

    private static final Logger LOGGER = LoggerFactory.getLogger(MPConfigDocGeneratorMojo.class);

    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    private MavenProject project;

    @Parameter(defaultValue = "${project.basedir}/config-properties.adoc", property = "outputFile", required = true)
    private File outputFile;

    @Parameter(defaultValue = "ASCII_DOC", property = "fileType")
    private DocumentType fileType;

    @Parameter(property = "packageName", required = true)
    private String packageName;

    ClassScanner classScanner;
    MPConfigAnnotationProcessor mpConfigAnnotationProcessor;
    SmallryeConfigMappingAnnotationProcessor smallryeConfigMappingAnnotationProcessor;

    /**
     * Serves as the main entry point of the plugin.
     */
    @Override
    public void execute() throws MojoExecutionException {
        try {
            classScanner = new ClassScanner(project);
            mpConfigAnnotationProcessor = new MPConfigAnnotationProcessor();
            smallryeConfigMappingAnnotationProcessor = new SmallryeConfigMappingAnnotationProcessor();

            generateDocumentation();
        } catch (IOException | ClassNotFoundException e) {
            throw new MojoExecutionException("Error writing documentation", e);
        }
    }

    private void generateDocumentation() throws IOException, ClassNotFoundException {
        List<ConfigPropertyDocElement> elements = classScanner.getAllClasses(packageName)
                .flatMap(this::getElements)
                .sorted()
                .toList();


        try (FileWriter writer = new FileWriter(outputFile)) {
            DocumentWriter doc = fileType.getDocumentWriter(writer);
            doc.write(elements);
        }
    }

    private Stream<ConfigPropertyDocElement> getElements(Class<?> clazz) {
        if (clazz.isAnnotationPresent(ConfigMapping.class)) {
            return smallryeConfigMappingAnnotationProcessor.processConfigMappingInterface(clazz);
        } else {
            return mpConfigAnnotationProcessor.processClass(clazz);
        }
    }


}
