# write-text-files-maven-plugin

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=Aleks-Ya_write-text-files-maven-plugin&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=Aleks-Ya_write-text-files-maven-plugin)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=Aleks-Ya_write-text-files-maven-plugin&metric=coverage)](https://sonarcloud.io/summary/new_code?id=Aleks-Ya_write-text-files-maven-plugin)

**Create text files with content specified in pom.xml (it's possible to use maven properties).**

It processes all maven properties:
* project properties (like `${project.artifactId}`)
* pom.xml custom properties (like `${spring.version}`),
* java properties (like `${java.version}`)
* OS environment variables (like `${env.HOME}`)
* maven's settings.xml properties (like `${settings.offline}`)

## Example

### pom.xml
```xml
<project>
    <groupId>ru.yaal.maven</groupId>
    <artifactId>run-plugin</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <name>Example for write-text-files-maven-plugin</name>

    <properties>
        <message>Machines should work; people should think.</message>
    </properties>

    <build>
        <plugins>
            <plugin>
                <groupId>ru.yaal.maven</groupId>
                <artifactId>write-text-files-maven-plugin</artifactId>
                <version>2.2.0</version>
                <configuration>
                    <!-- Default values for all files-->
                    <charset>UTF-8</charset>
                    <lineSeparator>SYSTEM</lineSeparator> <!--SYSTEM (default for current OS), LF (Unix), CRLF (Windows)-->
                    <files>
                        <file>
                            <path>target/info.txt</path>
                            <lineSeparator>LF</lineSeparator>
                            <charset>Windows-1252</charset>
                            <lines>
                                <line>=== SIMPLE STRING ===</line>
                                <line>How to use write-text-files-maven-plugin</line>
                                <line>=== PROJECT PROPERTIES ===</line>
                                <line>Name: ${project.name}</line>
                                <line>ArtifactId: ${project.artifactId}</line>
                                <line>Version: ${project.version}</line>
                                <line>=== POM.XML PROPERTY ===</line>
                                <line>Message: ${message}</line>
                                <line>=== JAVA PROPERTIES ===</line>
                                <line>Java vendor: ${java.vendor}</line>
                                <line>Java version: ${java.version}</line>
                                <line>Java home: ${java.home}</line>
                                <line>=== OS ENVIRONMENT VARIABLES ===</line>
                                <line>User dir: ${env.HOME}</line>
                                <line>=== MAVEN SETTINGS.XML ===</line>
                                <line>Offline: ${settings.offline}</line>
                                <line>=== NULL VALUES ===</line>
                                <line>Description: ${project.description}</line>
                            </lines>
                        </file>
                        <file>
                            <path>target/version.txt</path>
                            <lines>
                                <line>${project.version}</line>
                            </lines>
                        </file>
                    </files>
                </configuration>
                <executions>
                    <execution>
                        <id>write-text-files</id>
                        <phase>prepare-package</phase>
                        <goals>
                            <goal>write-text-files</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>

    <modelVersion>4.0.0</modelVersion>
</project>
```
### Console output
```text
[INFO] --- write-text-files:2.1.1-SNAPSHOT:write-text-files (write-text-files) @ run-plugin ---
[INFO] Write to new file: /home/aleks/tmp/use-write-text-files/target/info.txt
[INFO] Line separator: \n
[INFO] Charset: windows-1252
[INFO] Output file length: 550 bytes
[INFO] Write to new file: /home/aleks/tmp/use-write-text-files/target/version.txt
[INFO] Line separator: \n
[INFO] Charset: UTF-8
[INFO] Output file length: 14 bytes
```

### Output files
`info.txt`:
```text
=== SIMPLE STRING ===
How to use write-text-files-maven-plugin
=== PROJECT PROPERTIES ===
Name: Example for write-text-files-maven-plugin
ArtifactId: run-plugin
Version: 1.0-SNAPSHOT
=== POM.XML PROPERTY ===
Message: Machines should work; people should think.
=== JAVA PROPERTIES ===
Java vendor: Azul Systems, Inc.
Java version: 21.0.6
Java home: /home/aleks/.sdkman/candidates/java/21.0.6-zulu
=== OS ENVIRONMENT VARIABLES ===
User dir: /home/aleks
=== MAVEN SETTINGS.XML ===
Offline: false
=== NULL VALUES ===
Description: ${project.description}
```

`version.txt`:
```text
1.0.0-SNAPSHOT
```

## Development
See [README-DEV.md](README-DEV.md)
