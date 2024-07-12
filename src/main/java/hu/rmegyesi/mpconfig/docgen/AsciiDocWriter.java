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

import java.io.IOException;
import java.io.Writer;

public class AsciiDocWriter {

    Writer writer;

    public AsciiDocWriter(Writer writer) {
        this.writer = writer;
    }

    public void writeHeader() throws IOException {
        writer.write("= Config Properties\n\n");
    }

    public void writeTableHeading() throws IOException {
        writer.write("|===\n");
        writer.write("| Property Name | Environment Variable | Default value | Optional | Type\n\n");
    }

    public void writeProperty(String propertyName, String environmentVariable, String defaultValue, boolean optional, String type) throws IOException {
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

    public void writeTableEnd() throws IOException {
        writer.write("|===");
    }
}
