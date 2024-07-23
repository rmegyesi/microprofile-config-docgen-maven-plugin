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

import hu.rmegyesi.mpconfig.test.mpconfig.AggregatedConfig;
import hu.rmegyesi.mpconfig.test.mpconfig.TestBean;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Map;

import static hu.rmegyesi.mpconfig.docgen.TestUtils.getExpectedProperty;

class MPConfigDocGeneratorMojoTest {

    @Test
    void getPropertyName_withConfigProperties_withName() throws NoSuchFieldException {
        Class<AggregatedConfig> aggregatedConfigClass = AggregatedConfig.class;
        String fieldName = "exampleString";

        String exampleStringProperty = getPropertyName(aggregatedConfigClass, fieldName, "app");
        Assertions.assertEquals(getExpectedProperty(aggregatedConfigClass, fieldName), exampleStringProperty);
    }

    @Test
    void getPropertyName_withConfigProperties_withoutName() throws NoSuchFieldException {
        Class<AggregatedConfig> aggregatedConfigClass = AggregatedConfig.class;
        String fieldName = "unnamedString";

        String unnamedStringProperty = getPropertyName(aggregatedConfigClass, fieldName, "app");
        Assertions.assertEquals(getExpectedProperty(aggregatedConfigClass, fieldName), unnamedStringProperty);
    }

    @Test
    void getPropertyName_withoutConfigProperties_withName() throws NoSuchFieldException {
        Class<TestBean> testBeanClass = TestBean.class;
        String fieldName = "namedStringInBean";

        String actual = getPropertyName(testBeanClass, fieldName, "");
        String expected = getExpectedProperty(testBeanClass, fieldName);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getPropertyName_withoutConfigProperties_withoutName() throws NoSuchFieldException {
        Class<TestBean> testBeanClass = TestBean.class;
        String fieldName = "unnamedStringInBean";

        String actual = getPropertyName(testBeanClass, fieldName, "");
        String expected = getExpectedProperty(testBeanClass, fieldName);

        Assertions.assertEquals(expected, actual);
    }

    String getPropertyName(Class<?> parentClass, String fieldName, String prefix) throws NoSuchFieldException {
        Field field = parentClass.getDeclaredField(fieldName);
        ConfigProperty configProperty = field.getAnnotation(ConfigProperty.class);

        return MPConfigAnnotationProcessor.getPropertyName(configProperty, parentClass, field, prefix);
    }

    @Test
    void isOptional() throws NoSuchFieldException {
        Field optionalField = AggregatedConfig.class.getDeclaredField("optionalString");
        Assertions.assertTrue(Utils.isOptionalField(optionalField));

        Field requiredField = AggregatedConfig.class.getDeclaredField("exampleString");
        Assertions.assertFalse(Utils.isOptionalField(requiredField));
    }

    @Test
    void propertyNameToEnvironmentVariable() {
        Map<String, String> inputAndExpectedMap = Map.of(
                "com.example.configtest", "COM_EXAMPLE_CONFIGTEST",
                "com.example.[1].key", "COM_EXAMPLE__1__KEY",
                "api-configs.\"customer-api\".host", "API_CONFIGS__CUSTOMER_API__HOST"
        );

        inputAndExpectedMap.forEach((input, expected) -> {
            String actual = Utils.configPropertyToEnvironmentVariable(input);
            Assertions.assertEquals(expected, actual);
        });
    }

    @Test
    void getDefaultValue_hasDefaultValue() throws NoSuchFieldException {
        Class<AggregatedConfig> aggregatedConfigClass = AggregatedConfig.class;
        Field field = aggregatedConfigClass.getDeclaredField("defaultString");
        ConfigProperty configProperty = field.getAnnotation(ConfigProperty.class);

        String actual = MPConfigAnnotationProcessor.getDefaultValue(configProperty);
        String expected = "I have a default value";
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getDefaultValue_noDefaultValue() throws NoSuchFieldException {
        Class<AggregatedConfig> aggregatedConfigClass = AggregatedConfig.class;
        Field field = aggregatedConfigClass.getDeclaredField("exampleString");
        ConfigProperty configProperty = field.getAnnotation(ConfigProperty.class);

        String actual = MPConfigAnnotationProcessor.getDefaultValue(configProperty);
        Assertions.assertTrue(actual.isEmpty());
    }
}
