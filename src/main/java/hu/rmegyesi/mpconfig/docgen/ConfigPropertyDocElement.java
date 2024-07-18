package hu.rmegyesi.mpconfig.docgen;

public record ConfigPropertyDocElement(String name, String environmentVariable, String defaultValue, String type,
                                       boolean optional) {
}
