package hu.megyesi.mpconfig.docgen;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.eclipse.microprofile.config.inject.ConfigProperties;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;

@Mojo(name = "generate", defaultPhase = LifecyclePhase.PROCESS_CLASSES, requiresDependencyResolution = ResolutionScope.RUNTIME)
public class ConfigDocumentationMojo extends AbstractMojo {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigDocumentationMojo.class);

    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    private MavenProject project;

    @Parameter(defaultValue = "${project.basedir}/config-docs.adoc", property = "outputFile", required = true)
    private File outputFile;

    @Parameter(property = "packageName", required = true)
    private String packageName;

    ClassLoader classLoader;

    @Override
    public void execute() throws MojoExecutionException {
        try {
            classLoader = getClassLoader();
            generateDocumentation();
        } catch (IOException | ClassNotFoundException e) {
            throw new MojoExecutionException("Error writing documentation", e);
        }
    }

    private void generateDocumentation() throws IOException, ClassNotFoundException {
        List<Class<?>> classes = getAllClasses(packageName);

        try (FileWriter writer = new FileWriter(outputFile)) {
            DocWriter doc = new DocWriter(writer);
            doc.writeHeader();
            doc.writeTableHeading();

            for (Class<?> clazz : classes) {
                String prefix = "";
                if (clazz.isAnnotationPresent(ConfigProperties.class)) {
                    LOGGER.debug("{} annotated class: {}", ConfigProperties.class.getSimpleName(), clazz.getSimpleName());

                    ConfigProperties configProperties = clazz.getAnnotation(ConfigProperties.class);
                    prefix = configProperties.prefix() + ".";
                }

                for (Field field : clazz.getDeclaredFields()) {
                    if (field.isAnnotationPresent(ConfigProperty.class)) {
                        LOGGER.debug("{} annotated field: {}", ConfigProperty.class.getSimpleName(), field.getName());

                        ConfigProperty configProperty = field.getAnnotation(ConfigProperty.class);
                        String propertyName = prefix + configProperty.name();
                        String environmentVariable = configPropertyToEnvironmentVariable(propertyName);
                        String defaultValue = getDefaultValue(configProperty, field);
                        boolean optional = field.getType().equals(Optional.class);

                        LOGGER.info("Config property: {}", propertyName);
                        doc.writeProperty(propertyName, environmentVariable, defaultValue, optional);
                    }
                }
            }

            doc.writeTableEnd();
        }
    }

    private static String configPropertyToEnvironmentVariable(String configProperty) {
        return configProperty.toUpperCase().replaceAll("[^A-Za-z0-9]", "_");
    }

    private static String getDefaultValue(ConfigProperty configProperty, Field field) {
        if (!configProperty.defaultValue().equals(ConfigProperty.UNCONFIGURED_VALUE)) {
            return configProperty.defaultValue();
        }

        return "";
    }

    private List<Class<?>> getAllClasses(String packageName) throws IOException, ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>();
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);

        List<File> dirs = new ArrayList<>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();

            dirs.add(new File(resource.getFile()));
        }

        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
        }

        return classes;
    }

    private List<Class<?>> findClasses(File directory, String packageName) throws ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        if (files == null) {
            return classes;
        }
        for (File file : files) {
            if (file.isDirectory()) {
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                String className = packageName + '.' + file.getName().substring(0, file.getName().length() - 6);
                Class<?> loadedClass = classLoader.loadClass(className);
                classes.add(loadedClass);
            }
        }
        return classes;
    }

    // Method courtesy from TabNine From Google Results.
    private ClassLoader getClassLoader() {
        ClassLoader classLoader = null;
        try {
            List<String> classpathElements = project.getRuntimeClasspathElements();
            if (null == classpathElements) {
                return Thread.currentThread().getContextClassLoader();
            }
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
