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
        TestLogger.logTestStart("TC_BOOKS_004 - GET /Books/{id} - Buscar livro existente");
        TestLogger.logRequest("GET", "/Books/1", null);

        Response response = booksClient.getBookById(1);
        TestLogger.logResponse(response);

        try {
            response.then()
                    .statusCode(200)
                    .body("id", equalTo(1))
                    .body("title", is(notNullValue()))
                    .body("description", is(notNullValue()))
                    .body("pageCount", is(notNullValue()))
                    .body("publishDate", is(notNullValue()));
            TestLogger.logPass();
        } catch (AssertionError e) {
            TestLogger.logFail("status 200 com dados do livro", "status " + response.getStatusCode());
            throw e;
        }
    }

    @Test
    @DisplayName("TC_BOOKS_005 - GET /Books/{id} - Buscar livro inexistente")
    @Description("Validar que ID inexistente retorna 404")
    void shouldReturnNotFoundForInvalidId() {
        TestLogger.logTestStart("TC_BOOKS_005 - GET /Books/{id} - Buscar livro inexistente");
        TestLogger.logRequest("GET", "/Books/999999", null);

        Response response = booksClient.getBookById(999999);
        TestLogger.logResponse(response);

        try {
            response.then().statusCode(404);
            TestLogger.logPass();
        } catch (AssertionError e) {
            TestLogger.logFail("status 404", "status " + response.getStatusCode());
            throw e;
        }
    }

    @Test
    @DisplayName("TC_BOOKS_006 - GET /Books/{id} - Simular tentativa de acesso indevido (IDOR)")
    @Description("Simular tentativa de IDOR manipulando IDs. A API pública não implementa controle de acesso, portanto qualquer ID válido retorna dados. Documentando comportamento observado.")
    void shouldDocumentIdorBehavior() {
        TestLogger.logTestStart("TC_BOOKS_006 - GET /Books/{id} - Simular tentativa de acesso indevido (IDOR)");
        TestLogger.logRequest("GET", "/Books/1 e /Books/2", null);

        Response responseBook1 = booksClient.getBookById(1);
        Response responseBook2 = booksClient.getBookById(2);

        System.out.println("  Response [ID=1]: status " + responseBook1.getStatusCode());
        System.out.println("  Response [ID=2]: status " + responseBook2.getStatusCode());

        try {
            responseBook1.then().statusCode(200).body("id", equalTo(1));
            responseBook2.then().statusCode(200).body("id", equalTo(2));
            System.out.println("  Obs:      API não implementa controle de acesso por usuário (IDOR possível)");
            TestLogger.logPass();
        } catch (AssertionError e) {
            TestLogger.logFail("acesso a ambos IDs com status 200", "falha no acesso");
            throw e;
        }
    }
}
