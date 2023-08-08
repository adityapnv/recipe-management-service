package com.abnamro.recipemanagement.util;

import com.abnamro.recipemanagement.Entity.Recipe;
import com.abnamro.recipemanagement.domain.RecipeFilterRequest;
import org.springframework.data.jpa.domain.Specification;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.CriteriaBuilder;
import java.util.ArrayList;
import java.util.List;

public class RecipeSpecifications {

    public static Specification<Recipe> getRecipeSpecification(RecipeFilterRequest filterRequest) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            addEqualPredicate(predicates, cb, root.get("isVegetarian"), filterRequest.getIsVegetarian());
            addEqualPredicate(predicates, cb, root.get("servings"), filterRequest.getServings());

            addIngredientPredicates(predicates, cb, root, filterRequest.getIncludeIngredients(), true);
            addIngredientPredicates(predicates, cb, root, filterRequest.getExcludeIngredients(), false);

            addLikePredicate(predicates, cb, cb.lower(root.get("instructions")), filterRequest.getSearchText());
            addLikePredicate(predicates, cb, cb.lower(root.get("name")), filterRequest.getName());

            addNotLikePredicate(predicates, cb, cb.lower(root.get("instructions")), filterRequest.getExcludeInstructions());
            addNotLikePredicate(predicates, cb, cb.lower(root.get("name")), filterRequest.getExcludeName());

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static void addEqualPredicate(List<Predicate> predicates, CriteriaBuilder cb, Expression<?> expression, Object value) {
        if (value != null) {
            predicates.add(cb.equal(expression, value));
        }
    }

    //
    private static void addIngredientPredicates(List<Predicate> predicates, CriteriaBuilder cb, Root<Recipe> root, List<String> ingredients, boolean isInclusion) {
        if (ingredients != null && !ingredients.isEmpty()) {
            List<Predicate> ingredientPredicates = new ArrayList<>();
            for (String ingredient : ingredients) {
                Predicate predicate = isInclusion ?
                        cb.isMember(ingredient, root.get("ingredients")) :
                        cb.isNotMember(ingredient, root.get("ingredients"));
                ingredientPredicates.add(predicate);
            }

            Predicate ingredientPredicate = isInclusion ?
                    cb.and(ingredientPredicates.toArray(new Predicate[0])) :
                    cb.or(ingredientPredicates.toArray(new Predicate[0]));

            predicates.add(ingredientPredicate);
        }
    }

    private static void addLikePredicate(List<Predicate> predicates, CriteriaBuilder cb, Expression<String> expression, String value) {
        if (value != null && !value.isEmpty()) {
            predicates.add(cb.like(expression, "%" + value.toLowerCase() + "%"));
        }
    }

    private static void addNotLikePredicate(List<Predicate> predicates, CriteriaBuilder cb, Expression<String> expression, String value) {
        if (value != null && !value.isEmpty()) {
            predicates.add(cb.notLike(expression, "%" + value.toLowerCase() + "%"));
        }
    }

}



