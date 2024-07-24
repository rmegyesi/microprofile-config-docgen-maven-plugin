# MicroProfile Config Docgen Maven Plugin

Generates an AsciiDoc file from MicroProfile Config and Smallrye Config annotations.

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
            <version>0.1.0</version>
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

| Property    | Descriptions                        | Default value          |
|-------------|-------------------------------------|------------------------|
| packageName | Root package name to be scanned     |                        |
| outputFile  | Target output file. Ends with .adoc | config-properties.adoc |
