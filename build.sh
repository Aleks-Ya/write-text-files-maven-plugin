MAVEN_VERSION="${1:-3.6.3}"

docker run -it --rm \
	-v "$HOME/.m2":/root/.m2 \
	-v "$PWD":/var/project \
	-w /var/project \
	maven:${MAVEN_VERSION} \
	mvn clean package
