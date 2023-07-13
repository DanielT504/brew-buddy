package com.example.brewbuddy.data.repository
import com.example.brewbuddy.data.remote.dto.RecipeDto
import com.example.brewbuddy.data.remote.dto.SpoonacularApi
import com.example.brewbuddy.domain.repository.RecipeRepository
import javax.inject.Inject

class RecipeRepositoryImplementation @Inject constructor (
    private val api: SpoonacularApi
    ) : RecipeRepository {
    override suspend fun getRecipes(): List<RecipeDto> {
        return api.getRecipes().recipes
    }

    override suspend fun getRecipeById(recipeId: String): RecipeDto {
        TODO("Not yet implemented")
    }
}