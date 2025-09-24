package it.neapolispace.recipe.resource;

import it.neapolispace.recipe.model.Recipe;
import it.neapolispace.recipe.model.RecipeCategory;
import it.neapolispace.recipe.repository.RecipeRepository;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.bson.types.ObjectId;

import java.net.URI;
import java.util.List;

@Path("/api/recipes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RecipeResource {

    @Inject
    RecipeRepository repository;

    @GET
    public List<Recipe> list(@QueryParam("category") RecipeCategory category,
                             @QueryParam("name") String name) {
        if (category != null) {
            return repository.list("category", category);
        }
        if (name != null && !name.isBlank()) {
            return repository.list("name", name);
        }
        return repository.listAll();
    }

    @GET
    @Path("/{id}")
    public Recipe get(@PathParam("id") String id) {
        Recipe recipe = repository.findById(new ObjectId(id));
        if (recipe == null) throw new NotFoundException("Recipe not found");
        return recipe;
    }

    @POST
    @RolesAllowed("admin")
    public Response create(@Valid Recipe recipe) {
        recipe.id = null;
        repository.persist(recipe);
        return Response.created(URI.create("/api/recipes/" + recipe.id.toHexString())).entity(recipe).build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed("admin")
    public Recipe update(@PathParam("id") String id, @Valid Recipe recipe) {
        Recipe existing = repository.findById(new ObjectId(id));
        if (existing == null) throw new NotFoundException("Recipe not found");
        existing.name = recipe.name;
        existing.description = recipe.description;
        existing.category = recipe.category;
        existing.servings = recipe.servings;
        existing.ingredients = recipe.ingredients;
        existing.steps = recipe.steps;
        repository.update(existing);
        return existing;
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("admin")
    public Response delete(@PathParam("id") String id) {
        boolean deleted = repository.deleteById(new ObjectId(id));
        if (!deleted) throw new NotFoundException("Recipe not found");
        return Response.noContent().build();
    }
}
