package com.example.brewbuddy.domain.repository

import com.example.brewbuddy.data.remote.dto.MarketplaceItemDto
import com.example.brewbuddy.data.remote.dto.MarketplaceItemMetadataDto
import com.example.brewbuddy.data.remote.dto.RecipeDto
import com.example.brewbuddy.data.remote.dto.RecipeMetadataDto

interface RecipeRepository {

    suspend fun getRecipes(query: String?): List<RecipeMetadataDto>

    suspend fun getRecipeById(recipeId: String): RecipeDto

    suspend fun getRecipesByUserId(recipeId: String): List<RecipeMetadataDto>
    
    suspend fun getPopular(): List<RecipeMetadataDto>

    suspend fun getRecommended(userId: String): List<RecipeMetadataDto>

    suspend fun getMarketplaceItems(): List<MarketplaceItemMetadataDto>
    suspend fun getMarketplaceItemById(itemId: String): MarketplaceItemDto

}