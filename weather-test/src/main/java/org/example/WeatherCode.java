package org.example;

public enum WeatherCode {
    CLEAR_SKY(0, "Clear sky"),
    MAINLY_CLEAR_PARTLY_CLOUDY_OVERCAST(new int[]{1, 2, 3}, "Mainly clear, partly cloudy, and overcast"),
    FOG_DEPOSITING_RIME_FOG(new int[]{45, 48}, "Fog and depositing rime fog"),
    DRIZZLE_LIGHT_MODERATE_DENSE(new int[]{51, 53, 55}, "Drizzle: Light, moderate, and dense intensity"),
    FREEZING_DRIZZLE_LIGHT_DENSE(new int[]{56, 57}, "Freezing Drizzle: Light and dense intensity"),
    RAIN_SLIGHT_MODERATE_HEAVY(new int[]{61, 63, 65}, "Rain: Slight, moderate and heavy intensity"),
    FREEZING_RAIN_LIGHT_HEAVY(new int[]{66, 67}, "Freezing Rain: Light and heavy intensity"),
    SNOW_FALL_SLIGHT_MODERATE_HEAVY(new int[]{71, 73, 75}, "Snow fall: Slight, moderate, and heavy intensity"),
    SNOW_GRAINS(77, "Snow grains"),
    RAIN_SHOWERS_SLIGHT_MODERATE_VIOLENT(new int[]{80, 81, 82}, "Rain showers: Slight, moderate, and violent"),
    SNOW_SHOWERS_SLIGHT_HEAVY(new int[]{85, 86}, "Snow showers slight and heavy"),
    THUNDERSTORM_SLIGHT_MODERATE(95, "Thunderstorm: Slight or moderate"),
    THUNDERSTORM_HAIL_SLIGHT_HEAVY(new int[]{96, 99}, "Thunderstorm with slight and heavy hail");

    private final int[] codes;
    private final String description;

    WeatherCode(int code, String description) {
        this.codes = new int[]{code};
        this.description = description;
    }

    WeatherCode(int[] codes, String description) {
        this.codes = codes;
        this.description = description;
    }

    public static String getDescription(int code) {
        for (WeatherCode weatherCode : values()) {
            for (int c : weatherCode.codes) {
                if (c == code) {
                    return weatherCode.description;
                }
            }
        }
        return "Unknown weather code";
    }
}
