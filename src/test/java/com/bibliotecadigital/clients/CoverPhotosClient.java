package com.bibliotecadigital.clients;

import com.bibliotecadigital.utils.Environment;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class CoverPhotosClient {

    private static final String COVERS_BY_BOOK_ENDPOINT = "/CoverPhotos/books/covers/{idBook}";

    public Response getCoverPhotosByBookId(int idBook) {
        return given()
                .filter(new AllureRestAssured())
                .baseUri(Environment.getBaseUrl())
                .contentType(ContentType.JSON)
                .pathParam("idBook", idBook)
                .when()
                .get(COVERS_BY_BOOK_ENDPOINT);
    }
}
