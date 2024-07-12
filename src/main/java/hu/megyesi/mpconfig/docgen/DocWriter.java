package hu.megyesi.mpconfig.docgen;

import java.io.IOException;
import java.io.Writer;

public class DocWriter {

    Writer writer;

    public DocWriter(Writer writer) {
        this.writer = writer;
    }

    public void writeHeader() throws IOException {
        writer.write("= Config Properties\n\n");
    }

    public void writeTableHeading() throws IOException {
        writer.write("|===\n");
        writer.write("| Property Name | Environment Variable | Default value | Optional\n\n");
    }

    public void writeProperty(String propertyName, String environmentVariable, String defaultValue, boolean optional) throws IOException {
        writer.write("| " + propertyName + "\n");
        writer.write("| " + environmentVariable + "\n");
        writer.write("| " + defaultValue + "\n");
        writer.write("| " + optional + "\n\n");
    }

    public void writeTableEnd() throws IOException {
        writer.write("|===");
    }
}
