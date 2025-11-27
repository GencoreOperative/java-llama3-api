package uk.co.gencoreoperative;

import java.io.File;

import uk.co.gencoreoperative.ai.Response;
import uk.co.gencoreoperative.runner.MukelRunner;

public class Qwen2Test {
    static void main() {
        File model = new File(System.getProperty("user.dir"), "Qwen2.5-0.5B-Instruct-Q4_0.gguf");
        MukelRunner runner = new MukelRunner(model.toPath());
        Response response = runner.runWithResponse("Hello!");
        System.out.println(response.response());
        System.out.println(response.context());
    }
}
