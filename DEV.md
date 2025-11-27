# Qwen 2 (2.5?) Support
We should consider whether we also want to support the Qwen2 model: https://github.com/mukel/qwen2.svm.java
This is interesting because the Qwen two models offer a better size to intelligence scaling.

(361 MB) https://huggingface.co/mukel/Qwen2.5-0.5B-Instruct-GGUF
(703 MB) Llama-3.2-1B-Instruct-Q4_0.gguf
(1.01 GB) https://huggingface.co/mukel/Qwen2.5-1.5B-Instruct-GGUF

Depending on the user's needs, we could offer sliding scale of intelligence to file size.

Looking over the code it seems highly likely that both the [Qwen2](https://github.com/mukel/qwen2.svm.java) and
[Llama3](https://github.com/mukel/llama3.java) projects have the same design and interface. This means we can
treat both 'runner' classes as the same and just vary the model that is provided to the runner.

# README Improvements
The README needs to advertise the experience to the user earlier in the document. Show an example of what they 
can achieve in as few lines of code as possible.

# Extra JVM Argument
We might need to document an additional argument for this.
```
//RUNTIME_OPTIONS --add-modules=jdk.incubator.vector -Djdk.incubator.vector.VECTOR_ACCESS_OOB_CHECK=0
```

# Demo Classes
Introduce at least one, possibly more, demo classes that can be invoked from the command line that demonstrate
useful capabilities. These can be part of our testing process as we develop more capability with the LLM.
Demo - Question and Answer
Demo - Write a Python Script
Demo - Parse live JSON from a weather service

# Context Window Response
As part of processing, it would be valuable to inform the user of both the input size, context window usage, and
output size. This would provide the caller with more programmatic capability to understand how they are using the 
LLM.

Used - Tokens used during input
Total - Context Window Size
Output - Tokens generated in output

# Context Window Control
The Llama3 model has a context size of 512. This is artificially limited to reduce processing time. We should
at least expose this as a capability that the user as control over to allow them to control compute spend.
The model (Llama3 in this case) has a hard upper limit of 8K tokens which would factor into this.

# Provide a Demo to exercise Capability
The developer should aim to explore the capability of the GGUF model file before they attempt to integrate
it into their project. A command line tool that allows them to test out their ideas would be helpful.

# Chat Mode aka Multiple Requests
If we can design the handling code so that it supports chat mode, then this will be more suitable for tasks
where we are expecting to make repeated calls to the LLM. If we are keeping the context window short and using
multiple steps to complete processing, this will be a strong usage of this feature.

# Replace source code with Jar
https://github.com/mukel/llama3.java?tab=readme-ov-file#optional-makefile--manually-build-and-run
Use this step to package up the Murkel dependency into a jar.
We can then consume this locally.