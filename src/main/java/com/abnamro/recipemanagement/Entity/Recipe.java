package com.abnamro.recipemanagement.Entity;

import com.abnamro.recipemanagement.util.RecipeManagementUtil;
import com.abnamro.recipemanagement.util.ValidIngredients;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Recipe Name cannot be blank")
    @Size(max = RecipeManagementUtil.MAX_LENGTH_NAME, message = "Recipe name must be less than 100 chars")
    @Pattern(regexp = RecipeManagementUtil.PATTERN_STRINGS, message = "Only strings are allowed for recipe name.")
    private String name;
    @NotNull(message = "Recipe type cannot be blank")
    private Boolean isVegetarian;
    @Positive(message = "Number of servings must be greater than zero")
    private int servings;
    @ElementCollection
    @NotEmpty(message = "Recipe without ingredients is not possible")
    @ValidIngredients
    private List<String> ingredients;
    @NotBlank(message = "Instructions cannot be blank")
    @Size(max = RecipeManagementUtil.MAX_LENGTH_INSTRUCTION, message = "Instructions must be less than 255 chars")
    @Pattern(regexp = RecipeManagementUtil.PATTERN_INSTRUCTIONS, message = "Instructions must contain letters and numbers")
    private String instructions;
    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
    @Column
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getIsVegetarian() {
        return isVegetarian;
    }

    public void setIsVegetarian(boolean isVegetarian) {
        this.isVegetarian = isVegetarian;
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

