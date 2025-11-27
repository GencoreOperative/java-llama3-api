package uk.co.gencoreoperative;

import static java.text.MessageFormat.format;

import java.io.File;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import uk.co.gencoreoperative.runner.MukelRunner;

public class GeneralKnowledgeDemo {
    static void main(String... args) {
        MukelRunner qwen2Runner = new MukelRunner(new File(System.getProperty("user.dir"), "Qwen2.5-0.5B-Instruct-Q4_0.gguf").toPath());
        MukelRunner llama3Runner = new MukelRunner(new File(System.getProperty("user.dir"), "Llama-3.2-1B-Instruct-Q4_0.gguf").toPath());
        List<MukelRunner> runners = List.of(qwen2Runner, llama3Runner);

        String systemPrompt = "Be concise";

        List<String> questions = Arrays.asList(
            "Highest mountain in the world",
            "Longest river in the world",
            "Age of the Egyptian Pyramids"
        );

        // Demonstration of simple general knowledge question answering
        System.out.println(format("System Prompt: {0}\n", systemPrompt));
        for (String question : questions) {
            System.out.println(format("Question: {0}", question));
            for (MukelRunner runner : runners) {
                System.out.println(format("{0}: {1}", runner.getModelName(), runner.run(systemPrompt, question)));
            }
            System.out.println();
        }
    }
}
