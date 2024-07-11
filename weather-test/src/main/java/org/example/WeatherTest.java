package org.example;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.MessageFormat;

import uk.co.gencoreoperative.llama3.Llama3Runner;

public class WeatherTest {
    public static void main(String... args) throws IOException, InterruptedException {
        String latitude = "51.5074"; // Latitude for London
        String longitude = "-0.1278"; // Longitude for London
        String url = MessageFormat.format("https://api.open-meteo.com/v1/forecast?" +
                        "latitude={0}&" +
                        "longitude={1}&" +
                        "hourly=temperature_2m," +
                        "weather_code&" +
                        "forecast_days=1",
                latitude,
                longitude);

        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String weather = response.body().toString();

            Llama3Runner runner = new Llama3Runner(new File("Meta-Llama-3-8B-Instruct-Q4_0.gguf").toPath());
            System.out.println(runner.run("hello"));
        }
    }

    private static void parseAndPrint(String responseBody) {
        System.out.println(responseBody);
    }
}
