package com.bibliotecadigital.utils;

import io.restassured.response.Response;

public final class TestLogger {

    private static final String SEPARATOR = "══════════════════════════════════════════════════════════════════════════════";
    private static final String LINE = "──────────────────────────────────────────────────────────────────────────────";

    private TestLogger() {
    }

    public static void logTestStart(String testName) {
        System.out.println();
        System.out.println(SEPARATOR);
        System.out.println("▶ " + testName);
        System.out.println(LINE);
    }

    public static void logRequest(String method, String endpoint, Object payload) {
        System.out.println("  Request:  " + method + " " + endpoint);
        if (payload != null) {
            System.out.println("  Payload:  " + payload);
        } else {
            System.out.println("  Payload:  (sem body)");
        }
    }

    public static void logResponse(Response response) {
        System.out.println("  Status:   " + response.getStatusCode());
        String body = response.getBody().asString();
        if (body != null && !body.isEmpty()) {
            if (body.length() > 500) {
                System.out.println("  Response: " + body.substring(0, 500) + "... [truncado]");
            } else {
                System.out.println("  Response: " + body);
            }
        } else {
            System.out.println("  Response: (vazio)");
        }
    }

    public static void logPass() {
        System.out.println("  ✔ PASSOU");
    }

    public static void logFail(String expected, String actual) {
        System.out.println("  ✘ FALHOU");
        System.out.println("    Esperado: " + expected);
        System.out.println("    Obtido:   " + actual);
    }
}
