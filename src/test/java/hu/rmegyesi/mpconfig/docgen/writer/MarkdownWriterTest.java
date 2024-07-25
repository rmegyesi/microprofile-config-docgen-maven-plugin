package hu.rmegyesi.mpconfig.docgen.writer;

import hu.rmegyesi.mpconfig.docgen.data.ConfigPropertyDocElement;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class MarkdownWriterTest {

    @Test
    void getColumnWidthsTest() {
        List<ConfigPropertyDocElement> elements = List.of(
                new ConfigPropertyDocElement(
                        "dev.test.property",
                        "DEV_TEST_PROPERTY",
                        "",
                        "String",
                        true
                ),
                new ConfigPropertyDocElement(
                        "test",
                        "TEST",
                        "100L",
                        "Long",
                        false
                )
        );

        int[] expectedWidths = new int[]{17, 20, 13, 8, 6};
        int[] actualWidths = MarkdownWriter.getColumnWidths(elements);

        Assertions.assertArrayEquals(expectedWidths, actualWidths);
    }

    @Test
    void formatStringToWidthTest() {
        String result = MarkdownWriter.formatStringToWidth("test", 10);
        Assertions.assertEquals(10, result.length());

        result = MarkdownWriter.formatStringToWidth("test", 4);
        Assertions.assertEquals(4, result.length());
    }
}
