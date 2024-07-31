# MicroProfile Config Docgen Maven Plugin

Generates a Markdown or AsciiDoc file from MicroProfile Config and Smallrye Config annotations.

## Supported annotations

### MicroProfile Config

* @ConfigProperties
* @ConfigProperty

### Smallrye Config

* @ConfigMapping
* @WithName
* @WithDefault
* @WithParentName

## Usage

Add the following snippet to your pom.xml:

```xml

<build>
    <plugins>
        ...
        <plugin>
            <groupId>hu.rmegyesi</groupId>
            <artifactId>microprofile-config-docgen-maven-plugin</artifactId>
            <version>0.2.1</version>
            <executions>
                <execution>
                    <goals>
                        <goal>generate</goal>
                    </goals>
                </execution>
            </executions>
            <configuration>
                <packageName>com.example</packageName>
            </configuration>
        </plugin>
    </plugins>
</build>
```

The plugin runs in `process-classes` phase.

Manual execution:

```
mvn microprofile-config-docgen:generate
```

## Configuration

| Property    | Descriptions                                                                      | Default value        |
|-------------|-----------------------------------------------------------------------------------|----------------------|
| packageName | Root package name to be scanned                                                   | ❕ Required           |
| outputFile  | Target output file.<br/>Recognized extensions: .md, .markdown, .adoc              | config-properties.md |
| format      | Overrides the detected format from the outputFile<br/>Values: ASCII_DOC, MARKDOWN |                      |
