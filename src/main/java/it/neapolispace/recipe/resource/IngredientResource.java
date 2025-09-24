package it.neapolispace.recipe.resource;

import it.neapolispace.recipe.model.Ingredient;
import it.neapolispace.recipe.repository.IngredientRepository;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.bson.types.ObjectId;

import java.net.URI;
import java.util.List;

@Path("/api/ingredients")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class IngredientResource {

    @Inject
    IngredientRepository repository;

    @GET
    public List<Ingredient> list() {
        return repository.listAll();
    }

    @GET
    @Path("/{id}")
    public Ingredient get(@PathParam("id") String id) {
        Ingredient ing = repository.findById(new ObjectId(id));
        if (ing == null) throw new NotFoundException("Ingredient not found");
        return ing;
    }

    @POST
    @RolesAllowed("admin")
    public Response create(@Valid Ingredient ingredient) {
        ingredient.id = null;
        repository.persist(ingredient);
        return Response.created(URI.create("/api/ingredients/" + ingredient.id.toHexString())).entity(ingredient).build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed("admin")
    public Ingredient update(@PathParam("id") String id, @Valid Ingredient ingredient) {
        Ingredient existing = repository.findById(new ObjectId(id));
        if (existing == null) throw new NotFoundException("Ingredient not found");
        existing.name = ingredient.name;
        existing.type = ingredient.type;
        existing.caloriesPer100g = ingredient.caloriesPer100g;
        repository.update(existing);
        return existing;
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("admin")
    public Response delete(@PathParam("id") String id) {
        boolean deleted = repository.deleteById(new ObjectId(id));
        if (!deleted) throw new NotFoundException("Ingredient not found");
        return Response.noContent().build();
    }
}
