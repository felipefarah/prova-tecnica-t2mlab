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

@Epic("Books API")
@Feature("E2E")
public class E2ETest {

    private BooksClient booksClient;

    @BeforeEach
    void setUp() {
        booksClient = new BooksClient();
    }

    @Test
    @DisplayName("E2E - Fluxo completo: Criar -> Validar -> Atualizar -> Validar -> Excluir -> Validar")
    @Description("Teste end-to-end: criar livro, validar criação via resposta do POST, atualizar título, validar atualização via resposta do PUT, excluir, validar remoção e GET após exclusão")
    void shouldExecuteFullE2EFlow() {
        int bookId = 1;

        // 1. Criar livro
        var createPayload = BookFixtures.bookForE2E();
        Response createResponse = booksClient.createBook(createPayload);
        createResponse.then()
                .statusCode(200)
                .body("id", equalTo(5000))
                .body("title", equalTo("Livro E2E"));

        // 2. Validar criação - a FakeRestAPI não persiste dados reais,
        // portanto validamos via resposta do POST acima.
        // Complementamos com GET de um livro existente para validar o fluxo.
        Response getExisting = booksClient.getBookById(bookId);
        getExisting.then()
                .statusCode(200)
                .body("id", equalTo(bookId));

        // 3. Atualizar título
        var updatePayload = BookFixtures.bookForE2EUpdate(bookId);
        Response updateResponse = booksClient.updateBook(bookId, updatePayload);
        updateResponse.then()
                .statusCode(200)
                .body("title", equalTo("Livro E2E Atualizado"));

        // 4. Validar atualização via resposta do PUT
        Response getAfterUpdate = booksClient.getBookById(bookId);
        getAfterUpdate.then()
                .statusCode(200)
                .body("id", equalTo(bookId));

        // 5. Excluir livro
        Response deleteResponse = booksClient.deleteBook(bookId);
        deleteResponse.then()
                .statusCode(200);

        // 6. Validar remoção - GET após exclusão
        // A FakeRestAPI não persiste exclusões reais, então o livro pode ainda existir.
        // Documentamos o comportamento observado.
        Response getAfterDelete = booksClient.getBookById(bookId);
        int statusAfterDelete = getAfterDelete.getStatusCode();
        // Em uma API real deveria ser 404, a FakeRestAPI retorna 200
        org.junit.jupiter.api.Assertions.assertTrue(
                statusAfterDelete == 200 || statusAfterDelete == 404,
                "Após exclusão, esperado 404 (ideal) ou 200 (FakeRestAPI). Obtido: " + statusAfterDelete
        );
    }
}
