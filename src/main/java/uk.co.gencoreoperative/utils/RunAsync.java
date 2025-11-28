package uk.co.gencoreoperative.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * A simple way of executing a {@link Runnable} asynchronously by spawning
 * {@link ExecutorService} to run it in the background.
 * <p>
 * <b>Resource Management:</b> The {@link ExecutorService} will be shutdown
 * automatically at the end of execution.
 */
public class RunAsync {
    private RunAsync() {
        // Utility class
    }

     public static Future<?> runAsync(Runnable runnable) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        return service.submit(() -> {
            try {
                runnable.run();
            } finally {
                service.shutdown();
            }
        });
     }
}
