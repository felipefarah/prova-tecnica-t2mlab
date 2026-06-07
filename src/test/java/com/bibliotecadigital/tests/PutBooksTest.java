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

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

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

        Response response = booksClient.updateBook(1, payload);

        response.then()
                .statusCode(200)
                .body("id", equalTo(1))
                .body("title", equalTo("Livro Atualizado"))
                .body("description", equalTo("Descrição Atualizada"))
                .body("pageCount", equalTo(300));
    }

    @Test
    @DisplayName("TC_BOOKS_012 - PUT /Books/{id} - Atualizar livro inexistente")
    @Description("Atualizar livro com ID inexistente e documentar comportamento")
    void shouldHandleUpdateOfNonExistentBook() {
        var payload = BookFixtures.validBookForUpdate(999999);

        Response response = booksClient.updateBook(999999, payload);

        // A FakeRestAPI retorna 200 mesmo para IDs inexistentes no PUT.
        // Em uma API real, deveria retornar 404.
        int statusCode = response.getStatusCode();
        org.junit.jupiter.api.Assertions.assertTrue(
                statusCode == 200 || statusCode == 404,
                "Status esperado: 200 (comportamento observado) ou 404 (comportamento ideal). Obtido: " + statusCode
        );
    }

    @Test
    @DisplayName("TC_BOOKS_013 - PUT /Books/{id} - Validar idempotência da atualização")
    @Description("Executar mesmo PUT duas vezes e comparar respostas para validar idempotência")
    void shouldBeIdempotent() {
        var payload = BookFixtures.validBookForUpdate(1);

        Response firstResponse = booksClient.updateBook(1, payload);
        Response secondResponse = booksClient.updateBook(1, payload);

        firstResponse.then().statusCode(200);
        secondResponse.then().statusCode(200);

        assertEquals(
                firstResponse.getBody().asString(),
                secondResponse.getBody().asString(),
                "PUT deve ser idempotente: respostas devem ser idênticas"
        );
    }
}
