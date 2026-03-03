package core;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class RestClient {

    public static Response get(String endpoint) {
        return given()
                .log().all()
                .when()
                .get(endpoint)
                .then()
                .log().all()
                .extract()
                .response();

    }

}