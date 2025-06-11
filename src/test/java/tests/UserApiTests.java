package tests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import io.restassured.response.Response;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import static org.hamcrest.MatcherAssert.assertThat;

public class UserApiTests {

    static {
        RestAssured.baseURI = "https://jsonplaceholder.typicode.com";
    }

    @Test
    public void getUser_shouldReturn200_andCorrectUserData() {
        given()
                .when()
                .get("/users/1")
                .then()
                .assertThat()
                .statusCode(200)
                .body("username", equalTo("Bret"));
    }

    @Test
    public void createPost_shouldReturn201() {
        given()
                .contentType(ContentType.JSON)
                .body("{ \"title\": \"foo\", \"body\": \"bar\", \"userId\": 1 }")
                .when()
                .post("/posts")
                .then()
                .assertThat()
                .statusCode(201)
                .body("title", equalTo("foo"))
                .body("userId", equalTo(1));
    }

    @Test
    public void confirmContentType() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/users/1")
                .then()
                .assertThat()
                .contentType(ContentType.JSON);
    }

    @Test
    public void logAllRequestedUserData() {
        given()
                .log().all()
                .when()
                .get("/users/1")
                .then()
                .log().body();
    }

    @Test
    public void requestUsers_confirmFirstUserCity() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/users")
                .then()
                .assertThat()
                .body("[0].address.city", equalTo("Gwenborough"));
    }

    @Test
    public void checkUserExists() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/users")
                .then()
                .assertThat()
                .body("username", hasItem("Bret"));
    }

    @Test
    public void checkAmountOfUsers() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/users")
                .then()
                .assertThat()
                .body("size()", equalTo(10));
    }

    @ParameterizedTest
    @CsvSource({
            "Bret, Sincere@april.biz",
            "Antonette, Shanna@melissa.tv",
            "Samantha, Nathan@yesenia.net"
    })
    public void getUserByUsername_shouldReturnCorrectEmail(String username, String expectedEmail) {
        // Get all users and filter the one with matching username
        Response response = given()
                .when()
                .get("/users")
                .then()
                .statusCode(200)
                .extract()
                .response();

        // Find the first user with matching username
        String actualEmail = response.jsonPath()
                .getString("find { it.username == '" + username + "' }.email");

        assertThat(actualEmail, equalTo(expectedEmail));
    }
}
