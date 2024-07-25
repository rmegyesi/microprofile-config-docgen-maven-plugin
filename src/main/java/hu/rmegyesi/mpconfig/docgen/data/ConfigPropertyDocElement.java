package hu.rmegyesi.mpconfig.docgen.data;

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

/**
 * Config property data
 * @param name Config property name
 * @param environmentVariable Environment variable name
 * @param defaultValue Default value
 * @param type Type
 * @param optional Optional
 */
public record ConfigPropertyDocElement(String name, String environmentVariable, String defaultValue, String type,
                                       boolean optional) implements Comparable<ConfigPropertyDocElement> {

    @Override
    public int compareTo(ConfigPropertyDocElement o) {
        return this.name.compareTo(o.name);
    }
}
