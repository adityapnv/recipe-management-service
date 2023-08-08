package com.abnamro.recipemanagement.domain;

import com.abnamro.recipemanagement.util.RecipeManagementUtil;
import com.abnamro.recipemanagement.util.ValidIngredients;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.*;
import java.util.List;

public class RecipeRequest {
    @NotBlank(message = "Recipe Name cannot be blank")
    @Size(max = RecipeManagementUtil.MAX_LENGTH_NAME, message = "Recipe name must be less than 100 chars")
    @Pattern(regexp = RecipeManagementUtil.PATTERN_STRINGS, message = "Only strings are allowed for recipe name.")
    private String name;
    @NotNull(message = "Dish type cannot be blank")
    @JsonProperty("isVegetarian")
    private boolean vegetarian;
    @Positive(message = "Number of servings must be greater than zero")
    private int servings;
    @NotEmpty(message = "Recipe without ingredients is not possible")
    @ValidIngredients
    private List<String> ingredients;
    @NotBlank(message = "Instructions cannot be blank")
    @Size(max = RecipeManagementUtil.MAX_LENGTH_INSTRUCTION, message = "Instructions must be less than 255 chars")
    @Pattern(regexp = RecipeManagementUtil.PATTERN_INSTRUCTIONS, message = "Instructions must contain letters and numbers")
    private String instructions;

    // Default constructor (required for deserialization)
    public RecipeRequest() {
    }

    // Parameterized constructor
    public RecipeRequest(String name, boolean vegetarian, int servings, List<String> ingredients, String instructions) {
        this.name = name;
        this.vegetarian = vegetarian;
        this.servings = servings;
        this.ingredients = ingredients;
        this.instructions = instructions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isVegetarian() {
        return vegetarian;
    }

    public void setVegetarian(boolean vegetarian) {
        this.vegetarian = vegetarian;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }
}

