package com.bibliotecadigital.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class Environment {

    private static final Properties properties = new Properties();

    static {
        try (InputStream input = Environment.class.getClassLoader()
                .getResourceAsStream("environments/environment.properties")) {
            if (input != null) {
                properties.load(input);
            }
        } catch (IOException e) {
            throw new RuntimeException("Falha ao carregar environment.properties", e);
        }
    }

    private Environment() {
    }

    public static String getBaseUrl() {
        return properties.getProperty("base.url");
    }

    public static long getResponseTimeout() {
        return Long.parseLong(properties.getProperty("response.timeout"));
    }

    public static int getConcurrentRequests() {
        return Integer.parseInt(properties.getProperty("concurrent.requests"));
    }
}
