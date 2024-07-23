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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestUtils {

    public static String getExpectedProperty(Field field) {
        String[] values = field.getAnnotation(ExpectedProperties.class).value();
        if (values.length != 0) {
            return values[0];
        }

        throw new RuntimeException("Missing expected property");
    }

    public static String getExpectedProperty(Class<?> clazz, String fieldName) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            return getExpectedProperty(field);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public static Set<String> getExpectedPropertyNames(Class<?> clazz) {
        Stream<Field> fields = Arrays.stream(clazz.getDeclaredFields());
        Stream<Method> methods = Arrays.stream(clazz.getDeclaredMethods());

        Stream<String> propertyNamesFromFields = fields.filter(field -> field.isAnnotationPresent(ExpectedProperties.class))
                .map(field -> field.getAnnotation(ExpectedProperties.class))
                .flatMap(expectedProperties -> Arrays.stream(expectedProperties.value()));

        Stream<String> propertyNamesFromMethods = methods.filter(method -> method.isAnnotationPresent(ExpectedProperties.class))
                .map(method -> method.getAnnotation(ExpectedProperties.class))
                .flatMap(expectedProperties -> Arrays.stream(expectedProperties.value()));

        return Stream.concat(propertyNamesFromFields, propertyNamesFromMethods)
                .collect(Collectors.toSet());
    }
}
