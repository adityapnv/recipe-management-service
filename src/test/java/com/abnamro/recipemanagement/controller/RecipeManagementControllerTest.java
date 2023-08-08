package com.abnamro.recipemanagement.controller;

import com.abnamro.recipemanagement.Entity.Recipe;
import com.abnamro.recipemanagement.domain.RecipeFilterRequest;
import com.abnamro.recipemanagement.domain.RecipeRequest;
import com.abnamro.recipemanagement.service.RecipeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@ExtendWith(SpringExtension.class)
@WebMvcTest(RecipeManagementController.class)
public class RecipeManagementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RecipeService recipeService;

    Recipe recipe1;
    Recipe recipe2;
    RecipeRequest recipeRequest;

    @BeforeEach
    void init() {
        recipeRequest = new RecipeRequest();
        recipeRequest.setName("Test Recipe");
        recipeRequest.setServings(4);
        recipeRequest.setVegetarian(true);
        recipeRequest.setIngredients(Arrays.asList("rice","water"));
        recipeRequest.setInstructions("cooking");

        recipe1 = new Recipe();
        recipe1.setId(1L);
        recipe1.setName(recipeRequest.getName());
        recipe1.setIsVegetarian(true);
        recipe1.setInstructions("oven");
        recipe1.setIngredients(List.of("test1"));
        recipe1.setServings(3);

        recipe2 = new Recipe();
        recipe2.setId(2L);
        recipe2.setName("Recipe 2");
        recipe2.setIsVegetarian(true);
        recipe2.setInstructions("oven");
        recipe2.setIngredients(List.of("test1"));
        recipe2.setServings(6);

    }

    @Test
    void testGetRecipeById() throws Exception {
        when(recipeService.getRecipeById(1L)).thenReturn(recipe1);

        mockMvc.perform(MockMvcRequestBuilders.get("/recipes/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));
    }

    @Test
    void testAddRecipe() throws Exception {

        when(recipeService.addRecipe(any(RecipeRequest.class))).thenReturn(recipe1);

        mockMvc.perform(MockMvcRequestBuilders.post("/recipes")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(recipeRequest)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(content().string("{\"id\":1,\"name\":\"Test Recipe\",\"isVegetarian\":true,\"servings\":3,\"ingredients\":[\"test1\"],\"instructions\":\"oven\",\"createdAt\":null,\"updatedAt\":null}"));
    }

    @Test
    void testUpdateRecipe() throws Exception {
        recipe1.setName("Updated Recipe Name");

        RecipeRequest updatedRequest = new RecipeRequest();
        updatedRequest.setName("Updated Recipe Name");
        updatedRequest.setServings(4);
        updatedRequest.setVegetarian(true);
        updatedRequest.setIngredients(Arrays.asList("rice","water"));
        updatedRequest.setInstructions("cooking");

        when(recipeService.updateRecipe(anyLong(), any(RecipeRequest.class))).thenReturn(recipe1);

        mockMvc.perform(MockMvcRequestBuilders.put("/recipes/1")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(updatedRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Updated Recipe Name"));
    }

    @Test
    void testDeleteRecipe() throws Exception {
        when(recipeService.removeRecipe(1L)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/recipes/1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testSearchRecipes() throws Exception {

        when(recipeService.searchRecipes(any(RecipeFilterRequest.class))).thenReturn(Arrays.asList(recipe1, recipe2));

        mockMvc.perform(MockMvcRequestBuilders.get("/recipes/filter?isVegetarian=true"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2));
    }

    @Test
    void testAddRecipe_NullRequest() throws Exception {

        when(recipeService.addRecipe(any(RecipeRequest.class))).thenReturn(recipe1);

        mockMvc.perform(MockMvcRequestBuilders.post("/recipes")
                        .contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andExpect(content().string("{\"status\":500,\"message\":\"An unexpected error occurred with error Required request body is missing: public org.springframework.http.ResponseEntity<com.abnamro.recipemanagement.Entity.Recipe> com.abnamro.recipemanagement.controller.RecipeManagementController.addRecipe(com.abnamro.recipemanagement.domain.RecipeRequest)\"}"));
    }

    @Test
    void testAddRecipe_InvalidServings() throws Exception {
        recipeRequest.setServings(-1);

        when(recipeService.addRecipe(any(RecipeRequest.class))).thenReturn(recipe1);

        mockMvc.perform(MockMvcRequestBuilders.post("/recipes")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(recipeRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(content().string("{\"status\":400,\"message\":\"Number of servings must be greater than zero\"}"));
    }

    @Test
    void testAddRecipe_InvalidIngredients() throws Exception {
        recipeRequest.setIngredients(List.of("$#%$"));

        when(recipeService.addRecipe(any(RecipeRequest.class))).thenReturn(recipe1);

        mockMvc.perform(MockMvcRequestBuilders.post("/recipes")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(recipeRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(content().string("{\"status\":400,\"message\":\"Invalid ingredients provided\"}"));
    }

    @Test
    void testAddRecipe_InvalidName() throws Exception {
        recipeRequest.setName("$#%$");

        when(recipeService.addRecipe(any(RecipeRequest.class))).thenReturn(recipe1);

        mockMvc.perform(MockMvcRequestBuilders.post("/recipes")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(recipeRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(content().string("{\"status\":400,\"message\":\"Only strings are allowed for recipe name.\"}"));
    }
}
