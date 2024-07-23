# MicroProfile Config Docgen Maven Plugin

Generates an AsciiDoc file from MicroProfile Config and Smallrye Config annontations.

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
            <version>1.0.0-SNAPSHOT</version>
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

Manual execution:

```
mvn microprofile-config-docgen:generate
```

## Configuration

| Property    | Descriptions                        |
|-------------|-------------------------------------|
| packageName | Root package name to be scanned     |
| outputFile  | Target output file. Ends with .adoc |
