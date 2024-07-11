package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import com.google.gson.Gson;

public class IpGeolocation {

    public static Location getLocation() {
        String apiUrl = "http://ipinfo.io/json";

        // Parse JSON response using Gson
        Gson gson = new Gson();
        IpInfo ipInfo = gson.fromJson(httpRequest(apiUrl), IpInfo.class);

        // Extract latitude and longitude
        String[] latLon = ipInfo.loc.split(",");
        double latitude = Double.parseDouble(latLon[0]);
        double longitude = Double.parseDouble(latLon[1]);

        Location location = new Location();
        location.latitude = String.valueOf(latitude);
        location.longitude = String.valueOf(longitude);
        location.city = ipInfo.city;
        return location;
    }

    private static String httpRequest(String apiUrl) {
        URL url = getUrl(apiUrl);
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            StringBuilder content;
            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                content = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
            }
            // Close connections
            connection.disconnect();
            return content.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static URL getUrl(String apiUrl) {
        try {
            return new URL(apiUrl);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public static final class Location {
        String latitude;
        String longitude;
        String city;
    }

    // IpInfo class to model the JSON response
    static class IpInfo {
        String ip;
        String hostname;
        String city;
        String region;
        String country;
        String loc;
        String org;
        String postal;
        String timezone;
    }
}
