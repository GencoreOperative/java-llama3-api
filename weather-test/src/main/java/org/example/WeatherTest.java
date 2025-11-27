package org.example;

import static java.text.MessageFormat.format;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import uk.co.gencoreoperative.runner.Llama3Runner;

public class WeatherTest {
    public static void main(String... args) throws IOException, InterruptedException {
        IpGeolocation.Location location = IpGeolocation.getLocation();
        String latitude = location.latitude;
        String longitude = location.longitude;
        System.out.println(format("City: {0}", location.city));

        String url = format("https://api.open-meteo.com/v1/forecast?" +
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
            String weather = response.body();
            Gson gson = new Gson();
            WeatherResponse weatherResponse = gson.fromJson(weather, WeatherResponse.class);
            System.out.println(format("Weather Forecast: {0}", weather));
            String hourly = gson.toJson(weatherResponse.hourly);

            File currentDir = new File(System.getProperty("user.dir"));
            File parentDir = currentDir.getAbsoluteFile().getParentFile();

            Llama3Runner runner = new Llama3Runner(new File(parentDir, "Llama-3.2-1B-Instruct-Q4_0.gguf").toPath());
            String prompt = format("Answer only using Yes or No. Do I need an umbrella today?\n{0}", minify(hourly));
            System.out.println(prompt);
            System.out.println(format("Prompt Length: {0}", prompt.length()));
            String responseText = runner.run(prompt);
            System.out.println(format("Response: {0}\n", responseText));

        }
    }

    private static String minify(String json) {
        Gson gson = new Gson();
        JsonElement jsonElement = JsonParser.parseString(json);
        return gson.toJson(jsonElement);
    }

    class WeatherResponse {
        double latitude;
        double longitude;
        double generationtime_ms;
        int utc_offset_seconds;
        String timezone;
        String timezone_abbreviation;
        double elevation;
        HourlyUnits hourly_units;
        Hourly hourly;

        class HourlyUnits {
            String time;
            String temperature_2m;
            String weather_code;
        }

        class Hourly {
            List<String> time;
            List<Double> temperature_2m;
            List<Integer> weather_code;
        }
    }
}
