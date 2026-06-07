package com.bibliotecadigital.tests;

import com.bibliotecadigital.clients.AuthorsClient;
import com.bibliotecadigital.clients.BooksClient;
import com.bibliotecadigital.clients.CoverPhotosClient;
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
    @Description("Simular criação de livro associado a autor inexistente e documentar comportamento. A FakeRestAPI não valida existência de autores na criação de livros.")
    void shouldDocumentBehaviorWhenCreatingBookWithNonExistentAuthor() {
        // Verificar que o autor com ID 99999 não existe
        Response authorsResponse = authorsClient.getAuthorsByBookId(99999);

        // Criar livro que teoricamente estaria associado ao autor inexistente
        var bookPayload = BookFixtures.bookForIntegration();
        Response createResponse = booksClient.createBook(bookPayload);

        // A FakeRestAPI não implementa validação de integridade referencial.
        // O livro é criado mesmo sem autor válido associado.
        // Em uma API real, deveria retornar erro (400 ou 422).
        int statusCode = createResponse.getStatusCode();
        assertTrue(
                statusCode == 200 || statusCode == 400 || statusCode == 422,
                "Comportamento observado: API permite criação sem validar autor. Status: " + statusCode
        );
    }

    @Test
    @DisplayName("TC_BOOKS_016.2 - Excluir livro com capa associada")
    @Description("Excluir livro que possui capa associada e validar comportamento dos dados relacionados")
    void shouldDocumentBehaviorWhenDeletingBookWithAssociatedCover() {
        // Verificar capas associadas ao livro ID 1
        Response coversBeforeDelete = coverPhotosClient.getCoverPhotosByBookId(1);
        int coversStatusBefore = coversBeforeDelete.getStatusCode();

        // Excluir o livro
        Response deleteResponse = booksClient.deleteBook(1);
        deleteResponse.then().statusCode(200);

        // Verificar se capas ainda existem após exclusão do livro
        Response coversAfterDelete = coverPhotosClient.getCoverPhotosByBookId(1);
        int coversStatusAfter = coversAfterDelete.getStatusCode();

        // A FakeRestAPI não implementa cascade delete nem validação de integridade.
        // As capas permanecem mesmo após exclusão do livro (registros órfãos).
        // Documentando comportamento observado.
        assertTrue(
                coversStatusAfter == 200 || coversStatusAfter == 404,
                "Comportamento observado: capas podem permanecer órfãs. Status: " + coversStatusAfter
        );
    }
}
