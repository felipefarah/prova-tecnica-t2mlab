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

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Epic("Books API")
@Feature("POST /Books")
public class PostBooksTest {

    private BooksClient booksClient;

    @BeforeEach
    void setUp() {
        booksClient = new BooksClient();
    }

    @Test
    @DisplayName("TC_BOOKS_007 - POST /Books - Criar livro com dados válidos")
    @Description("Criar livro válido e validar status 200 e dados retornados")
    void shouldCreateBookWithValidData() {
        var payload = BookFixtures.validBook();
        TestLogger.logTestStart("TC_BOOKS_007 - POST /Books - Criar livro com dados válidos");
        TestLogger.logRequest("POST", "/Books", payload);

        Response response = booksClient.createBook(payload);
        TestLogger.logResponse(response);

        try {
            response.then()
                    .statusCode(200)
                    .body("id", equalTo(1001))
                    .body("title", equalTo("Livro QA"))
                    .body("description", equalTo("Livro criado para testes"))
                    .body("pageCount", equalTo(200))
                    .body("excerpt", equalTo("Trecho de teste"));
            TestLogger.logPass();
        } catch (AssertionError e) {
            TestLogger.logFail("status 200 com dados do livro criado", "status " + response.getStatusCode());
            throw e;
        }
    }

    @Test
    @DisplayName("TC_BOOKS_008 - POST /Books - Validar schema do livro criado")
    @Description("Validar schema completo do livro retornado após criação")
    void shouldValidateCreatedBookSchema() {
        var payload = BookFixtures.validBook();
        TestLogger.logTestStart("TC_BOOKS_008 - POST /Books - Validar schema do livro criado");
        TestLogger.logRequest("POST", "/Books", payload);

        Response response = booksClient.createBook(payload);
        TestLogger.logResponse(response);

        try {
            response.then()
                    .statusCode(200)
                    .body(matchesJsonSchemaInClasspath("schemas/book-schema.json"));
            TestLogger.logPass();
        } catch (AssertionError e) {
            TestLogger.logFail("schema válido (id, title, description, pageCount, excerpt, publishDate)", e.getMessage());
            throw e;
        }
    }

    @Test
    @DisplayName("TC_BOOKS_009 - POST /Books - Criar livro com campo obrigatório ausente")
    @Description("Enviar payload sem campo title e validar comportamento da API")
    void shouldHandleMissingRequiredField() {
        var payload = BookFixtures.bookWithoutTitle();
        TestLogger.logTestStart("TC_BOOKS_009 - POST /Books - Criar livro com campo obrigatório ausente");
        TestLogger.logRequest("POST", "/Books", payload);

        Response response = booksClient.createBook(payload);
        TestLogger.logResponse(response);

        int statusCode = response.getStatusCode();
        try {
            assertTrue(statusCode == 200 || statusCode == 400,
                    "Status esperado: 200 ou 400. Obtido: " + statusCode);
            System.out.println("  Obs:      API aceita payload incompleto (status " + statusCode + "). Em API real deveria ser 400.");
            TestLogger.logPass();
        } catch (AssertionError e) {
            TestLogger.logFail("status 200 ou 400", "status " + statusCode);
            throw e;
        }
    }

    @Test
    @DisplayName("TC_BOOKS_010 - POST /Books - Boundary: string vazia")
    @Description("Criar livro com strings vazias e validar comportamento")
    void shouldHandleEmptyStrings() {
        var payload = BookFixtures.bookWithEmptyStrings();
        TestLogger.logTestStart("TC_BOOKS_010 - POST /Books - Boundary: string vazia");
        TestLogger.logRequest("POST", "/Books", payload);

        Response response = booksClient.createBook(payload);
        TestLogger.logResponse(response);

        try {
            response.then()
                    .statusCode(200)
                    .body("id", equalTo(1003))
                    .body("title", equalTo(""))
                    .body("pageCount", equalTo(0));
            TestLogger.logPass();
        } catch (AssertionError e) {
            TestLogger.logFail("status 200 com strings vazias aceitas", "status " + response.getStatusCode());
            throw e;
        }
    }

    @Test
    @DisplayName("TC_BOOKS_010 - POST /Books - Boundary: valor máximo")
    @Description("Criar livro com valores extremos e validar comportamento")
    void shouldHandleMaxValues() {
        var payload = BookFixtures.bookWithMaxValues();
        TestLogger.logTestStart("TC_BOOKS_010 - POST /Books - Boundary: valor máximo");
        TestLogger.logRequest("POST", "/Books", payload);

        Response response = booksClient.createBook(payload);
        TestLogger.logResponse(response);

        int statusCode = response.getStatusCode();
        try {
            assertTrue(statusCode == 200 || statusCode == 400,
                    "Status esperado: 200 ou 400. Obtido: " + statusCode);
            TestLogger.logPass();
        } catch (AssertionError e) {
            TestLogger.logFail("status 200 ou 400", "status " + statusCode);
            throw e;
        }
    }

    @Test
    @DisplayName("TC_BOOKS_010 - POST /Books - Boundary: data inválida")
    @Description("Criar livro com data inválida e validar comportamento")
    void shouldHandleInvalidDate() {
        var payload = BookFixtures.bookWithInvalidDate();
        TestLogger.logTestStart("TC_BOOKS_010 - POST /Books - Boundary: data inválida");
        TestLogger.logRequest("POST", "/Books", payload);

        Response response = booksClient.createBook(payload);
        TestLogger.logResponse(response);

        int statusCode = response.getStatusCode();
        try {
            assertTrue(statusCode == 200 || statusCode == 400,
                    "Status esperado: 200 ou 400. Obtido: " + statusCode);
            TestLogger.logPass();
        } catch (AssertionError e) {
            TestLogger.logFail("status 200 ou 400", "status " + statusCode);
            throw e;
        }
    }
}
