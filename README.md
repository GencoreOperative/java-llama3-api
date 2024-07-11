# Introduction

This Java-based project aims to provide a simple API that developers can use to invoke an AI from within 
the JVM. The primary implementation for this API is the excellent work of [Murkel](https://github.com/mukel) 
in their [Llama3 project](https://github.com/mukel/llama3.java). In the future, as additional Java based 
inference engines become available, we may consider updating this API.

# AI Programming

"Why call an AI from within a Java application?"

Large Language Models (LLM) bring multiple potential benefits to developers:
- Natural Language Processing: enables the AI to work with human text
- Decision-Making: By providing text to the AI, we can ask it to make decisions that inform program behaviour

A simple use case of this idea might be a simple program that retrieves a weather forecast in JSON format,
passes this to the AI with a human level instruction like; "I am going to the office at 0900, do I need an 
umbrella?"

# Prerequisites

- Java JDK 21 or higher
- Maven 3.x or higher
- Make & Docker (optional)

### Building

The simplest way to build the project is to use the provided Makefile.
This will invoke a Docker Maven image that contains the right version of Java to build the project.

```commandline
make build
```

Alternatively, if you already have Java 21 installed, then you can build using the standard Maven command:

```commandline
mvn package
```

## Running the Test

To demonstrate that the project is working, a simple test class has been provided that will invoke the AI
with a basic maths question:

```commandline
make run
```
This will output the following:
```commandline
1 + 1 = 2
```

## Development with the Library

In order to use the library, we will need to include this code in our project. The simplest way to do this is to
install the library into your local Maven repository:

```commandline
mvn install
```

Then, in your project, you can include the library as a dependency:

```xml
<dependency>
    <groupId>uk.co.gencoreoperative</groupId>
    <artifactId>java-llama3-api</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

Lastly, you need to have available the Llama3 Model file. This is a large file and is not included in this project but
can be downloaded from [here](https://huggingface.co/mukel/Meta-Llama-3-8B-Instruct-GGUF).

The following example code shows how you might invoke the library:
```java
String prompt = "your prompt here";
Path modelPath = "path to model file here";
Run runner = new Llama3Runner(modelPath);
System.out.println(runner.run(prompt));
```

## License

This project is licensed under the same license as the Llama3.java project, MIT License - see the 
[LICENSE.md](LICENSE.md) file for details

## Acknowledgments

- Based on the excellent work by [mukel](https://github.com/mukel/llama3.java)