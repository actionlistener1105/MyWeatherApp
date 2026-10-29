package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class WeatherServiceTest {
    
    @Test
    void shouldGiveExtremeColdAdvice() {
        WeatherService service = new WeatherService();
        String advice = service.getAdvice(-25, 5); // холод + дождь
        assertTrue(advice.contains("Лютый холод"));
        assertTrue(advice.contains("❄️🥶"));
    }

    @Test
    void shouldGiveHeatAdvice() {
        WeatherService service = new WeatherService();
        String advice = service.getAdvice(32, 0); // жара, без дождя
        assertTrue(advice.contains("кондиционер"));
        assertTrue(advice.contains("☀️🔥"));
    }

    @Test
    void shouldGiveWinterAdvice() {
        WeatherService service = new WeatherService();
        String advice = service.getAdvice(-5, 2); // зима + снег
        assertTrue(advice.contains("шубку"));
        assertTrue(advice.contains("❄️"));
    }

    @Test
    void shouldGiveUmbrellaAdvice() {
        WeatherService service = new WeatherService();
        String advice = service.getAdvice(20, 10); // нормальная температура + дождь
        assertTrue(advice.contains("зонт"));
        assertTrue(advice.contains("🌂"));
    }


    @Test
    void testParseWeather() throws Exception {
        WeatherService service = new WeatherService();

        String json = "{\"current_weather\":{\"temperature_2m\":8.5,\"precipitation\":2.3}}";

        WeatherService.WeatherData data = service.parseWeather(json);

        assertEquals(8.5, data.temperature, 0.01);
        assertEquals(2.3, data.precipitation, 0.01);
    }

}
