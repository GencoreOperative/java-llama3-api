No functional changes were made to the [mukel-llama3](https://github.com/mukel/llama3.java) project other than
to package it into a Maven module so that it can be used by other parts of the project.

Example of using the Chat feature:
```
export JAVA_HOME=/home/robert/opt/jdk-21.0.3+9 && export PATH=$JAVA_HOME/bin:$PATH && java -version

java \
	--enable-preview \
	--add-modules jdk.incubator.vector \
	-cp target/mukel-llama3-1.0-SNAPSHOT.jar mukel.Llama3 \
	--model ../Meta-Llama-3-8B-Instruct-Q4_0.gguf \
	--chat
```