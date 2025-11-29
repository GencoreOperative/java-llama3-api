package uk.co.gencoreoperative;

import java.io.File;

import uk.co.gencoreoperative.ai.Response;
import uk.co.gencoreoperative.ai.Run;
import uk.co.gencoreoperative.runner.MukelRunner;

/**
 * A demonstration class that shows how we can invoke the LLM from within a Java application.
 */
public class SimpleTest {
    static void main(String... args) {
        Config config = new Config();
        config.modelPath = new File(System.getProperty("user.dir"), "Llama-3.2-1B-Instruct-Q4_0.gguf").toPath();

        // Simple prompt demonstration
        System.out.println("Single Prompt Mode:");
        config.prompt = "1 + 1 = ";
        System.out.print(config.prompt);
        Run runner = new MukelRunner(config.modelPath);
        Response response1 = runner.runWithResponse(config);
        System.out.println(response1.response());
        System.out.println(response1.context());

        System.out.println();

        // Example of using the system prompt to modify behaviour
        System.out.println("System and User Prompt Mode:");
        config.prompt = "1 + 1 = ";
        config.systemPrompt = "Use words instead of numbers";
        System.out.println(config.systemPrompt);
        System.out.print(config.prompt);
        runner = new MukelRunner(config.modelPath);
        Response response2 = runner.runWithResponse(config);
        System.out.println(response2.response());
        System.out.println(response2.context());

        System.out.println();

        // Demonstration of simple general knowledge question answering
        runner = new MukelRunner(config.modelPath);
        config.systemPrompt = "Be concise";
        config.prompt = "Highest mountain in the world";
        System.out.println(runner.run(config));
    }
}
