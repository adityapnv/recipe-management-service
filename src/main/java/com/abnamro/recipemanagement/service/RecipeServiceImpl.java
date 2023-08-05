package com.abnamro.recipemanagement.service;

import com.abnamro.recipemanagement.Entity.Recipe;
import com.abnamro.recipemanagement.domain.RecipeFilterRequest;
import com.abnamro.recipemanagement.exception.RecipeNotFoundException;
import com.abnamro.recipemanagement.repository.RecipeRepository;
import com.abnamro.recipemanagement.util.ErrorMessage;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
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
            throw new RecipeNotFoundException(ErrorMessage.RECIPE_NOT_FOUND);
        }
        return recipes;
    }

    @Override
    public Recipe getRecipeById(Long id) {
        return recipeRepository.findById(id).orElseThrow(()
                -> new RecipeNotFoundException(ErrorMessage.RECIPE_NOT_FOUND + id));
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
        Specification<Recipe> spec = Specification.where(null);

        if (filterRequest.getIsVegetarian() != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("vegetarian"), filterRequest.getIsVegetarian()));
        }

        if (filterRequest.getServings() != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("servings"), filterRequest.getServings()));
        }

        if (filterRequest.getIncludeIngredients() != null && !filterRequest.getIncludeIngredients().isEmpty()) {
            spec = spec.and((root, query, cb) -> {
                List<Predicate> ingredientPredicates = new ArrayList<>();
                for (String ingredient : filterRequest.getIncludeIngredients()) {
                    ingredientPredicates.add(cb.isMember(ingredient, root.get("ingredients")));
                }
                return cb.and(ingredientPredicates.toArray(new Predicate[0]));
            });
        }

        if (filterRequest.getExcludeIngredients() != null && !filterRequest.getExcludeIngredients().isEmpty()) {
            spec = spec.and((root, query, cb) -> {
                List<Predicate> ingredientPredicates = new ArrayList<>();
                for (String ingredient : filterRequest.getExcludeIngredients()) {
                    ingredientPredicates.add(cb.isNotMember(ingredient, root.get("ingredients")));
                }
                return cb.and(ingredientPredicates.toArray(new Predicate[0]));
            });
        }

        if (filterRequest.getSearchText() != null && !filterRequest.getSearchText().isEmpty()) {
            String searchText = "%" + filterRequest.getSearchText().toLowerCase() + "%";
            spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("instructions")), searchText));
        }

        List<Recipe> recipes = recipeRepository.findAll(spec);
        if (recipes.isEmpty()){
            throw new RecipeNotFoundException(ErrorMessage.RECIPE_NOT_FOUND);
        }
        return recipes;
    }


}

