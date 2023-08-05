package com.abnamro.recipemanagement.controller;

import com.abnamro.recipemanagement.Entity.Recipe;
import com.abnamro.recipemanagement.domain.RecipeFilterRequest;
import com.abnamro.recipemanagement.service.RecipeService;
import com.abnamro.recipemanagement.util.RecipeManagementUtil;
import com.abnamro.recipemanagement.util.ValidIngredients;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/recipes")
@Validated
public class RecipeManagementController {
    private final RecipeService recipeService;

    public RecipeManagementController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping
    public ResponseEntity<List<Recipe>> getAllRecipes() {
        List<Recipe> recipes = recipeService.getAllRecipes();
        return ResponseEntity.ok(recipes);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<Recipe> addRecipe(@Valid @RequestBody Recipe recipe) {
        Recipe addedRecipe = recipeService.addRecipe(recipe);
        return new ResponseEntity<>(addedRecipe, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Recipe> getRecipeById(@PathVariable("id") @Positive Long id) {
        Recipe recipe = recipeService.getRecipeById(id);
        return ResponseEntity.ok(recipe);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<Recipe> updateRecipe(@PathVariable("id") @Positive Long id,
                                               @Valid @RequestBody Recipe updatedRecipe) {
        Recipe recipe = recipeService.updateRecipe(id, updatedRecipe);
        return ResponseEntity.ok(recipe);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<String> deleteRecipe(@PathVariable("id") @Positive Long id) {
        recipeService.removeRecipe(id);
        return ResponseEntity.ok("Recipe deleted successfully.");
    }

    @GetMapping("/filter")
    public ResponseEntity<List<Recipe>> searchRecipes(

            @RequestParam(value = "isVegetarian", required = false) Boolean isVegetarian,
            @RequestParam(value = "servings", required = false) Integer servings,
            @RequestParam(value = "includeIngredients", required = false)
            @ValidIngredients List<String> includeIngredients,
            @RequestParam(value = "excludeIngredients", required = false)
            @ValidIngredients List<String> excludeIngredients,
            @RequestParam(value = "searchText", required = false) String searchText) {

        RecipeFilterRequest filterRequest = new RecipeFilterRequest();
        filterRequest.setIsVegetarian(isVegetarian);
        filterRequest.setServings(RecipeManagementUtil.validateServings(servings));
        filterRequest.setIncludeIngredients(includeIngredients);
        filterRequest.setExcludeIngredients(excludeIngredients);
        filterRequest.setSearchText(RecipeManagementUtil.validateSearchText(searchText));

        List<Recipe> filteredRecipes = recipeService.searchRecipes(filterRequest);
        return ResponseEntity.ok(filteredRecipes);
    }
}