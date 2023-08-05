package com.abnamro.recipemanagement.util;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class ValidIngredientsValidator implements ConstraintValidator<ValidIngredients, List<String>> {
    @Override
    public boolean isValid(List<String> value, ConstraintValidatorContext context) {
        // There are many ways to validate ingredients by implementing custom validation logic for ingredients
        // For example, check if the ingredient is valid based on a predefined list, database lookup, etc.
        // But for demo purpose allowing all strings with letters.

        return value == null || (!value.isEmpty() && containsOnlyLetters(value));
    }

    public static boolean containsOnlyLetters(List<String> input) {
        for(String str : input){
            //Regular expression to check if the input contains only letters (alphabets)
            if(!str.matches(RecipeManagementUtil.PATTERN_STRINGS)){
                return false;
            }
        }
        return true;
    }
}

