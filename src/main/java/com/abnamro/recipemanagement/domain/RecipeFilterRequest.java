package com.abnamro.recipemanagement.domain;

import java.util.List;

public class RecipeFilterRequest {
    private Boolean isVegetarian;

    private Integer servings;
    private List<String> includeIngredients;
    private List<String> excludeIngredients;
    private String searchText;
    private String excludeInstructions;
    private String name;
    private String excludeName;

    public String getExcludeInstructions() {
        return excludeInstructions;
    }

    public void setExcludeInstructions(String excludeInstructions) {
        this.excludeInstructions = excludeInstructions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExcludeName() {
        return excludeName;
    }

    public void setExcludeName(String excludeName) {
        this.excludeName = excludeName;
    }

    public Boolean getIsVegetarian() {
        return isVegetarian;
    }

    public void setIsVegetarian(Boolean vegetarian) {
        isVegetarian = vegetarian;
    }

    public Integer getServings() {
        return servings;
    }

    public void setServings(Integer servings) {
        this.servings = servings;
    }

    public List<String> getIncludeIngredients() {
        return includeIngredients;
    }

    public void setIncludeIngredients(List<String> includeIngredients) {
        this.includeIngredients = includeIngredients;
    }

    public List<String> getExcludeIngredients() {
        return excludeIngredients;
    }

    public void setExcludeIngredients(List<String> excludeIngredients) {
        this.excludeIngredients = excludeIngredients;
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }
}

