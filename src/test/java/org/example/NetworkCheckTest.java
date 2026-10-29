package org.example;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkCheckTest {

    // Интеграционный тест. Нужен включенный интернет.
    @Test
    public void testGoogleReturns200() throws Exception {

        URL url = new URL("https://www.google.com");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setConnectTimeout(5000);
        conn.setReadTimeout(5000);

        int code = conn.getResponseCode();

        assertEquals(200, code, "Google должен отвечать кодом 200");
    }
}
