package com.bibliotecadigital.clients;

import com.bibliotecadigital.utils.Environment;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class AuthorsClient {

    private static final String AUTHORS_BY_BOOK_ENDPOINT = "/Authors/authors/books/{idBook}";

    public Response getAuthorsByBookId(int idBook) {
        return given()
                .filter(new AllureRestAssured())
                .baseUri(Environment.getBaseUrl())
                .contentType(ContentType.JSON)
                .pathParam("idBook", idBook)
                .when()
                .get(AUTHORS_BY_BOOK_ENDPOINT);
    }
}
