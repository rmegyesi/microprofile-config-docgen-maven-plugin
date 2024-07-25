package hu.rmegyesi.mpconfig.docgen.data;

import hu.rmegyesi.mpconfig.docgen.writer.AsciiDocWriter;
import hu.rmegyesi.mpconfig.docgen.writer.DocumentWriter;
import hu.rmegyesi.mpconfig.docgen.writer.MarkdownWriter;

import java.io.Writer;
import java.util.function.Function;

public enum DocumentType {
    ASCII_DOC(AsciiDocWriter::new),
    MARKDOWN(MarkdownWriter::new);

    final Function<Writer, DocumentWriter> documentWriterFactory;

    DocumentType(Function<Writer, DocumentWriter> documentWriterFactory) {
        this.documentWriterFactory = documentWriterFactory;
    }

    public DocumentWriter getDocumentWriter(Writer writer) {
        return this.documentWriterFactory.apply(writer);
    }
}
