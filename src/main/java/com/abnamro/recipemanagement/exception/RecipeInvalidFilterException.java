package com.abnamro.recipemanagement.exception;

public class RecipeInvalidFilterException extends RuntimeException{

    public RecipeInvalidFilterException() {
        super("Invalid input used in filter criteria");
    }

    public RecipeInvalidFilterException(String message) {
        super(message);
    }

    public RecipeInvalidFilterException(String message, Throwable cause) {
        super(message, cause);
    }
}
