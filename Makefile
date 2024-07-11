DOCKER_IMAGE="maven:3-eclipse-temurin-21-alpine"
RUN_IMAGE="openjdk:21"

.PHONY: build clean

build:
	docker run -it --rm \
	  --user $(shell id -u):$(shell id -g) \
	  -v $(HOME)/.m2:/var/maven/.m2:rw \
	  -e MAVEN_CONFIG=/var/maven/.m2 \
	  -e MAVEN_OPTS="$(MAVEN_OPTS) -Duser.home=/var/maven -Xmx2048m" \
	  -v "$(shell pwd):/tmp/source":rw \
	  -w /tmp/source \
	  $(DOCKER_IMAGE) \
	  mvn package


clean:
	docker run -it --rm \
	  --user $(shell id -u):$(shell id -g) \
	  -v $(HOME)/.m2:/var/maven/.m2:rw \
	  -e MAVEN_CONFIG=/var/maven/.m2 \
	  -e MAVEN_OPTS="$(MAVEN_OPTS) -Duser.home=/var/maven" \
	  -v "$(shell pwd):/tmp/source":rw \
	  -w /tmp/source \
	  $(DOCKER_IMAGE) \
	  mvn clean

test:
	docker run -it --rm \
	  -v "$(shell pwd):/tmp/source":rw \
	  -w /tmp/source \
	  $(RUN_IMAGE) \
	  java \
	  	--enable-preview \
	  	--add-modules jdk.incubator.vector \
	  	-jar api/target/api-1.0-SNAPSHOT.jar \
	  		--model Meta-Llama-3-8B-Instruct-Q4_0.gguf

weather:
	docker run -it --rm \
	  -v "$(shell pwd):/tmp/source":rw \
	  -w /tmp/source \
	  $(RUN_IMAGE) \
	  java \
	  	--enable-preview \
	  	--add-modules jdk.incubator.vector \
	  	-cp weather-test/target/weather-test-1.0-SNAPSHOT.jar \
		org.example.WeatherTest