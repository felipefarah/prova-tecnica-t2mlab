package com.bibliotecadigital.clients;

import com.bibliotecadigital.utils.Environment;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class BooksClient {

    private static final String BOOKS_ENDPOINT = "/Books";
    private static final String BOOKS_BY_ID_ENDPOINT = "/Books/{id}";

    public Response getAllBooks() {
        return given()
                .filter(new AllureRestAssured())
                .baseUri(Environment.getBaseUrl())
                .contentType(ContentType.JSON)
                .when()
                .get(BOOKS_ENDPOINT);
    }

    public Response getBookById(int id) {
        return given()
                .filter(new AllureRestAssured())
                .baseUri(Environment.getBaseUrl())
                .contentType(ContentType.JSON)
                .pathParam("id", id)
                .when()
                .get(BOOKS_BY_ID_ENDPOINT);
    }

    public Response createBook(Object bookPayload) {
        return given()
                .filter(new AllureRestAssured())
                .baseUri(Environment.getBaseUrl())
                .contentType(ContentType.JSON)
                .body(bookPayload)
                .when()
                .post(BOOKS_ENDPOINT);
    }

    public Response updateBook(int id, Object bookPayload) {
        return given()
                .filter(new AllureRestAssured())
                .baseUri(Environment.getBaseUrl())
                .contentType(ContentType.JSON)
                .pathParam("id", id)
                .body(bookPayload)
                .when()
                .put(BOOKS_BY_ID_ENDPOINT);
    }

    public Response deleteBook(int id) {
        return given()
                .filter(new AllureRestAssured())
                .baseUri(Environment.getBaseUrl())
                .contentType(ContentType.JSON)
                .pathParam("id", id)
                .when()
                .delete(BOOKS_BY_ID_ENDPOINT);
    }
}
