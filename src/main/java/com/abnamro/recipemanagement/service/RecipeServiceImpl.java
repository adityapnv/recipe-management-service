package com.abnamro.recipemanagement.service;

import com.abnamro.recipemanagement.Entity.Recipe;
import com.abnamro.recipemanagement.domain.RecipeFilterRequest;
import com.abnamro.recipemanagement.domain.RecipeRequest;
import com.abnamro.recipemanagement.exception.RecipeNotFoundException;
import com.abnamro.recipemanagement.repository.RecipeRepository;
import com.abnamro.recipemanagement.util.ErrorMessage;
import com.abnamro.recipemanagement.util.RecipeManagementUtil;
import com.abnamro.recipemanagement.util.RecipeSpecifications;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RecipeServiceImpl implements RecipeService {
    private final RecipeRepository recipeRepository;

    public RecipeServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    public List<Recipe> getAllRecipes() {
        List<Recipe> recipes = recipeRepository.findAll();
        if (recipes.isEmpty()){
            throw new RecipeNotFoundException(ErrorMessage.RECIPES_NOT_FOUND);
        }
        return recipes;
    }

    @Override
    public Recipe getRecipeById(Long id) {
        return recipeRepository.findById(id).orElseThrow(()
                -> new RecipeNotFoundException(ErrorMessage.RECIPE_NOT_FOUND + id));
    }

    @Override
    public Recipe addRecipe(RecipeRequest recipeRequest) {
        Recipe newRecipe = new Recipe();
        mapRecipeRequestToRecipe(recipeRequest, newRecipe);
        return recipeRepository.save(newRecipe);
    }

    @Override
    public Recipe updateRecipe(Long id, RecipeRequest updatedRecipeRequest) {
        Recipe existingRecipe = getRecipeById(id);
        if (existingRecipe != null) {
            mapRecipeRequestToRecipe(updatedRecipeRequest, existingRecipe);
            return recipeRepository.save(existingRecipe);
        } else {
            throw new RecipeNotFoundException(ErrorMessage.RECIPE_NOT_FOUND + id);
        }
    }

    @Override
    public boolean removeRecipe(Long id) {
        Recipe existingRecipe = getRecipeById(id);
        if (existingRecipe != null) {
            recipeRepository.delete(existingRecipe);
            return true;
        } else {
            throw new RecipeNotFoundException(ErrorMessage.RECIPE_NOT_FOUND + id);
        }
    }

   @Override
    public List<Recipe> searchRecipes(RecipeFilterRequest filterRequest) {
       Specification<Recipe> spec = RecipeSpecifications.getRecipeSpecification(filterRequest);
       List<Recipe> recipes = recipeRepository.findAll(spec);
        if (recipes.isEmpty()){
            throw new RecipeNotFoundException(ErrorMessage.RECIPE_NOT_FOUND);
        }
        return recipes;
    }


    private void mapRecipeRequestToRecipe(RecipeRequest recipeRequest, Recipe recipe) {
        recipe.setName(recipeRequest.getName());
        recipe.setIsVegetarian(recipeRequest.isVegetarian());
        recipe.setServings(recipeRequest.getServings());
        recipe.setIngredients(RecipeManagementUtil.replaceToLowerCaseList(recipeRequest.getIngredients()));
        recipe.setInstructions(recipeRequest.getInstructions());
    }

}

