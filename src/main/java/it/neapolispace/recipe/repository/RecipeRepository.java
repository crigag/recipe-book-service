package it.neapolispace.recipe.repository;

import io.quarkus.mongodb.panache.PanacheMongoRepository;
import it.neapolispace.recipe.model.Recipe;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RecipeRepository implements PanacheMongoRepository<Recipe> {
}
