package hu.rmegyesi.mpconfig.docgen.smallryeconfig;

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
import hu.rmegyesi.mpconfig.test.smallryeconfig.NestedConfigMapping;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static hu.rmegyesi.mpconfig.docgen.TestUtils.getExpectedPropertyNames;

public class SmallryeConfigMappingAnnotationProcessorTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(SmallryeConfigMappingAnnotationProcessorTest.class);

    @Test
    void testNestedConfig() {
        SmallryeConfigMappingAnnotationProcessor processor = new SmallryeConfigMappingAnnotationProcessor();
        Stream<ConfigPropertyDocElement> configs = processor.processConfigMappingInterface(NestedConfigMapping.class);

        Set<String> expectedNames = getExpectedPropertyNames(NestedConfigMapping.class);
        Set<String> configNames = configs.map(ConfigPropertyDocElement::name).collect(Collectors.toSet());

        Set<String> unexpectedNames = new HashSet<>(configNames);
        unexpectedNames.removeAll(expectedNames);

        if (!unexpectedNames.isEmpty()) LOGGER.error("Unexpected names: {}", unexpectedNames);

        Set<String> missingNames = new HashSet<>(expectedNames);
        missingNames.removeAll(configNames);

        if (!missingNames.isEmpty()) LOGGER.error("Missing names: {}", missingNames);

        Assertions.assertTrue(unexpectedNames.isEmpty());
        Assertions.assertTrue(missingNames.isEmpty());
    }
}
