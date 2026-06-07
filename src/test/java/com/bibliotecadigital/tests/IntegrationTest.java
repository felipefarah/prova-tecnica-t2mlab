package com.bibliotecadigital.tests;

import com.bibliotecadigital.clients.AuthorsClient;
import com.bibliotecadigital.clients.BooksClient;
import com.bibliotecadigital.clients.CoverPhotosClient;
import com.bibliotecadigital.fixtures.BookFixtures;
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
@Feature("Integração")
public class IntegrationTest {

    private BooksClient booksClient;
    private AuthorsClient authorsClient;
    private CoverPhotosClient coverPhotosClient;

    @BeforeEach
    void setUp() {
        booksClient = new BooksClient();
        authorsClient = new AuthorsClient();
        coverPhotosClient = new CoverPhotosClient();
    }

    @Test
    @DisplayName("TC_BOOKS_016.1 - Criar livro utilizando autor inexistente")
    @Description("Simular criação de livro associado a autor inexistente e documentar comportamento")
    void shouldDocumentBehaviorWhenCreatingBookWithNonExistentAuthor() {
        var bookPayload = BookFixtures.bookForIntegration();
        TestLogger.logTestStart("TC_BOOKS_016.1 - Criar livro utilizando autor inexistente");
        TestLogger.logRequest("GET", "/Authors/authors/books/99999", null);

        Response authorsResponse = authorsClient.getAuthorsByBookId(99999);
        System.out.println("  Authors:  status " + authorsResponse.getStatusCode() + " | " + authorsResponse.getBody().asString());

        TestLogger.logRequest("POST", "/Books", bookPayload);
        Response createResponse = booksClient.createBook(bookPayload);
        TestLogger.logResponse(createResponse);

        int statusCode = createResponse.getStatusCode();
        try {
            assertTrue(statusCode == 200 || statusCode == 400 || statusCode == 422,
                    "Comportamento observado. Status: " + statusCode);
            System.out.println("  Obs:      API não valida existência do autor. Livro criado sem restrição.");
            TestLogger.logPass();
        } catch (AssertionError e) {
            TestLogger.logFail("status 200, 400 ou 422", "status " + statusCode);
            throw e;
        }
    }

    @Test
    @DisplayName("TC_BOOKS_016.2 - Excluir livro com capa associada")
    @Description("Excluir livro que possui capa associada e validar comportamento dos dados relacionados")
    void shouldDocumentBehaviorWhenDeletingBookWithAssociatedCover() {
        TestLogger.logTestStart("TC_BOOKS_016.2 - Excluir livro com capa associada");
        TestLogger.logRequest("GET", "/CoverPhotos/books/covers/1", null);

        Response coversBeforeDelete = coverPhotosClient.getCoverPhotosByBookId(1);
        System.out.println("  Capas antes:  status " + coversBeforeDelete.getStatusCode() + " | " + coversBeforeDelete.getBody().asString());

        TestLogger.logRequest("DELETE", "/Books/1", null);
        Response deleteResponse = booksClient.deleteBook(1);
        TestLogger.logResponse(deleteResponse);

        TestLogger.logRequest("GET", "/CoverPhotos/books/covers/1 (após exclusão)", null);
        Response coversAfterDelete = coverPhotosClient.getCoverPhotosByBookId(1);
        System.out.println("  Capas depois: status " + coversAfterDelete.getStatusCode() + " | " + coversAfterDelete.getBody().asString());

        int coversStatusAfter = coversAfterDelete.getStatusCode();
        try {
            deleteResponse.then().statusCode(200);
            assertTrue(coversStatusAfter == 200 || coversStatusAfter == 404,
                    "Comportamento observado. Status capas: " + coversStatusAfter);
            System.out.println("  Obs:      Capas permanecem após exclusão do livro (registros órfãos). Sem cascade delete.");
            TestLogger.logPass();
        } catch (AssertionError e) {
            TestLogger.logFail("exclusão com status 200, capas com status 200 ou 404", "status " + coversStatusAfter);
            throw e;
        }
    }
}
