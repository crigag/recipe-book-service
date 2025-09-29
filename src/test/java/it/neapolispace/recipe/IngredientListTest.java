package it.neapolispace.recipe;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class IngredientListTest {

    @Test
    void listShouldReturnSeededIngredientsWithValues() {
        given()
        .when()
            .get("/api/ingredients")
        .then()
            .statusCode(200)
            .body("size()", greaterThanOrEqualTo(2))
            .body("name", hasItems("Sugar", "Flour"))
            .body("find { it.name == 'Sugar' }.caloriesPer100g", notNullValue())
            .body("find { it.name == 'Sugar' }.type", equalTo("SWEETENER"))
            .body("find { it.name == 'Flour' }.caloriesPer100g", notNullValue())
            .body("find { it.name == 'Flour' }.type", equalTo("GRAIN"));
    }
}
