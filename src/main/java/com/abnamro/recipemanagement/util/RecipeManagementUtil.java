package com.abnamro.recipemanagement.util;

import com.abnamro.recipemanagement.exception.RecipeInvalidFilterException;

public class RecipeManagementUtil {

    /**
     * Matches for recipe name
     */
    public static final String PATTERN_STRINGS = "^[A-Za-z]+( [A-Za-z]+)*$";

    /**
     * Max length of recipe
     */
    public static final int MAX_LENGTH_NAME = 100;

    /**
     * Default max length
     */
    public static final int MAX_LENGTH_INSTRUCTION= 255;

    /**
     * Matcher to allow only string and numbers and excluding special chars.
     */
    public static final String PATTERN_INSTRUCTIONS = "^[A-Za-z0-9\\s.,'\"_\\-]{0,254}$";

    public static final String PATTERN_POSITIVE_NUMBER = "^(?!0$)[1-9][0-9]*$";

    public static Integer validateOptionalServings(Integer servings) {
        if(servings == null || isValidPositiveNumber(String.valueOf(servings))){
            return servings;
        } else {
            throw new RecipeInvalidFilterException();
        }
    }

    public static String validateOptionalSearchText(String searchText) {
        if(searchText == null || searchText.matches(PATTERN_INSTRUCTIONS)){
            return searchText;
        } else {
            throw new RecipeInvalidFilterException();
        }
    }

    public static String validateOptionalName(String name) {
        if(name == null || name.matches(PATTERN_STRINGS)){
            return name;
        } else {
            throw new RecipeInvalidFilterException();
        }
    }

    private static boolean isValidPositiveNumber(String input) {
        // Use the regex pattern to check if the input is a positive number
        return input.matches(PATTERN_POSITIVE_NUMBER);
    }

}
