*Context Window Response*
As part of processing, it would be valuable to inform the user of both the input size, context window usage, and
output size. This would provide the caller with more programmatic capability.

*Context Window Control*
The Llama3 model has a context size of 512. This is artificially limited to reduce processing time. We should
at least expose this as a capability that the user as control over to allow them to control compute spend.
The model (Llama3 in this case) has a hard upper limit of 8K tokens which would factor into this.

*Chat Mode aka Multiple Requests*
If we can design the handling code so that it supports chat mode, then this will be more suitable for tasks
where we are expecting to make repeated calls to the LLM. If we are keeping the context window short and using
multiple steps to complete processing, this will be a strong usage of this feature.

*Qwen 2 (2.5?) Support*
We should consider whether we also want to support the Qwen2 model: https://github.com/mukel/qwen2.svm.java
This is interesting because the Qwen two models offer a better size to intelligence scaling.
https://huggingface.co/mukel/Qwen2.5-0.5B-Instruct-GGUF (361 MB)
https://huggingface.co/mukel/Qwen2.5-1.5B-Instruct-GGUF (1.01 GB)

https://github.com/mukel/llama3.java?tab=readme-ov-file#optional-makefile--manually-build-and-run
Use this step to package up the Murkel dependency into a jar.
We can then consume this locally.