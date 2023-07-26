package com.example.brewbuddy.domain.repository

import android.content.Context
import android.net.Uri
import com.example.brewbuddy.data.remote.dto.MarketplaceItemDto
import com.example.brewbuddy.data.remote.dto.MarketplaceItemMetadataDto
import com.example.brewbuddy.data.remote.dto.PreferencesDto
import com.example.brewbuddy.data.remote.dto.RecipeDto
import com.example.brewbuddy.data.remote.dto.RecipeMetadataDto
import com.example.brewbuddy.data.remote.dto.UserDto
import com.example.brewbuddy.domain.model.Preferences

interface RecipeRepository {

    suspend fun getRecipes(query: String?): List<RecipeMetadataDto>

    suspend fun getRecipeById(recipeId: String): RecipeDto

    suspend fun getRecipesByUserId(recipeId: String): List<RecipeMetadataDto>
    
    suspend fun getPopular(): List<RecipeMetadataDto>

    suspend fun getRecommended(userId: String): List<RecipeMetadataDto>

    suspend fun getUserLikedRecipes(userId: String): List<RecipeMetadataDto>

    suspend fun getMarketplaceItems(query: String?): List<MarketplaceItemMetadataDto>
    
    suspend fun getMarketplaceItemById(itemId: String): MarketplaceItemDto

    suspend fun getMarketplaceItemsByUserId(userId: String): List<MarketplaceItemMetadataDto>

    suspend fun signInWithUsername(username: String, password: String): Boolean

    suspend fun signInWithGoogle(context: Context, username: String, email: String): Boolean

    suspend fun getUserById(userId: String): UserDto

    suspend fun getUserPreferencesById(id: String): PreferencesDto

    suspend fun setUserPreferencesById(id: String, preferences: Preferences): Boolean
    suspend fun setImageUploadByType(id: String, uri: Uri, type: String): Boolean
}