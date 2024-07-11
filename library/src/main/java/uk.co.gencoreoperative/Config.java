package uk.co.gencoreoperative;

import java.nio.file.Path;

import com.beust.jcommander.Parameter;

public class Config {
    @Parameter(names = {"--model", "-m"}, description = "required, path to .gguf file", required = true)
    public Path modelPath;
}
