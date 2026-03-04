package com.smhi.tempmap.resource;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
class TemperatureResourceTest {

    @Test
    void testStationsEndpoint() {
        given()
            .when().get("/api/stations")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON);
    }

    @Test
    void testCurrentTemperaturesEndpoint() {
        given()
            .when().get("/api/temperatures/current")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON);
    }

    @Test
    void testHistoryEndpointWithInvalidId() {
        given()
            .when().get("/api/temperatures/99999/history")
            .then()
            .statusCode(200);
    }
}
