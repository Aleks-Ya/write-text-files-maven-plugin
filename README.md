#  write-text-files-maven-plugin
Create text files with content specified in pom.xml (it's possible use maven properties).

## Versions history
### v 1.0
1. First version
2. Multiple files in one configuration
3. Maven properties substitution
4. File charset
5. Null value per file and for all files (default is empty string)
6. Throw exception if property was not found (optional)

## Example

### pom.xml
```
<project>
    <groupId>ru.yaal.maven</groupId>
    <artifactId>run-plugin</artifactId>
    <version>1.0-SNAPSHOT</version>
    <name>Example for write-text-files-maven-plugin</name>

    <properties>
        <message>Machines should work; people should think.</message>
    </properties>

    <build>
        <plugins>
            <plugin>
                <groupId>ru.yaal.maven</groupId>
                <artifactId>write-text-files-maven-plugin</artifactId>
                <version>1.0</version>
                <configuration>
                    <nullValue>global-null-value</nullValue>
                    <nullValueException>false</nullValueException>
                    <charset>UTF-8</charset>
                    <files>
                        <file>
                            <path>target/version.txt</path>
                            <nullValue>null value per file</nullValue>
                            <lines>
                                <line>Name: ${project.name}</line>
                                <line>ArtifactId: ${project.artifactId}</line>
                                <line>Version: ${project.version}</line>
                                <line>Description: ${project.description}</line>
                                <line>Message: ${message}</line>
                                <line>Buy!</line>
                            </lines>
                        </file>
                    </files>
                </configuration>
                <executions>
                    <execution>
                        <id>write-text-files</id>
                        <phase>prepare-package</phase>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>

    <modelVersion>4.0.0</modelVersion>
</project>
```
### Output file version.txt
```
Name: Example for write-text-files-maven-plugin
ArtifactId: run-plugin
Version: 1.0-SNAPSHOT
Description: null value per file
Message: Machines should work; people should think.
Buy!
```

