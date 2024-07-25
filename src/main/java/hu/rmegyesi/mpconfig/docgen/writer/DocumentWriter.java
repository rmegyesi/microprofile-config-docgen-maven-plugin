package hu.rmegyesi.mpconfig.docgen.writer;

import hu.rmegyesi.mpconfig.docgen.data.ConfigPropertyDocElement;

import java.io.IOException;
import java.util.Collection;

/**
 * Config property document writer
 */
public interface DocumentWriter {

    /**
     * Write config properties into a document
     * @param elements Config properties
     */
    void write(Collection<ConfigPropertyDocElement> elements) throws IOException;
}
