package hu.rmegyesi.mpconfig.docgen.writer;

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

import java.io.IOException;
import java.io.Writer;

/**
 * Handles AsciiDoc file writing
 */
public class AsciiDocWriter {

    Writer writer;

    /**
     * Public constructor
     * @param writer Writer
     */
    public AsciiDocWriter(Writer writer) {
        this.writer = writer;
    }

    /**
     * Write document header
     */
    public void writeHeader() throws IOException {
        writer.write("= Config Properties\n\n");
    }

    /**
     * Write table heading
     */
    public void writeTableHeading() throws IOException {
        writer.write("|===\n");
        writer.write("| Property Name | Environment Variable | Default value | Optional | Type\n\n");
    }

    /**
     * Write a table row for a config property
     * @param element Config property
     */
    public void writeProperty(ConfigPropertyDocElement element) {
        try {
            writeProperty(
                    element.name(),
                    element.environmentVariable(),
                    element.defaultValue(),
                    element.optional(),
                    element.type()
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Write table closing line
     */
    public void writeTableEnd() throws IOException {
        writer.write("|===");
    }

    private void writeProperty(String propertyName, String environmentVariable, String defaultValue, boolean optional, String type) throws IOException {
        writeTableCell(propertyName);
        writeTableCell(environmentVariable);
        writeTableCell(defaultValue);
        writeTableCell(optional);
        writeTableCell(type);

        writer.write("\n");
    }

    private void writeTableCell(Object value) throws IOException {
        writer.write("| " + value + "\n");
    }

}
