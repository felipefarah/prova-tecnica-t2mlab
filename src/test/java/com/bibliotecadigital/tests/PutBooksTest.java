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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Epic("Books API")
@Feature("PUT /Books/{id}")
public class PutBooksTest {

    private BooksClient booksClient;

    @BeforeEach
    void setUp() {
        booksClient = new BooksClient();
    }

    @Test
    @DisplayName("TC_BOOKS_011 - PUT /Books/{id} - Atualizar livro existente")
    @Description("Atualizar livro existente e validar retorno com dados atualizados")
    void shouldUpdateExistingBook() {
        var payload = BookFixtures.validBookForUpdate(1);
        TestLogger.logTestStart("TC_BOOKS_011 - PUT /Books/{id} - Atualizar livro existente");
        TestLogger.logRequest("PUT", "/Books/1", payload);

        Response response = booksClient.updateBook(1, payload);
        TestLogger.logResponse(response);

        try {
            response.then()
                    .statusCode(200)
                    .body("id", equalTo(1))
                    .body("title", equalTo("Livro Atualizado"))
                    .body("description", equalTo("Descrição Atualizada"))
                    .body("pageCount", equalTo(300));
            TestLogger.logPass();
        } catch (AssertionError e) {
            TestLogger.logFail("status 200 com dados atualizados", "status " + response.getStatusCode());
            throw e;
        }
    }

    @Test
    @DisplayName("TC_BOOKS_012 - PUT /Books/{id} - Atualizar livro inexistente")
    @Description("Atualizar livro com ID inexistente e documentar comportamento")
    void shouldHandleUpdateOfNonExistentBook() {
        var payload = BookFixtures.validBookForUpdate(999999);
        TestLogger.logTestStart("TC_BOOKS_012 - PUT /Books/{id} - Atualizar livro inexistente");
        TestLogger.logRequest("PUT", "/Books/999999", payload);

        Response response = booksClient.updateBook(999999, payload);
        TestLogger.logResponse(response);

        int statusCode = response.getStatusCode();
        try {
            assertTrue(statusCode == 200 || statusCode == 404,
                    "Status esperado: 200 ou 404. Obtido: " + statusCode);
            System.out.println("  Obs:      API retorna " + statusCode + " para PUT em ID inexistente. Em API real deveria ser 404.");
            TestLogger.logPass();
        } catch (AssertionError e) {
            TestLogger.logFail("status 200 ou 404", "status " + statusCode);
            throw e;
        }
    }

    @Test
    @DisplayName("TC_BOOKS_013 - PUT /Books/{id} - Validar idempotência da atualização")
    @Description("Executar mesmo PUT duas vezes e comparar respostas para validar idempotência")
    void shouldBeIdempotent() {
        var payload = BookFixtures.validBookForUpdate(1);
        TestLogger.logTestStart("TC_BOOKS_013 - PUT /Books/{id} - Validar idempotência da atualização");
        TestLogger.logRequest("PUT", "/Books/1 (2x)", payload);

        Response firstResponse = booksClient.updateBook(1, payload);
        Response secondResponse = booksClient.updateBook(1, payload);

        System.out.println("  1ª resposta: status " + firstResponse.getStatusCode() + " | " + firstResponse.getBody().asString());
        System.out.println("  2ª resposta: status " + secondResponse.getStatusCode() + " | " + secondResponse.getBody().asString());

        try {
            firstResponse.then().statusCode(200);
            secondResponse.then().statusCode(200);
            assertEquals(firstResponse.getBody().asString(), secondResponse.getBody().asString(),
                    "PUT deve ser idempotente: respostas devem ser idênticas");
            TestLogger.logPass();
        } catch (AssertionError e) {
            TestLogger.logFail("respostas idênticas nas duas execuções", "respostas diferentes");
            throw e;
        }
    }
}
