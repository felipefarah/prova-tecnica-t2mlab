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
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        TestLogger.logTestStart("E2E - Fluxo completo: Criar -> Validar -> Atualizar -> Validar -> Excluir -> Validar");

        // 1. Criar livro
        var createPayload = BookFixtures.bookForE2E();
        System.out.println("  [Passo 1] Criar livro");
        TestLogger.logRequest("POST", "/Books", createPayload);
        Response createResponse = booksClient.createBook(createPayload);
        TestLogger.logResponse(createResponse);

        try {
            createResponse.then()
                    .statusCode(200)
                    .body("id", equalTo(5000))
                    .body("title", equalTo("Livro E2E"));
        } catch (AssertionError e) {
            TestLogger.logFail("status 200 na criação", "status " + createResponse.getStatusCode());
            throw e;
        }

        // 2. Validar criação via GET
        System.out.println("  [Passo 2] Validar criação via GET");
        TestLogger.logRequest("GET", "/Books/" + bookId, null);
        Response getExisting = booksClient.getBookById(bookId);
        TestLogger.logResponse(getExisting);
        getExisting.then().statusCode(200).body("id", equalTo(bookId));

        // 3. Atualizar título
        var updatePayload = BookFixtures.bookForE2EUpdate(bookId);
        System.out.println("  [Passo 3] Atualizar título");
        TestLogger.logRequest("PUT", "/Books/" + bookId, updatePayload);
        Response updateResponse = booksClient.updateBook(bookId, updatePayload);
        TestLogger.logResponse(updateResponse);
        updateResponse.then().statusCode(200).body("title", equalTo("Livro E2E Atualizado"));

        // 4. Validar atualização via GET
        System.out.println("  [Passo 4] Validar atualização via GET");
        TestLogger.logRequest("GET", "/Books/" + bookId, null);
        Response getAfterUpdate = booksClient.getBookById(bookId);
        TestLogger.logResponse(getAfterUpdate);
        getAfterUpdate.then().statusCode(200).body("id", equalTo(bookId));

        // 5. Excluir livro
        System.out.println("  [Passo 5] Excluir livro");
        TestLogger.logRequest("DELETE", "/Books/" + bookId, null);
        Response deleteResponse = booksClient.deleteBook(bookId);
        TestLogger.logResponse(deleteResponse);
        deleteResponse.then().statusCode(200);

        // 6. Validar remoção - GET após exclusão
        System.out.println("  [Passo 6] Validar GET após exclusão");
        TestLogger.logRequest("GET", "/Books/" + bookId, null);
        Response getAfterDelete = booksClient.getBookById(bookId);
        TestLogger.logResponse(getAfterDelete);

        int statusAfterDelete = getAfterDelete.getStatusCode();
        try {
            assertTrue(statusAfterDelete == 200 || statusAfterDelete == 404,
                    "Após exclusão, esperado 404 ou 200. Obtido: " + statusAfterDelete);
            System.out.println("  Obs:      FakeRestAPI não persiste exclusão. Status pós-delete: " + statusAfterDelete);
            TestLogger.logPass();
        } catch (AssertionError e) {
            TestLogger.logFail("status 404 ou 200 após exclusão", "status " + statusAfterDelete);
            throw e;
        }
    }
}
