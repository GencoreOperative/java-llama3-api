package uk.co.gencoreoperative;

import com.beust.jcommander.JCommander;

import uk.co.gencoreoperative.ai.Response;
import uk.co.gencoreoperative.ai.Run;
import uk.co.gencoreoperative.llama3.Llama3Runner;

/**
 * A demonstration class that shows how we can invoke the LLM from within a Java application.
 */
public class Test {
    static void main(String... args) {
        Config config = new Config();
        JCommander.newBuilder()
                .addObject(config)
                .programName("Test")
                .build()
                .parse(args);

        // Simple prompt demonstration
        System.out.println("Single Prompt Mode:");
        String prompt = "1 + 1 = ";
        System.out.print(prompt);
        Run runner = new Llama3Runner(config.modelPath);
        Response response1 = runner.runWithResponse(prompt);
        System.out.println(response1.response());
        System.out.println(response1.context());

        System.out.println();

        // Example of using the system prompt to modify behaviour
        System.out.println("System and User Prompt Mode:");
        prompt = "1 + 1 = ";
        String systemPrompt = "Use words instead of numbers";
        System.out.println(systemPrompt);
        System.out.print(prompt);
        runner = new Llama3Runner(config.modelPath);
        Response response2 = runner.runWithResponse(systemPrompt, prompt);
        System.out.println(response2.response());
        System.out.println(response2.context());

        System.out.println();

        // Demonstration of simple general knowledge question answering
        runner = new Llama3Runner(config.modelPath);
        System.out.println(runner.run("Be concise", "Highest mountain in the world"));
    }
}
