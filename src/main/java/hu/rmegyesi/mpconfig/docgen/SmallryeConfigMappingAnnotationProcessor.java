package hu.rmegyesi.mpconfig.docgen;

import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;
import io.smallrye.config.WithName;
import io.smallrye.config.WithParentName;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Stream;

public class SmallryeConfigMappingAnnotationProcessor {

    public Stream<ConfigPropertyDocElement> processConfigMappingInterface(Class<?> clazz) {
        ConfigMapping configMapping = clazz.getAnnotation(ConfigMapping.class);

        return processInterface(clazz, configMapping, Collections.emptySet(), configMapping.prefix());
    }

    private Stream<ConfigPropertyDocElement> processInterface(Class<?> clazz, ConfigMapping configMapping, Set<Class<?>> configGroups, String prefix) {
        HashSet<Class<?>> groups = new HashSet<>(Arrays.asList(clazz.getDeclaredClasses()));
        groups.addAll(configGroups);

        return Arrays.stream(clazz.getDeclaredMethods())
                .flatMap(method -> processMethod(method, configMapping, groups, prefix));
    }

    private Stream<ConfigPropertyDocElement> processMethod(Method method, ConfigMapping configMapping, Set<Class<?>> configGroups, String prefix) {
        Class<?> returnType = method.getReturnType();

        if (configGroups.contains(returnType)) {
            if (method.isAnnotationPresent(WithParentName.class)) {
                return processInterface(returnType, configMapping, configGroups, prefix);
            } else if (method.isAnnotationPresent(WithName.class)) {
                WithName withName = method.getAnnotation(WithName.class);
                return processInterface(returnType, configMapping, configGroups, prefix + "." + withName.value());
            } else {
                return processInterface(returnType, configMapping, configGroups, prefix + "." + configMapping.namingStrategy().apply(method.getName()));
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
