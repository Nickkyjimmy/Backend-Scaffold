include .env

JAVA=java
MVN=./mvnw
MODULE=generic
SERVICE_MODULE=$(MODULE)_service

version:
	java --version

build:
	$(MVN) package -DskipTests -pl $(SERVICE_MODULE) -am

run: build
	$(JAVA) -jar $(SERVICE_MODULE)/target/$(SERVICE_MODULE)-0.0.1-SNAPSHOT.jar

