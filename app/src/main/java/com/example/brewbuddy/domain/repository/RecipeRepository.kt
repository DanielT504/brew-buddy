package com.example.brewbuddy.domain.repository

import com.example.brewbuddy.data.remote.dto.RecipeDto
import com.example.brewbuddy.data.remote.dto.RecipeMetadataDto

interface RecipeRepository {

    suspend fun getRecipes(): List<RecipeMetadataDto>

    suspend fun getRecipeById(recipeId: String): RecipeDto
}