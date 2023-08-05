package com.abnamro.recipemanagement.exception;

public class RecipeNotFoundException extends RuntimeException {
    public RecipeNotFoundException() {
        super("Recipe not found.");
    }

    public RecipeNotFoundException(String message) {
        super(message);
    }

    public RecipeNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
