package uk.co.gencoreoperative;

import com.beust.jcommander.JCommander;

import uk.co.gencoreoperative.ai.Run;
import uk.co.gencoreoperative.llama3.Llama3Runner;

public class Test {
    public static void main(String... args) {
        Config config = new Config();
        JCommander.newBuilder()
                .addObject(config)
                .programName("Test")
                .build()
                .parse(args);

        System.out.println("Single Prompt Mode:");
        String prompt = "1 + 1 = ";
        System.out.print(prompt);
        Run runner = new Llama3Runner(config.modelPath);
        System.out.println(runner.run(prompt));

        System.out.println("System and User Prompt Mode:");
        System.out.print(prompt);
        runner = new Llama3Runner(config.modelPath);
        System.out.println(runner.run("Respond in binary", prompt));
    }
}
