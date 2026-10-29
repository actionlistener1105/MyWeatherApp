package org.example;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;

import static org.mockito.Mockito.*;

// Интеграционный тест. Делаем заглушку вместо выхода в интернет.
class WeatherServiceMockTest {

    @Test
    void testGetWeatherReturnsCorrectAdvice() throws Exception {

        HttpURLConnection mockConn = mock(HttpURLConnection.class);

        String mockJson = "{\"current_weather\":{\"temperature_2m\":15.5,\"precipitation\":3.2}}";
        InputStream mockStream = new ByteArrayInputStream(mockJson.getBytes(StandardCharsets.UTF_8));

        when(mockConn.getInputStream()).thenReturn(mockStream);
        when(mockConn.getResponseCode()).thenReturn(200);

        WeatherService service = new WeatherService(mockConn);
        service.getWeather();
    }
}

