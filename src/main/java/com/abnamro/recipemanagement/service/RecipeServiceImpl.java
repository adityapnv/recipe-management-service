package com.abnamro.recipemanagement.service;

import com.abnamro.recipemanagement.Entity.Recipe;
import com.abnamro.recipemanagement.domain.RecipeFilterRequest;
import com.abnamro.recipemanagement.repository.RecipeRepository;
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
        return recipeRepository.findAll();
    }

    @Override
    public Recipe getRecipeById(Long id) {
        return recipeRepository.findById(id).orElse(null);
    }

    @Override
    public Recipe addRecipe(Recipe recipe) {
        return recipeRepository.save(recipe);
    }

    @Override
    public Recipe updateRecipe(Long id, Recipe updatedRecipe) {
        Recipe existingRecipe = getRecipeById(id);
        if (existingRecipe != null) {
            existingRecipe.setName(updatedRecipe.getName());
            existingRecipe.setVegetarian(updatedRecipe.isVegetarian());
            existingRecipe.setServings(updatedRecipe.getServings());
            existingRecipe.setIngredients(updatedRecipe.getIngredients());
            existingRecipe.setInstructions(updatedRecipe.getInstructions());
            return recipeRepository.save(existingRecipe);
        }
        return null;
    }

    @Override
    public boolean removeRecipe(Long id) {
        Recipe existingRecipe = getRecipeById(id);
        if (existingRecipe != null) {
            recipeRepository.delete(existingRecipe);
            return true;
        }
        return false;
    }

    @Override
    public List<Recipe> searchRecipes(RecipeFilterRequest filterRequest) {
        Specification<Recipe> spec = Specification.where(null);

        if (filterRequest.getIsVegetarian() != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("vegetarian"), filterRequest.getIsVegetarian()));
        }

        if (filterRequest.getServings() != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("servings"), filterRequest.getServings()));
        }

        if (filterRequest.getIncludeIngredients() != null && !filterRequest.getIncludeIngredients().isEmpty()) {
            spec = spec.and((root, query, cb) -> root.get("ingredients").in(filterRequest.getIncludeIngredients()));
        }

        if (filterRequest.getExcludeIngredients() != null && !filterRequest.getExcludeIngredients().isEmpty()) {
            spec = spec.and((root, query, cb) -> cb.not(root.get("ingredients").in(filterRequest.getExcludeIngredients())));
        }

        if (filterRequest.getSearchText() != null && !filterRequest.getSearchText().isEmpty()) {
            spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("instructions")), "%" + filterRequest.getSearchText().toLowerCase() + "%"));
        }

        return recipeRepository.findAll(spec);
    }


}

