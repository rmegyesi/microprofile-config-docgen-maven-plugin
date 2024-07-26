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
