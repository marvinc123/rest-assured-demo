package tests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class UserApiTests {

    static {
        RestAssured.baseURI = "https://jsonplaceholder.typicode.com";
    }

    @Test
    void getUser_shouldReturn200_andCorrectUserData() {
        given()
        .when()
            .get("/users/1")
        .then()
            .statusCode(200)
            .body("username", equalTo("Bret"));
    }

    @Test
    void createPost_shouldReturn201() {
        given()
            .contentType(ContentType.JSON)
            .body("{ \"title\": \"foo\", \"body\": \"bar\", \"userId\": 1 }")
        .when()
            .post("/posts")
        .then()
            .statusCode(201)
            .body("title", equalTo("foo"))
            .body("userId", equalTo(1));
    }
}
