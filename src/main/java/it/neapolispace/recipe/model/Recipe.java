package it.neapolispace.recipe.model;

import io.quarkus.mongodb.panache.common.MongoEntity;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.bson.types.ObjectId;

import java.util.List;

@MongoEntity(collection = "recipes")
public class Recipe {
    public ObjectId id;

    @NotBlank
    public String name;

    public String description;

    @NotNull
    public RecipeCategory category;

    @NotNull
    @Min(1)
    public Integer servings;

    @NotEmpty
    public List<RecipeIngredient> ingredients;

    public List<String> steps;

    public static class RecipeIngredient {
        @NotNull
        public ObjectId ingredientId;
        @NotNull
        @Min(0)
        public Double amountGrams;
    }
}
