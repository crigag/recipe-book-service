package it.neapolispace.recipe;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class IngredientResourceTest {

    @io.quarkus.test.security.TestSecurity(user = "tester", roles = {"admin"})
    @Test
    void crudFlow() {
        String id =
            given()
                .contentType(ContentType.JSON)
                .body("{\"name\":\"Butter\",\"type\":\"DAIRY\",\"caloriesPer100g\":717}")
            .when()
                .post("/api/ingredients")
            .then()
                .statusCode(201)
                .body("name", equalTo("Butter"))
                .extract().path("id");

        given().when().get("/api/ingredients/" + id)
            .then().statusCode(200).body("type", equalTo("DAIRY"));

        given()
            .contentType(ContentType.JSON)
            .body("{\"name\":\"Unsalted Butter\",\"type\":\"DAIRY\",\"caloriesPer100g\":717}")
        .when()
            .put("/api/ingredients/" + id)
        .then()
            .statusCode(200)
            .body("name", equalTo("Unsalted Butter"));

        given().when().delete("/api/ingredients/" + id)
            .then().statusCode(204);
    }
}
