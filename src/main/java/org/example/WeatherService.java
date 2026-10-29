package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherService {

    private HttpURLConnection connection;
    private static final ObjectMapper mapper = new ObjectMapper();

    public WeatherService() {
        this.connection = null;
    }

    public WeatherService(HttpURLConnection mockConnection) {
        this.connection = mockConnection;
    }

    public void getWeather() throws Exception {
        double lat = 55.75;
        double lon = 37.62;
        String urlString = "https://api.open-meteo.com/v1/forecast?latitude=" + lat + "&longitude=" + lon + "&current_weather=true";

        URL url = new URL(urlString);

        if (connection == null) {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);
        }

        connection.connect();

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        String rawJson = response.toString();

        WeatherData data = parseWeather(rawJson);
        String advice = getAdvice(data.temperature, data.precipitation);

        System.out.println("=== Прогноз ===");
        System.out.println("Город: Moscow");
        System.out.println("Температура: " + data.temperature + " °C");
        System.out.println("Осадки: " + data.precipitation + " мм");
        System.out.println("Совет: " + advice);
        System.out.println("=================");
    }

    public WeatherData parseWeather(String rawJson) throws Exception {
        JsonNode root = mapper.readTree(rawJson);

        JsonNode tempNode = root.has("current_weather")
                ? root.path("current_weather").path("temperature_2m")
                : root.path("temperature_2m");

        JsonNode precipNode = root.has("current_weather")
                ? root.path("current_weather").path("precipitation")
                : root.path("precipitation");

        double temperature = tempNode.isNumber() ? tempNode.asDouble() : 0.0;
        double precipitation = precipNode.isNumber() ? precipNode.asDouble() : 0.0;

        return new WeatherData(temperature, precipitation);
    }

    public String getAdvice(double temperature, double precipitation) {
        // Сначала проверяем самые экстремальные случаи — они важнее
        if (temperature <= -20) {
            return "Лютый холод ❄️🥶. Лучше вообще не выходить.";
        }
        if (temperature >= 30) {
            return "Нам срочно нужен кондиционер! ☀️🔥. Держи голову в прохладе.";
        }

        // Потом обычные диапазоны
        if (temperature <= 0) {
            return "Пора доставать шубку! ❄️. Снежинки уже кружатся.";
        }
        if (temperature > 25) {
            return "Пора начинать пляжный сезон! 🌊☀️. Купальник готов?";
        }

        // Если температура в норме (от 0.1 до 25), смотрим на осадки
        boolean needsUmbrella = precipitation > 0;
        if (needsUmbrella) {
            return "Бери зонт! 🌂. Дождь не повод грустить.";
        } else {
            return "Зонт не нужен. ☀️. Просто наслаждайся погодой.";
        }
    }


    public static class WeatherData {
        public final double temperature;
        public final double precipitation;

        public WeatherData(double temperature, double precipitation) {
            this.temperature = temperature;
            this.precipitation = precipitation;
        }
    }
}
