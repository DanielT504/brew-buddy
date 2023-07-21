package com.example.brewbuddy.domain.use_case.get_content_based_recommendations

import com.example.brewbuddy.domain.model.Recipe
import com.example.brewbuddy.domain.model.User
import com.example.brewbuddy.domain.repository.RecipeRepository
import javax.inject.Inject

class GetContentBasedRecommendations @Inject constructor(
    private val repository: RecipeRepository
) {
    suspend operator fun invoke(user: User): List<Recipe> {
        val allRecipes = repository.getAllRecipes()
        val likedRecipeIds = user.likedRecipeIds

        val recipeAttributes = allRecipes.filter { it.id in likedRecipeIds }
            .flatMap { it.diets  }
            .toSet()

        return allRecipes.filter { recipe ->
            recipe.id !in likedRecipeIds && recipe.diets.any { it in recipeAttributes}
        }
    }
}