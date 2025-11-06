The Llama3 model has a context size of 512. See if we can expand that, otherwise we need to protect
against the user providing more text than this.

Fix packaging so that the API and its dependent Murkel package are packaged together into something
that could be used by someone else.

https://github.com/mukel/llama3.java?tab=readme-ov-file#optional-makefile--manually-build-and-run
Use this step to package up the Murkel dependency into a jar.
We can then consume this locally.