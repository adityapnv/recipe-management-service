package com.abnamro.recipemanagement.service;

import com.abnamro.recipemanagement.Entity.Recipe;
import com.abnamro.recipemanagement.domain.RecipeFilterRequest;
import com.abnamro.recipemanagement.domain.RecipeRequest;

import java.util.List;

public interface RecipeService {
    List<Recipe> getAllRecipes();
    Recipe getRecipeById(Long id);
    Recipe addRecipe(RecipeRequest recipe);
    Recipe updateRecipe(Long id, RecipeRequest updatedRecipe);
    boolean removeRecipe(Long id);

    List<Recipe> searchRecipes(RecipeFilterRequest filterRequest);
}
