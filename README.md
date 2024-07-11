# Project Title

This Java-based project aims to provide a simple API that developers can use to invoke
an AI from within the JVM. This project is make possible by the excellent work of 
Murkel in their Llama3 project.

Large Language Models (LLM) bring multiple potential benefits to developers:
- Natural Language Processing: enables the AI to work with human text
- Decision-Making: By providing data to the AI, we can ask it to make decisions that inform program behaviour

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

## Running the tests

Then using the Makefile, we can run the project with:

```bash
make run
```

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

## Acknowledgments

- Based on the excellent work by [mukel](https://github.com/mukel/llama3.java)