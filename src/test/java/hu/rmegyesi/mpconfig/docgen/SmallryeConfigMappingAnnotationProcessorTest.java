package hu.rmegyesi.mpconfig.docgen;

import hu.rmegyesi.mpconfig.test.smallryeconfig.NestedConfigMapping;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SmallryeConfigMappingAnnotationProcessorTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(SmallryeConfigMappingAnnotationProcessorTest.class);

    @Test
    void testNestedConfig() {
        SmallryeConfigMappingAnnotationProcessor processor = new SmallryeConfigMappingAnnotationProcessor();
        Stream<ConfigPropertyDocElement> configs = processor.processConfigMappingInterface(NestedConfigMapping.class);
        Set<String> expectedNames = getExpectedPropertyNames(NestedConfigMapping.class);
        LOGGER.info("Expected: {}", expectedNames);

        Set<String> configNames = configs.map(ConfigPropertyDocElement::name).collect(Collectors.toSet());
        LOGGER.info("Actual: {}", configNames);

        Assertions.assertTrue(expectedNames.containsAll(configNames));
    }

    public Set<String> getExpectedPropertyNames(Class<?> clazz) {
        Stream<Field> fields = Arrays.stream(clazz.getDeclaredFields());
        Stream<Method> methods = Arrays.stream(clazz.getDeclaredMethods());

        Stream<String> propertyNamesFromFields = fields.filter(field -> field.isAnnotationPresent(ExpectedProperties.class))
                .map(field -> field.getAnnotation(ExpectedProperties.class))
                .flatMap(expectedProperties -> Arrays.stream(expectedProperties.value()));

        Stream<String> propertyNamesFromMethods = methods.filter(method -> method.isAnnotationPresent(ExpectedProperties.class))
                .map(method -> method.getAnnotation(ExpectedProperties.class))
                .flatMap(expectedProperties -> Arrays.stream(expectedProperties.value()));

        return Stream.concat(propertyNamesFromFields, propertyNamesFromMethods)
                .collect(Collectors.toSet());
    }
}
