package it.neapolispace.recipe.repository;

import io.quarkus.mongodb.panache.PanacheMongoRepository;
import it.neapolispace.recipe.model.Ingredient;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class IngredientRepository implements PanacheMongoRepository<Ingredient> {
}
