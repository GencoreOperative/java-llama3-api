package uk.co.gencoreoperative;

import java.nio.file.Path;

import com.beust.jcommander.Parameter;

public class Config {
    @Parameter(names = {"--model", "-m"}, description = "required, path to .gguf file", required = true)
    public Path modelPath;

    @Parameter(names = {"--system-prompt", "-s"}, description = "System Prompt for the LLM", required = false)
    public String systemPrompt;

    @Parameter(names = {"--temperature", "-t"}, description = "Temperature control for the LLM", required = false)
    public float temperature = 0.1f;

    @Parameter(description = "User Prompt for the LLM", required = true)
    public String prompt;
}
