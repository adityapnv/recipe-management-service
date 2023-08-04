package com.abnamro.recipemanagement;

import com.abnamro.recipemanagement.controller.RecipeManagementController;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RecipeManagementApplicationTests {

	@Autowired
	RecipeManagementController recipeManagementController;

	@Test
	void contextLoads(){
		Assertions.assertThat(recipeManagementController).isNotNull();
	}

}
