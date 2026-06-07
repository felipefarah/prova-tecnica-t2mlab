package com.bibliotecadigital.tests;

import com.bibliotecadigital.clients.BooksClient;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.*;

@Epic("Books API")
@Feature("GET /Books/{id}")
public class GetBookByIdTest {

    private BooksClient booksClient;

    @BeforeEach
    void setUp() {
        booksClient = new BooksClient();
    }

    @Test
    @DisplayName("TC_BOOKS_004 - GET /Books/{id} - Buscar livro existente")
    @Description("Validar busca de livro existente retorna status 200 e dados corretos")
    void shouldGetExistingBook() {
        Response response = booksClient.getBookById(1);

        response.then()
                .statusCode(200)
                .body("id", equalTo(1))
                .body("title", is(notNullValue()))
                .body("description", is(notNullValue()))
                .body("pageCount", is(notNullValue()))
                .body("publishDate", is(notNullValue()));
    }

    @Test
    @DisplayName("TC_BOOKS_005 - GET /Books/{id} - Buscar livro inexistente")
    @Description("Validar que ID inexistente retorna 404")
    void shouldReturnNotFoundForInvalidId() {
        Response response = booksClient.getBookById(999999);

        response.then()
                .statusCode(404);
    }

    @Test
    @DisplayName("TC_BOOKS_006 - GET /Books/{id} - Simular tentativa de acesso indevido (IDOR)")
    @Description("Simular tentativa de IDOR manipulando IDs. A API pública não implementa controle de acesso, portanto qualquer ID válido retorna dados. Documentando comportamento observado.")
    void shouldDocumentIdorBehavior() {
        Response responseBook1 = booksClient.getBookById(1);
        Response responseBook2 = booksClient.getBookById(2);

        responseBook1.then()
                .statusCode(200)
                .body("id", equalTo(1));

        responseBook2.then()
                .statusCode(200)
                .body("id", equalTo(2));

        // Documentação: A API não implementa controle de acesso por usuário.
        // Qualquer requisição pode acessar qualquer recurso pelo ID.
        // Em um sistema real, isso seria uma vulnerabilidade IDOR.
    }
}
