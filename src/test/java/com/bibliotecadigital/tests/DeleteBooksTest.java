package com.bibliotecadigital.tests;

import com.bibliotecadigital.clients.BooksClient;
import com.bibliotecadigital.utils.TestLogger;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Epic("Books API")
@Feature("DELETE /Books/{id}")
public class DeleteBooksTest {

    private BooksClient booksClient;

    @BeforeEach
    void setUp() {
        booksClient = new BooksClient();
    }

    @Test
    @DisplayName("TC_BOOKS_014 - DELETE /Books/{id} - Excluir livro existente")
    @Description("Excluir livro existente e validar retorno com sucesso")
    void shouldDeleteExistingBook() {
        TestLogger.logTestStart("TC_BOOKS_014 - DELETE /Books/{id} - Excluir livro existente");
        TestLogger.logRequest("DELETE", "/Books/1", null);

        Response response = booksClient.deleteBook(1);
        TestLogger.logResponse(response);

        try {
            response.then().statusCode(200);
            TestLogger.logPass();
        } catch (AssertionError e) {
            TestLogger.logFail("status 200", "status " + response.getStatusCode());
            throw e;
        }
    }

    @Test
    @DisplayName("TC_BOOKS_015 - DELETE /Books/{id} - Excluir recurso já removido")
    @Description("Excluir livro que já foi removido e documentar comportamento")
    void shouldHandleDeleteOfAlreadyRemovedResource() {
        TestLogger.logTestStart("TC_BOOKS_015 - DELETE /Books/{id} - Excluir recurso já removido");
        TestLogger.logRequest("DELETE", "/Books/1 (2x)", null);

        booksClient.deleteBook(1);
        Response response = booksClient.deleteBook(1);

        System.out.println("  2ª exclusão:");
        TestLogger.logResponse(response);

        int statusCode = response.getStatusCode();
        try {
            assertTrue(statusCode == 200 || statusCode == 404,
                    "Status esperado: 200 ou 404. Obtido: " + statusCode);
            System.out.println("  Obs:      API retorna " + statusCode + " para exclusão repetida. Em API real deveria ser 404.");
            TestLogger.logPass();
        } catch (AssertionError e) {
            TestLogger.logFail("status 200 ou 404", "status " + statusCode);
            throw e;
        }
    }
}
