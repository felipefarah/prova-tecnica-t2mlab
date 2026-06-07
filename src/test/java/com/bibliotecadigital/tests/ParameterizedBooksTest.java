package com.bibliotecadigital.tests;

import com.bibliotecadigital.clients.BooksClient;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.hamcrest.Matchers.*;

@Epic("Books API")
@Feature("Teste Parametrizado")
public class ParameterizedBooksTest {

    private BooksClient booksClient;

    @BeforeEach
    void setUp() {
        booksClient = new BooksClient();
    }

    @ParameterizedTest(name = "GET /Books/{0} - Buscar livro com ID {0}")
    @ValueSource(ints = {1, 2, 3, 4, 5})
    @DisplayName("Parametrizado - GET /Books/{id}")
    @Description("Executar GET /Books/{id} para múltiplos IDs válidos e validar retorno")
    void shouldGetBookByMultipleIds(int bookId) {
        Response response = booksClient.getBookById(bookId);

        response.then()
                .statusCode(200)
                .body("id", equalTo(bookId))
                .body("title", is(notNullValue()))
                .body("pageCount", is(notNullValue()))
                .body("publishDate", is(notNullValue()));
    }
}
