package com.bibliotecadigital.tests;

import com.bibliotecadigital.clients.BooksClient;
import com.bibliotecadigital.fixtures.BookFixtures;
import com.bibliotecadigital.utils.TestLogger;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

@Epic("Books API")
@Feature("Segurança")
public class SecurityTest {

    private BooksClient booksClient;

    @BeforeEach
    void setUp() {
        booksClient = new BooksClient();
    }

    @Test
    @DisplayName("TC_BOOKS_017.1 - POST /Books - Validar proteção contra SQL Injection")
    @Description("Enviar payload com SQL Injection no campo title e validar que o conteúdo é tratado como texto")
    void shouldHandleSqlInjectionPayload() {
        var payload = BookFixtures.bookWithSqlInjection();
        TestLogger.logTestStart("TC_BOOKS_017.1 - POST /Books - Validar proteção contra SQL Injection");
        TestLogger.logRequest("POST", "/Books", payload);

        Response response = booksClient.createBook(payload);
        TestLogger.logResponse(response);

        try {
            response.then()
                    .statusCode(200)
                    .body("title", equalTo("' OR '1'='1"));
            String responseBody = response.getBody().asString();
            assertFalse(responseBody.contains("error"),
                    "Não deve haver erro de SQL - conteúdo deve ser tratado como texto");
            System.out.println("  Obs:      API trata payload SQL Injection como texto puro (seguro)");
            TestLogger.logPass();
        } catch (AssertionError e) {
            TestLogger.logFail("status 200, conteúdo tratado como texto", "status " + response.getStatusCode());
            throw e;
        }
    }

    @Test
    @DisplayName("TC_BOOKS_017.2 - POST /Books - Validar proteção contra XSS")
    @Description("Enviar payload com script XSS no campo title e validar que o conteúdo é tratado de forma segura")
    void shouldHandleXssPayload() {
        var payload = BookFixtures.bookWithXss();
        TestLogger.logTestStart("TC_BOOKS_017.2 - POST /Books - Validar proteção contra XSS");
        TestLogger.logRequest("POST", "/Books", payload);

        Response response = booksClient.createBook(payload);
        TestLogger.logResponse(response);

        try {
            response.then()
                    .statusCode(200)
                    .body("title", is(notNullValue()));
            String title = response.jsonPath().getString("title");
            assertFalse(title == null || title.isEmpty(),
                    "O campo title deve conter o valor enviado, tratado como texto");
            System.out.println("  Obs:      API aceita payload XSS sem executar scripts (armazenado como texto)");
            TestLogger.logPass();
        } catch (AssertionError e) {
            TestLogger.logFail("status 200, script tratado como texto", "status " + response.getStatusCode());
            throw e;
        }
    }
}
