#  write-text-files-maven-plugin
**Create text files with content specified in pom.xml (it's possible to use maven properties).**

It process:
* project properties (like `${project.artifactId}`)
* pom.xml properties (like `${spring.version}`),
* java properties (like `${java.version}`)
* OS environment (like `${env.HOME}`)
* maven's settings.xml properties (like `${settings.offline}`)


## Versions history
### v 1.1
0. Escape symbols support (double dollar): `$${property}`
1. Process empty lines bug
2. Change log level to info for overwriting exists file

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
                <version>1.1</version>
                <configuration>
                    <nullValue>global-null-value</nullValue>
                    <nullValueException>false</nullValueException>
                    <charset>UTF-8</charset>
                    <files>
                        <file>
                            <path>target/version.txt</path>
                            <nullValue>null value for file</nullValue>
                             <lines>
                                 <line>=== EASY STRING ===</line>
                                 <line>How to use write-text-files-maven-plugin</line>
                                 <line/>
                                 <line>=== PROJECT PROPERTIES ===</line>
                                 <line>Name: ${project.name}</line>
                                 <line>ArtifactId: ${project.artifactId}</line>
                                 <line>Version: ${project.version}</line>
                                 <line/>
                                 <line>=== POM.XML PROPERTY ===</line>
                                 <line>Message: ${message}</line>
                                 <line/>
                                 <line>=== JAVA PROPERTIES ===</line>
                                 <line>Java vendor: ${java.vendor}</line>
                                 <line>Java version: ${java.version}</line>
                                 <line>Java home: ${java.home}</line>
                                 <line/>
                                 <line>=== OS ENVIRONMENT ===</line>
                                 <line>User dir: ${env.HOME}</line>
                                 <line/>
                                 <line>=== MAVEN SETTINGS.XML ===</line>
                                 <line>Offline: ${settings.offline}</line>
                                 <line/>
                                 <line>=== NULL VALUES ===</line>
                                 <line>By default null values are replaced with empty string (you can change it)</line>
                                 <line>Description: ${project.description}</line>
                                 <line/>
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
### Console output
```
[INFO] --- write-text-files-maven-plugin:1.1:write-text-files (write-text-files) @ run-plugin ---
[INFO] Overwrite file: /home/aleks/tmp/run_plugin/target/version.txt
```
### Output file version.txt

<pre>
=== EASY STRING ===
How to use write-text-files-maven-plugin
<br/>
=== PROJECT PROPERTIES ===
Name: Example for write-text-files-maven-plugin
ArtifactId: run-plugin
Version: 1.0-SNAPSHOT
<br/>
=== POM.XML PROPERTY ===
Message: Machines should work; people should think.
<br/>
=== JAVA PROPERTIES ===
Java vendor: Oracle Corporation
Java version: 1.7.0_80
Java home: /usr/lib/jvm/java-7-oracle/jre
<br/>
=== OS ENVIRONMENT ===
User dir: /home/aleks
<br/>
=== MAVEN SETTINGS.XML ===
Offline: false
<br/>
=== NULL VALUES ===
By default null values are replaced with empty string (you can change it)
Description: null value for file
<br/>
Buy!
</pre>

