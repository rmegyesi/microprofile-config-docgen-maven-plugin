package hu.rmegyesi.mpconfig.docgen;

import org.eclipse.microprofile.config.inject.ConfigProperties;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

public class MPConfigAnnotationProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(MPConfigAnnotationProcessor.class);

    public Stream<ConfigPropertyDocElement> processClass(Class<?> clazz) {
        String prefix = getPrefix(clazz);

        return Arrays.stream(clazz.getDeclaredFields())
                .map(field -> toDocElement(field, clazz, prefix))
                .filter(Objects::nonNull);
    }

    private String getPrefix(Class<?> clazz) {
        if (clazz.isAnnotationPresent(ConfigProperties.class)) {
            ConfigProperties configProperties = clazz.getAnnotation(ConfigProperties.class);
            return configProperties.prefix();
        }

        return null;
    }

    private ConfigPropertyDocElement toDocElement(Field field, Class<?> clazz, String prefix) {
        if (!field.isAnnotationPresent(ConfigProperty.class)) {
            return null;
        }

        LOGGER.debug("{} annotated field: {}", ConfigProperty.class.getSimpleName(), field.getName());

        ConfigProperty configProperty = field.getAnnotation(ConfigProperty.class);
        String propertyName = getPropertyName(configProperty, clazz, field, prefix);
        String environmentVariable = Utils.configPropertyToEnvironmentVariable(propertyName);
        String defaultValue = getDefaultValue(configProperty);
        String type = Utils.getFieldType(field);
        boolean optional = Utils.isOptionalField(field);

        return new ConfigPropertyDocElement(propertyName, environmentVariable, defaultValue, type, optional);
    }

    static String getPropertyName(ConfigProperty configProperty, Class<?> parentClass, Field field, String prefix) {
        if (configProperty.name().isEmpty()) {
            if (prefix == null || prefix.isBlank()) {
                return parentClass.getCanonicalName() + "." + field.getName();
            }

            return prefix + field.getName();
        }


        return prefix + configProperty.name();
    }

    static String getDefaultValue(ConfigProperty configProperty) {
        if (!configProperty.defaultValue().equals(ConfigProperty.UNCONFIGURED_VALUE)) {
            return configProperty.defaultValue();
        }

        return "";
    }
}
