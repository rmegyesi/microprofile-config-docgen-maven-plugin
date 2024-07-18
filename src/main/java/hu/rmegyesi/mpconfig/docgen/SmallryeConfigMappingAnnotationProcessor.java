package hu.rmegyesi.mpconfig.docgen;

import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;
import io.smallrye.config.WithName;
import io.smallrye.config.WithParentName;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

public class SmallryeConfigMappingAnnotationProcessor {

    public Stream<ConfigPropertyDocElement> processConfigMappingInterface(Class<?> clazz) {
        ConfigMapping configMapping = clazz.getAnnotation(ConfigMapping.class);

        return processInterface(clazz, configMapping, configMapping.prefix());
    }

    private Stream<ConfigPropertyDocElement> processInterface(Class<?> clazz, ConfigMapping configMapping, String prefix) {
        HashSet<Class<?>> configGroups = new HashSet<>(Arrays.asList(clazz.getDeclaredClasses()));

        return Arrays.stream(clazz.getDeclaredMethods())
                .flatMap(method -> processMethod(method, configMapping, configGroups, prefix));
    }

    private Stream<ConfigPropertyDocElement> processMethod(Method method, ConfigMapping configMapping, Set<Class<?>> configGroups, String prefix) {
        Class<?> returnType = method.getReturnType();

        if (configGroups.contains(returnType)) {
            if (method.isAnnotationPresent(WithParentName.class)) {
                return processInterface(returnType, configMapping, prefix);
            } else if (method.isAnnotationPresent(WithName.class)) {
                WithName withName = method.getAnnotation(WithName.class);
                return processInterface(returnType, configMapping, prefix + "." + withName.value());
            } else {
                return processInterface(returnType, configMapping, prefix + "." + configMapping.namingStrategy().apply(method.getName()));
            }
        }

        String actualPrefix = prefix.isBlank() ? prefix : prefix + ".";
        String name;

        if (method.isAnnotationPresent(WithName.class)) {
            WithName withName = method.getAnnotation(WithName.class);
            name = actualPrefix + withName.value();
        } else {
            name = actualPrefix + configMapping.namingStrategy().apply(method.getName());
        }

        String environmentVariable = Utils.configPropertyToEnvironmentVariable(name);
        String type = method.getGenericReturnType().getTypeName();
        String defaultValue = getDefaultValue(method);
        boolean optional = method.getReturnType().equals(Optional.class);

        return Stream.of(new ConfigPropertyDocElement(name, environmentVariable, defaultValue, type, optional));
    }

    static String getDefaultValue(Method method) {
        if (method.isAnnotationPresent(WithDefault.class)) {
            WithDefault withDefault = method.getAnnotation(WithDefault.class);
            return withDefault.value();
        }

        return "";
    }
}
