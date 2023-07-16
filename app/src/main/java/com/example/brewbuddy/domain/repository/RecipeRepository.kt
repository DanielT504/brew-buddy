package com.example.brewbuddy.domain.repository

import com.example.brewbuddy.data.remote.dto.RecipeById.RecipeByIdDto
import com.example.brewbuddy.data.remote.dto.RecipeDto

interface RecipeRepository {

    suspend fun getRecipes(): List<RecipeDto>

    suspend fun getRecipeById(recipeId: String): RecipeByIdDto
}