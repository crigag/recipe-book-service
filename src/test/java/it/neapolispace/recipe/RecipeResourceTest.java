package it.neapolispace.recipe;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class RecipeResourceTest {

    @io.quarkus.test.security.TestSecurity(user = "tester", roles = {"admin"})
    @Test
    void crudFlow() {
        // Create an ingredient to use in recipe
        String ingId =
            given()
                .contentType(ContentType.JSON)
                .body("{\"name\":\"Egg\",\"type\":\"OTHER\",\"caloriesPer100g\":155}")
            .when()
                .post("/api/ingredients")
            .then()
                .statusCode(201)
                .extract().path("id");

        String recipeBody = "{\"name\":\"Scrambled Eggs\",\"description\":\"Simple eggs\",\"category\":\"BREAKFAST\",\"servings\":2,\"ingredients\":[{\"ingredientId\":\"" + ingId + "\",\"amountGrams\":100}],\"steps\":[\"Beat eggs\",\"Cook\"]}";

        String recipeId =
            given()
                .contentType(ContentType.JSON)
                .body(recipeBody)
            .when()
                .post("/api/recipes")
            .then()
                .statusCode(201)
                .body("name", equalTo("Scrambled Eggs"))
                .extract().path("id");

        given().when().get("/api/recipes/" + recipeId)
            .then().statusCode(200).body("category", equalTo("BREAKFAST"));

        String updated = "{\"name\":\"Best Scrambled Eggs\",\"description\":\"Fluffy\",\"category\":\"BREAKFAST\",\"servings\":3,\"ingredients\":[{\"ingredientId\":\"" + ingId + "\",\"amountGrams\":120}],\"steps\":[\"Beat\",\"Cook\"]}";

        given()
            .contentType(ContentType.JSON)
            .body(updated)
        .when()
            .put("/api/recipes/" + recipeId)
        .then()
            .statusCode(200)
            .body("servings", equalTo(3));

        given().when().delete("/api/recipes/" + recipeId)
            .then().statusCode(204);
    }
}
