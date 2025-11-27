package uk.co.gencoreoperative;

import java.io.File;

import uk.co.gencoreoperative.ai.Response;
import uk.co.gencoreoperative.runner.MukelRunner;

/**
 * The {@link mukel.qwen2.Qwen2} LLM runner gives us more flexibility on the scale of intelligence compared
 * to model file size. In addition, we note that this model does not require Java Preview features in order
 * to run.
 */
public class Qwen2Test {
    static void main() {
        File model = new File(System.getProperty("user.dir"), "Qwen2.5-0.5B-Instruct-Q4_0.gguf");
        MukelRunner runner = new MukelRunner(model.toPath());
        Response response = runner.runWithResponse("Tell me a joke");
        System.out.println(response.response());
        System.out.println(response.context());
    }
}
