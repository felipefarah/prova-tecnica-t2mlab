package com.bibliotecadigital.tests;

import com.bibliotecadigital.clients.BooksClient;
import com.bibliotecadigital.fixtures.BookFixtures;
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
        Response response = booksClient.deleteBook(1);

        response.then()
                .statusCode(200);
    }

    @Test
    @DisplayName("TC_BOOKS_015 - DELETE /Books/{id} - Excluir recurso já removido")
    @Description("Excluir livro que já foi removido e documentar comportamento")
    void shouldHandleDeleteOfAlreadyRemovedResource() {
        // Primeira exclusão
        booksClient.deleteBook(1);

        // Segunda exclusão do mesmo recurso
        Response response = booksClient.deleteBook(1);

        // A FakeRestAPI retorna 200 mesmo para exclusão de recurso já removido.
        // Em uma API real, deveria retornar 404.
        int statusCode = response.getStatusCode();
        assertTrue(
                statusCode == 200 || statusCode == 404,
                "Status esperado: 200 (comportamento observado) ou 404 (comportamento ideal). Obtido: " + statusCode
        );
    }
}
