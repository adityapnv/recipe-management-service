package com.abnamro.recipemanagement.service;

import com.abnamro.recipemanagement.Entity.Recipe;
import com.abnamro.recipemanagement.domain.RecipeRequest;
import com.abnamro.recipemanagement.exception.RecipeNotFoundException;
import com.abnamro.recipemanagement.repository.RecipeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
public class RecipeServiceImplTest {
    @Mock
    private RecipeRepository recipeRepository;

    @InjectMocks
    private RecipeServiceImpl recipeService;

    Recipe recipe1;
    Recipe recipe2;
    RecipeRequest recipeRequest;

    @BeforeEach
    public void setUp() {
        recipeRequest = new RecipeRequest();
        recipeRequest.setName("Test Recipe");
        recipeRequest.setServings(4);
        recipeRequest.setVegetarian(true);
        recipeRequest.setIngredients(Arrays.asList("rice","water"));
        recipeRequest.setInstructions("cooking");

        recipe1 = new Recipe();
        recipe1.setId(1L);
        recipe1.setName(recipeRequest.getName());
        recipe1.setIsVegetarian(true);
        recipe1.setInstructions("oven");
        recipe1.setIngredients(List.of("test1"));

        recipe2 = new Recipe();
        recipe2.setId(2L);
        recipe2.setName("Recipe 2");
        recipe2.setIsVegetarian(false);
        recipe2.setInstructions("oven");
        recipe2.setIngredients(List.of("test1"));
    }

    @Test
    void testGetRecipeByID (){
        // Arrange
        when(recipeRepository.findById(anyLong())).thenReturn(Optional.ofNullable(recipe1));
        // Act
        Recipe resultRecipe = recipeService.getRecipeById(recipe1.getId());
        // Assert
        Assertions.assertEquals(recipe1.getId(), resultRecipe.getId());
        Assertions.assertEquals(recipe1.getServings(), resultRecipe.getServings());
        Assertions.assertEquals(recipe1.getInstructions(), resultRecipe.getInstructions());
        Assertions.assertTrue(resultRecipe.getIsVegetarian());
    }
    @Test
    void testGetRecipeById_RecipeNotFound() {
        // Arrange
        long recipeId = 1L;
        when(recipeRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        Assertions.assertThrows(RecipeNotFoundException.class, () -> recipeService.getRecipeById(recipeId));

        verify(recipeRepository, times(1)).findById(recipeId);
    }

    @Test
    void testGetRecipes_RecipeNotFound() {
        // Arrange
        when(recipeRepository.findAll()).thenReturn(Collections.emptyList());

        // Act & Assert
        Assertions.assertThrows(RecipeNotFoundException.class, () -> recipeService.getAllRecipes());

        verify(recipeRepository, times(1)).findAll();
    }

    @Test
    void testUpdateRecipeById_RecipeNotFound() {
        // Arrange
        long recipeId = 1L;
        when(recipeRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        Assertions.assertThrows(RecipeNotFoundException.class, () -> recipeService.updateRecipe(recipeId, null));

        verify(recipeRepository, times(1)).findById(recipeId);
    }

    @Test
    void testDeleteRecipeById_RecipeNotFound() {
        // Arrange
        long recipeId = 1L;
        when(recipeRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        Assertions.assertThrows(RecipeNotFoundException.class, () -> recipeService.removeRecipe(recipeId));

        verify(recipeRepository, times(1)).findById(recipeId);
    }




}
