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

    Recipe recipe1;
    Recipe recipe2;
    Recipe recipe3;

    @BeforeEach
    public void setUp() {
        recipe1 = new Recipe();
        recipe1.setName("Pasta");
        recipe1.setIsVegetarian(true);
        recipe1.setInstructions("stove");
        recipe1.setIngredients(List.of("Pasta", "Tomatoes"));
        recipe1.setServings(3);

        recipe2 = new Recipe();
        recipe2.setName("Veggie Burger");
        recipe2.setIsVegetarian(true);
        recipe2.setInstructions("oven");
        recipe2.setIngredients(List.of("Burger Bun", "Lettuce", "Tomatoes"));
        recipe2.setServings(3);

        recipe3 = new Recipe();
        recipe3.setName("Chicken Burger");
        recipe3.setIsVegetarian(false);
        recipe3.setInstructions("oven");
        recipe3.setIngredients(List.of("Burger Bun", "Chicken", "Tomatoes"));
        recipe3.setServings(6);

        recipeService = new RecipeServiceImpl(recipeRepository);
    }

    @AfterEach
    public void tearDown() {
        recipeRepository.deleteAll();
    }

    @Test
    void testGetRecipeById() {
        // Arrange
        entityManager.persist(recipe1);
        entityManager.flush();

        // Act
        Recipe result = recipeService.getRecipeById(recipe1.getId());

        // Assert
        assertEquals(recipe1.getId(), result.getId());
        assertEquals("Pasta", result.getName());
        assertTrue(result.getIsVegetarian());
        assertEquals(3, result.getServings());
        assertEquals(recipe1.getIngredients(), result.getIngredients());
        assertEquals(recipe1.getInstructions(), result.getInstructions());
    }

    @Test
    public void testGetRecipeById_RecipeNotFound() {
        // Arrange
        long recipeId = 1L;

        // Act & Assert
        assertThrows(RecipeNotFoundException.class, () -> recipeService.getRecipeById(recipeId));

    }

    @Test
    void testFilterVegetarianRecipes() {
        // Arrange
        entityManager.persist(recipe1);
        entityManager.persist(recipe2);
        entityManager.persist(recipe3);
        entityManager.flush();

        RecipeFilterRequest filterRequest = new RecipeFilterRequest();
        filterRequest.setIsVegetarian(true);
        // Act
        List<Recipe> result = recipeService.searchRecipes(filterRequest);
        // Assert
        assertEquals(2, result.size());
        assertEquals(recipe1.getId(), result.get(0).getId());
        assertEquals("Pasta", result.get(0).getName());
        assertEquals("Veggie Burger", result.get(1).getName());
    }

    @Test
    void testFilterVegetarianRecipesWithNumberOfServings() {
        // Arrange
        entityManager.persist(recipe1);
        entityManager.persist(recipe2);
        entityManager.persist(recipe3);
        entityManager.flush();

        RecipeFilterRequest filterRequest = new RecipeFilterRequest();
        filterRequest.setIsVegetarian(true);
        filterRequest.setServings(3);
        // Act
        List<Recipe> result = recipeService.searchRecipes(filterRequest);
        // Assert
        assertEquals(2, result.size());
        assertEquals(recipe1.getId(), result.get(0).getId());
        assertEquals("Pasta", result.get(0).getName());
        assertEquals("Veggie Burger", result.get(1).getName());
    }

    @Test
    void testFilterVegetarianRecipesWithNumberOfServingsAndInstructions() {
        // Arrange
        entityManager.persist(recipe1);
        entityManager.persist(recipe2);
        entityManager.persist(recipe3);
        entityManager.flush();

        RecipeFilterRequest filterRequest = new RecipeFilterRequest();
        filterRequest.setIsVegetarian(true);
        filterRequest.setServings(3);
        filterRequest.setSearchText("stove");
        // Act
        List<Recipe> result = recipeService.searchRecipes(filterRequest);
        // Assert
        assertEquals(1, result.size());
        assertEquals(recipe1.getId(), result.get(0).getId());
        assertEquals("Pasta", result.get(0).getName());
    }

    @Test
    void testFilterRecipesWithName() {
        // Arrange
        entityManager.persist(recipe1);
        entityManager.persist(recipe2);
        entityManager.persist(recipe3);
        entityManager.flush();

        RecipeFilterRequest filterRequest = new RecipeFilterRequest();
        filterRequest.setName("Burger");
        // Act
        List<Recipe> result = recipeService.searchRecipes(filterRequest);
        // Assert
        assertEquals(2, result.size());
        assertEquals("Veggie Burger", result.get(0).getName());
        assertEquals("Chicken Burger", result.get(1).getName());
    }

    @Test
    void testFilterVegetarianRecipesByExcludingName() {
        // Arrange
        entityManager.persist(recipe1);
        entityManager.persist(recipe2);
        entityManager.persist(recipe3);
        entityManager.flush();

        RecipeFilterRequest filterRequest = new RecipeFilterRequest();
        filterRequest.setIsVegetarian(true);
        filterRequest.setExcludeName("Burger");
        // Act
        List<Recipe> result = recipeService.searchRecipes(filterRequest);
        // Assert
        assertEquals(1, result.size());
        assertEquals("Pasta", result.get(0).getName());
    }

    @Test
    void testFilterRecipesWithIncludingAndExcludingIngredients() {
        // Arrange
        entityManager.persist(recipe1);
        entityManager.persist(recipe2);
        entityManager.persist(recipe3);
        entityManager.flush();

        RecipeFilterRequest filterRequest = new RecipeFilterRequest();
        filterRequest.setIncludeIngredients(List.of("Burger Bun", "Tomatoes"));
        filterRequest.setExcludeIngredients(List.of("Chicken"));
        // Act
        List<Recipe> result = recipeService.searchRecipes(filterRequest);
        // Assert
        assertEquals(1, result.size());
        assertEquals("Veggie Burger", result.get(0).getName());
    }

    @Test
    void testFilterRecipesWithIncludingAndExcludingIngredientsAndName() {
        // Arrange
        entityManager.persist(recipe1);
        entityManager.persist(recipe2);
        entityManager.persist(recipe3);
        entityManager.flush();

        RecipeFilterRequest filterRequest = new RecipeFilterRequest();
        filterRequest.setName("Burger");
        filterRequest.setIncludeIngredients(List.of("Burger Bun", "Tomatoes"));
        filterRequest.setExcludeIngredients(List.of("Chicken"));
        // Act
        List<Recipe> result = recipeService.searchRecipes(filterRequest);
        // Assert
        assertEquals(1, result.size());
        assertEquals("Veggie Burger", result.get(0).getName());
    }

    @Test
    void testFilterRecipes() {
        // Arrange
        entityManager.persist(recipe1);
        entityManager.persist(recipe2);
        entityManager.persist(recipe3);
        entityManager.flush();

        RecipeFilterRequest filterRequest = new RecipeFilterRequest();
        filterRequest.setIsVegetarian(true);
        filterRequest.setServings(3);
        filterRequest.setIncludeIngredients(Arrays.asList("Pasta", "Tomatoes"));
        filterRequest.setExcludeIngredients(List.of("Salmon"));
        filterRequest.setSearchText("stove");
        filterRequest.setName("Pasta");

        // Act
        List<Recipe> result = recipeService.searchRecipes(filterRequest);

        // Assert
        assertEquals(1, result.size());
        assertEquals(recipe1.getId(), result.get(0).getId());
        assertEquals("Pasta", result.get(0).getName());
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
        assertEquals(Arrays.asList("pasta", "tomatoes"), result.getIngredients());
        assertEquals("Instructions", result.getInstructions());
    }

   @Test
    void testUpdateRecipe() {
        // Arrange

        entityManager.persist(recipe2);
        entityManager.flush();

        RecipeRequest recipeRequest = new RecipeRequest();
        recipeRequest.setName("Updated Pasta");
        recipeRequest.setVegetarian(false);
        recipeRequest.setServings(3);
        recipeRequest.setIngredients(new ArrayList<>(Arrays.asList("Pasta", "Tomatoes", "Cheese")));
        recipeRequest.setInstructions("Updated Instructions");

        // Act
        Recipe result = recipeService.updateRecipe(recipe2.getId(), recipeRequest);

        // Assert
        assertEquals(recipe2.getId(), result.getId());
        assertEquals("Updated Pasta", result.getName());
        assertFalse(result.getIsVegetarian());
        assertEquals(3, result.getServings());
        assertEquals(List.of("pasta", "tomatoes", "cheese"), result.getIngredients());
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
        entityManager.persist(recipe3);
        entityManager.flush();

        // Act
        recipeService.removeRecipe(recipe3.getId());

        // Assert
        assertFalse(recipeRepository.existsById(recipe3.getId()));
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
        entityManager.persist(recipe1);
        entityManager.persist(recipe2);
        entityManager.persist(recipe3);
        entityManager.flush();

        // Act
        List<Recipe> recipes = recipeService.getAllRecipes();

        // Assert
        assertEquals(3, recipes.size());
        assertEquals("Pasta", recipes.get(0).getName());
        assertEquals("Veggie Burger", recipes.get(1).getName());
        assertEquals("Chicken Burger", recipes.get(2).getName());
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
