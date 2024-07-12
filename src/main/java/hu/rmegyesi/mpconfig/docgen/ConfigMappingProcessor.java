package hu.rmegyesi.mpconfig.docgen;

import java.lang.reflect.Field;

public class ConfigMappingProcessor {

    AsciiDocWriter doc;

    public ConfigMappingProcessor(AsciiDocWriter doc) {
        this.doc = doc;
    }

    public void processClass(Class<?> targetClass) {
        for (Field field : targetClass.getDeclaredFields()) {

        }
    }
}
