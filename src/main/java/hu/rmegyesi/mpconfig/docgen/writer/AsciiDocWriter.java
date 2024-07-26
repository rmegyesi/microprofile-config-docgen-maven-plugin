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
import java.util.Collection;

/**
 * Handles AsciiDoc file writing
 */
public class AsciiDocWriter implements DocumentWriter {

    Writer writer;

    /**
     * Public constructor
     * @param writer Writer
     */
    public AsciiDocWriter(Writer writer) {
        this.writer = writer;
    }

    /**
     * Write config properties into an AsciiDoc table
     * @param elements Config properties
     */
    public void write(Collection<ConfigPropertyDocElement> elements) throws IOException {
        writeHeader();

        writeTableHeading();
        for (ConfigPropertyDocElement element : elements) {
            writeProperty(element);
        }
        writeTableEnd();
    }

    private void writeHeader() throws IOException {
        writer.write("= " + TITLE + "\n\n");
    }

    private void writeTableHeading() throws IOException {
        writer.write("|===\n");
        for (String header : HEADERS) {
            writer.write("| " + header + " ");
        }
        writer.write("|\n\n");
    }

    private void writeProperty(ConfigPropertyDocElement element) throws IOException {
        writeProperty(
                element.name(),
                element.environmentVariable(),
                element.defaultValue(),
                element.optional(),
                element.type()
        );
    }

    private void writeTableEnd() throws IOException {
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
