package com.abnamro.recipemanagement.util;

import com.abnamro.recipemanagement.Entity.Recipe;
import com.abnamro.recipemanagement.domain.RecipeFilterRequest;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class RecipeSpecifications {

    public static Specification<Recipe> getRecipeSpecification(RecipeFilterRequest filterRequest) {
        Specification<Recipe> spec = Specification.where(null);

        if (filterRequest.getIsVegetarian() != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("isVegetarian"), filterRequest.getIsVegetarian()));
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

        if (filterRequest.getExcludeInstructions() != null && !filterRequest.getExcludeInstructions().isEmpty()) {
            String excludeInstructionsText = "%" + filterRequest.getExcludeInstructions().toLowerCase() + "%";
            spec = spec.and((root, query, cb) -> cb.notLike(cb.lower(root.get("instructions")), excludeInstructionsText));
        }

        if (filterRequest.getName() != null && !filterRequest.getName().isEmpty()) {
            String nameText = "%" + filterRequest.getName().toLowerCase() + "%";
            spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("name")), nameText));
        }

        if (filterRequest.getExcludeName() != null && !filterRequest.getExcludeName().isEmpty()) {
            String excludeNameText = "%" + filterRequest.getExcludeName().toLowerCase() + "%";
            spec = spec.and((root, query, cb) -> cb.notLike(cb.lower(root.get("name")), excludeNameText));
        }
        return spec;
    }

}




