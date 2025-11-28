package uk.co.gencoreoperative;

import com.beust.jcommander.JCommander;

import uk.co.gencoreoperative.ai.Response;
import uk.co.gencoreoperative.runner.MukelRunner;

/**
 * {@link Test} provides command line access to running the model in single execution mode.
 */
public class Test {
    public static void main(String ... args) {
        Config config = new Config();
        JCommander.newBuilder()
                .addObject(config)
                .programName("Test")
                .build()
                .parse(args);

        MukelRunner runner = new MukelRunner(config.modelPath, config.temperature);
        Response response;
        if (config.systemPrompt != null) {
//            response = runner.runWithResponse(config.systemPrompt, config.prompt);
            runner.runAsStream(config.systemPrompt, config.prompt).forEach(System.out::print);
        } else {
//            response = runner.runWithResponse(config.prompt);
            runner.runAsStream(config.prompt).forEach(System.out::print);
        }
//        System.out.println(response);
    }
}
