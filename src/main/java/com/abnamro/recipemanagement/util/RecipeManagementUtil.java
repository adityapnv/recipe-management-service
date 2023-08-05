package com.abnamro.recipemanagement.util;

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

}
