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
import java.io.Writer;
import java.util.Collection;

/**
 * Handles Markdown file writing
 */
public class MarkdownWriter implements DocumentWriter {

    Writer writer;

    /**
     * Public constructor
     * @param writer Writer
     */
    public MarkdownWriter(Writer writer) {
        this.writer = writer;
    }

    /**
     * Write config properties into a Markdown table
     * @param elements Config properties
     */
    public void write(Collection<ConfigPropertyDocElement> elements) throws IOException {
        int[] columnWidths = getColumnWidths(elements);

        writeHeader();

        writeTableHeading(columnWidths);
        for (ConfigPropertyDocElement element : elements) {
            writeRow(element, columnWidths);
        }
    }

    private void writeHeader() throws IOException {
        writer.write("# " + TITLE + "\n\n");
    }

    private void writeTableHeading(int[] columnWidths) throws IOException {
        for (int i = 0; i < HEADERS.length; i++) {
            String header = formatStringToWidth(HEADERS[i], columnWidths[i]);
            writer.write("| " + header + " ");
        }
        writer.write("|\n");

        for (int i = 0; i < HEADERS.length; i++) {
            String s = "-".repeat(columnWidths[i] + 2);
            writer.write("|" + s);
        }
        writer.write("|\n");
    }

    private void writeRow(ConfigPropertyDocElement element, int[] columnWidths) throws IOException {
        writeCell(element.name(), columnWidths[0]);
        writeCell(element.environmentVariable(), columnWidths[1]);
        writeCell(element.defaultValue(), columnWidths[2]);
        writeCell(Boolean.toString(element.optional()), columnWidths[3]);
        writeCell(element.type(), columnWidths[4]);
        writer.write("|\n");
    }

    private void writeCell(String value, int width) throws IOException {
        writer.write("| " + formatStringToWidth(value, width) + " ");
    }

    static String formatStringToWidth(String value, int width) {
        int whitespacesToAppend = width - value.length();

        if (whitespacesToAppend == 0) return value;

        return value + " ".repeat(whitespacesToAppend);
    }

    static int[] getColumnWidths(Collection<ConfigPropertyDocElement> elements) {
        int columns = 5;
        int[] widths = new int[columns];

        for (int i = 0; i < MarkdownWriter.HEADERS.length; i++) {
            widths[i] = MarkdownWriter.HEADERS[i].length();
        }

        for (ConfigPropertyDocElement element : elements) {
            findMaxWidth(element.name(), 0, widths);
            findMaxWidth(element.environmentVariable(), 1, widths);
            findMaxWidth(element.defaultValue(), 2, widths);
            // "Optional" column width is fixed
            findMaxWidth(element.type(), 4, widths);
        }

        return widths;
    }

    private static void findMaxWidth(String value, int column, int[] widths) {
        int currentMax = widths[column];
        int width = value.length();
        if (width > currentMax) widths[column] = width;
    }
}
