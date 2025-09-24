package it.neapolispace.recipe.model;

import io.quarkus.mongodb.panache.common.MongoEntity;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.bson.types.ObjectId;

@MongoEntity(collection = "ingredients")
public class Ingredient {

    public ObjectId id;

    @NotBlank
    public String name;

    @NotNull
    public IngredientType type;

    @NotNull
    @Min(0)
    public Double caloriesPer100g;
}
