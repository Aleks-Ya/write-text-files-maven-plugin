# Development notes

## Build

### Build on the current Maven version
Build: `mvn clean package`

### Build on a specific Maven version
Available versions: https://hub.docker.com/_/maven/tags
- `./build.sh 3.6.3-jdk-11`
- `./build.sh 3.9.11-amazoncorretto-11`

## Release
1. Copy the passphrase for the secret key into the clipboard
2. Prepare: `mvn release:prepare` (revert: `mvn release:clean`)
3. Perform: `mvn release:perform`
4. Publish the deployment at https://central.sonatype.com/publishing/deployments
