package com.abnamro.recipemanagement.repository;

import com.abnamro.recipemanagement.Entity.Recipe;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    List<Recipe> findAll(Specification<Recipe> spec);
}

