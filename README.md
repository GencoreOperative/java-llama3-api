# Introduction

This Java-based project aims to provide a simple API that developers can use to invoke an AI from within 
the JVM. The primary implementation for this API is the excellent work of [Murkel](https://github.com/mukel) 
in their [Llama3 project](https://github.com/mukel/llama3.java) and 
[Qwen2 project](https://github.com/mukel/qwen2.svm.java).

This project acts as an API wrapper around these projects to provide a simplified way for developers to access
this capability.

# AI Programming

_"Why call an AI from within a Java application?"_

Large Language Models (LLM) bring multiple potential benefits to developers:
- Natural Language Processing: enables the AI to work with human text
- Interpretation: Where we want to make decisions based on the user's input, LLMs might present useful functionality
- Decision-Making: By providing text to the AI, we can ask it to make decisions that inform program behaviour

A simple use case of this idea might be a simple program that retrieves a weather forecast in JSON format,
passes this to the AI with a human level instruction like; "I am going to the office at 0900, do I need an 
umbrella?"

# Prerequisites

- Java JDK 21 or higher
- Maven 3.x or higher
- One of the supported LLM models downloaded locally

# Example

The following three lines of Java show how we can both initialise the model, in this case a Qwen2.5 0.5B model
and then invoke it with a single prompt.

```java
File model = new File(System.getProperty("user.dir"), "Qwen2.5-0.5B-Instruct-Q4_0.gguf");
MukelRunner runner = new MukelRunner(model.toPath());
System.out.println(runner.run("Tell me a joke"));
```

```commandline
Sure, here's a joke for you:

Why was the whole village wrong about the big tree?

Because it was a pine tree!
```

In this case, the Qwen2.5 0.5B model is somewhat 'limited' in its ability to tell jokes, but this example
serves to show the minimum amount of code required to execute a LLM inside Java.

## Downloading Q4_0 GGUF Model

This project is designed to work with LLM models in the GGUF format that have been quantized to 4-bit.
Mukel has helpfully provided a repository that contains suitable models to select from:

* [Llama3+](https://huggingface.co/collections/mukel/llama-3)
* [Qwen 2.5](https://huggingface.co/collections/mukel/qwen-25)

The following are a selection that may be of interest:

* 344MB [mukel/Qwen2.5-0.5B-Instruct-Q4_0.gguf](https://huggingface.co/mukel/Qwen2.5-0.5B-Instruct-GGUF/resolve/main/Qwen2.5-0.5B-Instruct-Q4_0.gguf?download=true)
* 671MB [mukel/Llama-3.2-1B-Instruct-GGUF](https://huggingface.co/mukel/Llama-3.2-1B-Instruct-GGUF/resolve/main/Llama-3.2-1B-Instruct-Q4_0.gguf?download=true)
* 960MB [mukel/Qwen2.5-1.5B-Instruct-GGUF](https://huggingface.co/mukel/Qwen2.5-1.5B-Instruct-GGUF/resolve/main/Qwen2.5-1.5B-Instruct-Q4_0.gguf?download=true)

As you increase the file size of the model, we generally expect to find more capability.

## Building

The simplest way to build the project is to have Java and Maven installed and build the project using the provided
`pom.xml` file.

```commandline
mvn package
```

## Running the Test

To demonstrate that the project is working, a simple test class has been provided that will invoke the AI
with a basic maths question:

```commandline
./test --model Qwen2.5-0.5B-Instruct-Q4_0.gguf --prompt "1 + 1 = ?"
```
This will output something that might look as follows:
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
    <artifactId>java-mukel-api</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

Lastly, you need to have available the Llama3 Model file. This is a large file and is not included in this project but
can be downloaded from [here](https://huggingface.co/mukel/Meta-Llama-3-8B-Instruct-GGUF). Alternatively, there is a 
smaller model available [here](https://huggingface.co/mukel/Llama-3.2-1B-Instruct-GGUF).

The following example code shows how you might invoke the library:
```java
String prompt = "your prompt here";
Path modelPath = "path to model file here";
Run runner = new Llama3Runner(modelPath);
System.out.println(runner.run(prompt));
```

## Working with an LLM

Performance is a key consideration when working with an LLM. The main driver on performance is both the size of the 
input prompt and the size of the response from the LLM. This scales linearly such that larger requests take longer
to process and larger responses take longer to output.

## License

This project is licensed under the same license as the Llama3.java project, MIT License - see the 
[LICENSE.md](LICENSE.md) file for details

## Acknowledgments

- Based on the excellent work by [mukel](https://github.com/mukel/llama3.java)