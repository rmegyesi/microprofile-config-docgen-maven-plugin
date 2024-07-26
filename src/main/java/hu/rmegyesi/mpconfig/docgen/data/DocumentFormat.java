package hu.rmegyesi.mpconfig.docgen.data;

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

import hu.rmegyesi.mpconfig.docgen.writer.AsciiDocWriter;
import hu.rmegyesi.mpconfig.docgen.writer.DocumentWriter;
import hu.rmegyesi.mpconfig.docgen.writer.MarkdownWriter;

import java.io.Writer;
import java.util.function.Function;

/**
 * Supported document formats
 */
public enum DocumentFormat {
    ASCII_DOC(
            new String[]{"adoc"},
            AsciiDocWriter::new
    ),
    MARKDOWN(
            new String[]{"md", "markdown"},
            MarkdownWriter::new
    );

    final String[] knownExtensions;
    final Function<Writer, DocumentWriter> documentWriterFactory;

    DocumentFormat(String[] knownExtensions, Function<Writer, DocumentWriter> documentWriterFactory) {
        this.knownExtensions = knownExtensions;
        this.documentWriterFactory = documentWriterFactory;
    }

    public DocumentWriter createDocumentWriter(Writer writer) {
        return this.documentWriterFactory.apply(writer);
    }

    public String[] getKnownExtensions() {
        return knownExtensions;
    }
}
