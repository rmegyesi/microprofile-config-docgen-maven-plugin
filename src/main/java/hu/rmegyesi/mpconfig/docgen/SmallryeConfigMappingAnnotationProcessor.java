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
