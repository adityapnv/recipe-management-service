package com.abnamro.recipemanagement.service;

import com.abnamro.recipemanagement.Entity.Recipe;
import com.abnamro.recipemanagement.domain.RecipeFilterRequest;

import java.util.List;

public interface RecipeService {
    List<Recipe> getAllRecipes();
    Recipe getRecipeById(Long id);
    Recipe addRecipe(Recipe recipe);
    Recipe updateRecipe(Long id, Recipe updatedRecipe);
    boolean removeRecipe(Long id);

    List<Recipe> searchRecipes(RecipeFilterRequest filterRequest);
}
