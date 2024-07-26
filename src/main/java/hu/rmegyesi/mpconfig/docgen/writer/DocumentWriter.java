package hu.rmegyesi.mpconfig.docgen.writer;

/*-
 * #%L
 * hu.rmegyesi:microprofile-config-docgen-maven-plugin
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

import java.io.IOException;
import java.util.Collection;

/**
 * Config property document writer
 */
public interface DocumentWriter {

    String TITLE = "Config Properties";
    String[] HEADERS = {"Property", "Environment variable", "Default value", "Optional", "Type"};

    /**
     * Write config properties into a document
     * @param elements Config properties
     */
    void write(Collection<ConfigPropertyDocElement> elements) throws IOException;
}
