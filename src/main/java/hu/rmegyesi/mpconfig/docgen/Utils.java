package hu.rmegyesi.mpconfig.docgen;

import java.lang.reflect.Field;
import java.util.Optional;

public class Utils {

    public static boolean isOptionalField(Field field) {
        return field.getType().equals(Optional.class);
    }

    public static String getFieldType(Field field) {
        return field.getGenericType().getTypeName();
    }

    public static String configPropertyToEnvironmentVariable(String configProperty) {
        return configProperty.toUpperCase().replaceAll("[^A-Za-z0-9]", "_");
    }
}
