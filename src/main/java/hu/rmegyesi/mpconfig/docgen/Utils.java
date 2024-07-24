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
import java.util.Optional;

/**
 * Utilities
 */
public abstract class Utils {

    /**
     * Checks if the field is optional
     * @param field Field
     * @return True, if field is of Optional type
     */
    public static boolean isOptionalField(Field field) {
        return field.getType().equals(Optional.class);
    }

    /**
     * Gets the type of the field
     * @param field Field
     * @return Name of the type
     */
    public static String getFieldType(Field field) {
        return field.getGenericType().getTypeName();
    }

    /**
     * Converts a config property name to environment variable name
     * @param configProperty Config property name
     * @return Environment variable name
     */
    public static String configPropertyToEnvironmentVariable(String configProperty) {
        return configProperty.toUpperCase().replaceAll("[^A-Za-z0-9]", "_");
    }
}
