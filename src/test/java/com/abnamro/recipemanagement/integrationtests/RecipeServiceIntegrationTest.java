package com.abnamro.recipemanagement.integrationtests;

import com.abnamro.recipemanagement.Entity.Recipe;
import com.abnamro.recipemanagement.domain.RecipeFilterRequest;
import com.abnamro.recipemanagement.domain.RecipeRequest;
import com.abnamro.recipemanagement.exception.RecipeNotFoundException;
import com.abnamro.recipemanagement.repository.RecipeRepository;
import com.abnamro.recipemanagement.service.RecipeService;
import com.abnamro.recipemanagement.service.RecipeServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
public class RecipeServiceIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private RecipeRepository recipeRepository;

    private RecipeService recipeService;

    @BeforeEach
    public void setUp() {
        recipeService = new RecipeServiceImpl(recipeRepository);
    }

    @AfterEach
    public void tearDown() {
        recipeRepository.deleteAll();
    }

    @Test
    void testGetRecipeById() {
        Recipe recipe = new Recipe();
        recipe.setName("Pasta");
        recipe.setIngredients(Arrays.asList("Pasta", "Tomatoes"));
        recipe.setIsVegetarian(true);
        recipe.setServings(4);
        recipe.setInstructions("Instructions");

        entityManager.persist(recipe);
        entityManager.flush();

        // Act
        Recipe result = recipeService.getRecipeById(recipe.getId());

        // Assert
        assertEquals(recipe.getId(), result.getId());
        assertEquals("Pasta", result.getName());
        assertTrue(result.getIsVegetarian());
        assertEquals(4, result.getServings());
        assertEquals(Arrays.asList("Pasta", "Tomatoes"), result.getIngredients());
        assertEquals("Instructions", result.getInstructions());
    }

    @Test
    public void testGetRecipeById_RecipeNotFound() {
        // Arrange
        long recipeId = 1L;

        // Act & Assert
        assertThrows(RecipeNotFoundException.class, () -> recipeService.getRecipeById(recipeId));

    }

    @Test
    void testFilterRecipes() {
        // Arrange
        Recipe recipe1 = new Recipe();
        recipe1.setName("Pasta Recipe");
        recipe1.setIngredients(Arrays.asList("Pasta", "Tomatoes"));
        recipe1.setIsVegetarian(true);
        recipe1.setServings(4);
        recipe1.setInstructions("Instructions");
        Recipe recipe2 = new Recipe();
        recipe2.setName("Veggie Burger");
        recipe2.setIngredients(Arrays.asList("Burger Bun", "Lettuce", "Tomatoes"));
        recipe2.setIsVegetarian(true);
        recipe2.setServings(2);
        recipe2.setInstructions("Instructions");
        entityManager.persist(recipe1);
        entityManager.persist(recipe2);
        entityManager.flush();

        RecipeFilterRequest filterRequest = new RecipeFilterRequest();
        filterRequest.setIsVegetarian(true);
        filterRequest.setServings(4);
        filterRequest.setIncludeIngredients(Arrays.asList("Pasta", "Tomatoes"));
        filterRequest.setExcludeIngredients(List.of("Salmon"));
        filterRequest.setSearchText("Instructions");

        // Act
        List<Recipe> result = recipeService.searchRecipes(filterRequest);

        // Assert
        assertEquals(1, result.size());
        assertEquals(recipe1.getId(), result.get(0).getId());
        assertEquals("Pasta Recipe", result.get(0).getName());
    }

    @Test
    void testAddRecipe() {
        // Arrange
        RecipeRequest recipeRequest = new RecipeRequest();
        recipeRequest.setName("Pasta");
        recipeRequest.setVegetarian(true);
        recipeRequest.setServings(4);
        recipeRequest.setIngredients(Arrays.asList("Pasta", "Tomatoes"));
        recipeRequest.setInstructions("Instructions");

        // Act
        Recipe result = recipeService.addRecipe(recipeRequest);

        // Assert
        assertNotNull(result.getId());
        assertEquals("Pasta", result.getName());
        assertTrue(result.getIsVegetarian());
        assertEquals(4, result.getServings());
        assertEquals(Arrays.asList("Pasta", "Tomatoes"), result.getIngredients());
        assertEquals("Instructions", result.getInstructions());
    }

   @Test
    void testUpdateRecipe() {
        // Arrange
        Recipe recipe = new Recipe();
        recipe.setName("Pasta");
        recipe.setIngredients(Arrays.asList("Pasta", "Tomatoes"));
        recipe.setIsVegetarian(true);
        recipe.setServings(3);
        recipe.setInstructions("Instructions");
        entityManager.persist(recipe);
        entityManager.flush();

        RecipeRequest recipeRequest = new RecipeRequest();
        recipeRequest.setName("Updated Pasta");
        recipeRequest.setVegetarian(false);
        recipeRequest.setServings(3);
        recipeRequest.setIngredients(new ArrayList<>(Arrays.asList("Pasta", "Tomatoes", "Cheese")));
        recipeRequest.setInstructions("Updated Instructions");

        // Act
        Recipe result = recipeService.updateRecipe(recipe.getId(), recipeRequest);

        // Assert
        assertEquals(recipe.getId(), result.getId());
        assertEquals("Updated Pasta", result.getName());
        assertFalse(result.getIsVegetarian());
        assertEquals(3, result.getServings());
        assertEquals(Arrays.asList("Pasta", "Tomatoes", "Cheese"), result.getIngredients());
        assertEquals("Updated Instructions", result.getInstructions());
    }

    @Test
    void testUpdateRecipe_RecipeNotFound() {
        // Arrange
        long nonExistingRecipeId = 100L;
        RecipeRequest recipeRequest = new RecipeRequest();

        // Act & Assert
        assertThrows(RecipeNotFoundException.class, () -> recipeService.updateRecipe(nonExistingRecipeId, recipeRequest));
    }

    @Test
    void testRemoveRecipe() {
        // Arrange
        Recipe recipe = new Recipe();
        recipe.setName("Pasta");
        recipe.setIngredients(Arrays.asList("Pasta", "Tomatoes"));
        recipe.setIsVegetarian(true);
        recipe.setServings(10);
        recipe.setInstructions("Instructions");
        entityManager.persist(recipe);
        entityManager.flush();

        // Act
        recipeService.removeRecipe(recipe.getId());

        // Assert
        assertFalse(recipeRepository.existsById(recipe.getId()));
    }

    @Test
    void testRemoveRecipe_RecipeNotFound() {
        // Arrange
        long nonExistingRecipeId = 100L;

        // Act & Assert
        assertThrows(RecipeNotFoundException.class, () -> recipeService.removeRecipe(nonExistingRecipeId));
    }

    @Test
    public void testGetAllRecipes() {
        // Arrange
        Recipe recipe1 = new Recipe();
        recipe1.setName("Pasta");
        recipe1.setIngredients(Arrays.asList("Pasta", "Tomatoes"));
        recipe1.setIsVegetarian(true);
        recipe1.setServings(4);
        recipe1.setInstructions("Instructions");
        Recipe recipe2 = new Recipe();
        recipe2.setName("Veggie Burger");
        recipe2.setIngredients(Arrays.asList("Burger Bun", "Lettuce", "Tomatoes"));
        recipe2.setIsVegetarian(true);
        recipe2.setServings(2);
        recipe2.setInstructions("Instructions");
        entityManager.persist(recipe1);
        entityManager.persist(recipe2);
        entityManager.flush();

        // Act
        List<Recipe> recipes = recipeService.getAllRecipes();

        // Assert
        assertEquals(2, recipes.size());
        assertEquals("Pasta", recipes.get(0).getName());
        assertEquals("Veggie Burger", recipes.get(1).getName());
    }

    @Test
    public void testGetAllRecipes_NoRecipes() {
        // Act & Assert
        assertThrows(RecipeNotFoundException.class, () -> recipeService.getAllRecipes());

    }
    @Test
    public void testUpdateRecipe_NullRequest() {
        // Act & Assert
        assertThrows(RecipeNotFoundException.class, () -> recipeService.updateRecipe(1L, null));
    }

}
