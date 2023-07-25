package com.example.brewbuddy.domain.repository

import android.content.Context
import com.example.brewbuddy.data.remote.dto.MarketplaceItemDto
import com.example.brewbuddy.data.remote.dto.MarketplaceItemMetadataDto
import com.example.brewbuddy.data.remote.dto.RecipeDto
import com.example.brewbuddy.data.remote.dto.RecipeMetadataDto
import com.example.brewbuddy.data.remote.dto.UserDto

interface RecipeRepository {

    suspend fun getRecipes(query: String?): List<RecipeMetadataDto>

    suspend fun getRecipeById(recipeId: String): RecipeDto

    suspend fun getRecipesByUserId(recipeId: String): List<RecipeMetadataDto>
    
    suspend fun getPopular(): List<RecipeMetadataDto>

    suspend fun getRecommended(userId: String): List<RecipeMetadataDto>

    suspend fun getMarketplaceItems(query: String?): List<MarketplaceItemMetadataDto>
    suspend fun getMarketplaceItemById(itemId: String): MarketplaceItemDto

    suspend fun getMarketplaceItemsByUserId(userId: String): List<MarketplaceItemMetadataDto>

    suspend fun signIn(username: String, password: String): Boolean

    suspend fun registerUserWithGoogle(context: Context, username: String, email: String): Boolean

    suspend fun getUserById(userId: String): UserDto
}