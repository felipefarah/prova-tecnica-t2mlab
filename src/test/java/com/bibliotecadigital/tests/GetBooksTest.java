package com.bibliotecadigital.tests;

import com.bibliotecadigital.clients.BooksClient;
import com.bibliotecadigital.utils.Environment;
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

@Epic("Books API")
@Feature("GET /Books")
public class GetBooksTest {

    private BooksClient booksClient;

    @BeforeEach
    void setUp() {
        booksClient = new BooksClient();
    }

    @Test
    @DisplayName("TC_BOOKS_001 - GET /Books - Listar todos os livros com sucesso")
    @Description("Validar status 200 e lista retornada")
    void shouldListAllBooksSuccessfully() {
        TestLogger.logTestStart("TC_BOOKS_001 - GET /Books - Listar todos os livros com sucesso");
        TestLogger.logRequest("GET", "/Books", null);

        Response response = booksClient.getAllBooks();
        TestLogger.logResponse(response);

        try {
            response.then()
                    .statusCode(200)
                    .body("$", is(notNullValue()))
                    .body("size()", greaterThan(0))
                    .body("[0].id", is(notNullValue()))
                    .body("[0].title", is(notNullValue()));
            TestLogger.logPass();
        } catch (AssertionError e) {
            TestLogger.logFail("status 200, lista não vazia", "status " + response.getStatusCode());
            throw e;
        }
    }

    @Test
    @DisplayName("TC_BOOKS_002 - GET /Books - Validar schema da lista de livros")
    @Description("Validar schema completo da lista: id, title, description, pageCount, excerpt, publishDate")
    void shouldValidateBookListSchema() {
        TestLogger.logTestStart("TC_BOOKS_002 - GET /Books - Validar schema da lista de livros");
        TestLogger.logRequest("GET", "/Books", null);

        Response response = booksClient.getAllBooks();
        TestLogger.logResponse(response);

        try {
            response.then()
                    .statusCode(200)
                    .body(matchesJsonSchemaInClasspath("schemas/books-list-schema.json"));
            TestLogger.logPass();
        } catch (AssertionError e) {
            TestLogger.logFail("schema válido com campos: id, title, description, pageCount, excerpt, publishDate", e.getMessage());
            throw e;
        }
    }

    @Test
    @DisplayName("TC_BOOKS_003 - GET /Books - Validar tempo de resposta da listagem")
    @Description("Validar tempo de resposta dentro do threshold de 2000ms")
    void shouldRespondWithinTimeout() {
        long timeout = Environment.getResponseTimeout();
        TestLogger.logTestStart("TC_BOOKS_003 - GET /Books - Validar tempo de resposta da listagem");
        TestLogger.logRequest("GET", "/Books", null);

        Response response = booksClient.getAllBooks();
        TestLogger.logResponse(response);
        long responseTime = response.getTime();
        System.out.println("  Tempo:    " + responseTime + "ms (threshold: " + timeout + "ms)");

        try {
            response.then()
                    .statusCode(200)
                    .time(lessThan(timeout));
            TestLogger.logPass();
        } catch (AssertionError e) {
            TestLogger.logFail("tempo < " + timeout + "ms", "tempo " + responseTime + "ms");
            throw e;
        }
    }
}
