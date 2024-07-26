package hu.rmegyesi.mpconfig.docgen;

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

import hu.rmegyesi.mpconfig.docgen.data.DocumentFormat;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Optional;

public class MPConfigDocGeneratorMojoTest {

    @Test
    void getFormatTest() {
        Map<String, DocumentFormat> expectedValuesForFilenames = Map.of(
                "config-properties.adoc", DocumentFormat.ASCII_DOC,
                "config-properties.md", DocumentFormat.MARKDOWN,
                "config-properties.markdown", DocumentFormat.MARKDOWN,
                "config-properties.test.md", DocumentFormat.MARKDOWN
        );

        expectedValuesForFilenames.forEach((filename, expected) -> {
            DocumentFormat actual = MPConfigDocGeneratorMojo.getFormat(filename).orElse(null);
            Assertions.assertEquals(expected, actual);
        });
    }

    @Test
    void getFormatTest_unknown() {
        String filename = "config-properties.txt";
        Optional<DocumentFormat> format = MPConfigDocGeneratorMojo.getFormat(filename);
        Assertions.assertTrue(format.isEmpty());
    }
}
