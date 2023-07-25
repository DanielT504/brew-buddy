package com.example.brewbuddy.data.repository
import android.util.Log
import com.example.brewbuddy.data.remote.dto.Instructions
import com.example.brewbuddy.data.remote.dto.MarketplaceItemDto
import com.example.brewbuddy.data.remote.dto.MarketplaceItemMetadataDto
import com.example.brewbuddy.data.remote.dto.RecipeDto
import com.example.brewbuddy.data.remote.dto.RecipeMetadataDto
import com.example.brewbuddy.domain.repository.RecipeRepository
import com.example.brewbuddy.requests.getFunctions
import com.example.brewbuddy.domain.model.Author
import com.example.brewbuddy.domain.model.Recipe
import com.example.brewbuddy.domain.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RecipeRepositoryImplementation @Inject constructor () : RecipeRepository {

    override suspend fun getRecipes(query: String?): List<RecipeMetadataDto> {
        Log.d("GET_RECIPES", "Running")
        return withContext(Dispatchers.IO) {
            val dataDeferred = async {
                if(query != null) {
                    getFunctions()
                        .getHttpsCallable("getRecipesMetadata")
                        .call(hashMapOf("query" to query)).await()

                } else {
                    getFunctions()
                        .getHttpsCallable("getRecipesMetadata")
                        .call().await()
                }
            }
            val task = dataDeferred.await()
            val data = task.data as List<HashMap<String, Object>>
            return@withContext data.map{RecipeMetadataDto.from(it)}
        }
    }
    override suspend fun getRecipeById(id: String): RecipeDto {
        Log.d("GET_RECIPE_BY_ID", id)

        return withContext(Dispatchers.IO) {
            val dataDeferred = async {
                getFunctions()
                    .getHttpsCallable("getRecipeById")
                    .call(hashMapOf("recipeId" to id)).await()
            }
            val task = dataDeferred.await()
            val data = task.data as HashMap<String, Object>
            return@withContext RecipeDto.from(data)
        }
    }
    override suspend fun getPopular(): List<RecipeMetadataDto> {
        Log.d("GET_POPULAR", "Running")

        return withContext(Dispatchers.IO) {
            val dataDeferred = async {
                getFunctions()
                    .getHttpsCallable("getPopularRecipes")
                    .call().await()
            }
            val task = dataDeferred.await()
            val data = task.data as List<HashMap<String, Object>>
            return@withContext data.map{RecipeMetadataDto.from(it)}
        }
    }
    override suspend fun getRecipesByUserId(user_id: String): List<RecipeMetadataDto> {
        Log.d("GET_RECIPES_BY_USER_ID", user_id)

        return withContext(Dispatchers.IO) {
            val dataDeferred = async {
                getFunctions()
                    .getHttpsCallable("getUserRecipes")
                    .call(hashMapOf("authorId" to user_id)).await()
            }
            val task = dataDeferred.await()
            val data = task.data as List<HashMap<String, Object>>
            return@withContext data.map{RecipeMetadataDto.from(it)}

        }
    }

    override suspend fun getUserLikedRecipes(userId: String): List<RecipeMetadataDto> {
        Log.d("GET_USER_LIKED_RECIPES", "Running")

        return withContext(Dispatchers.IO) {
            val dataDeferred = async {
                getFunctions()
                    .getHttpsCallable("getUserLikedRecipes")
                    .call(hashMapOf("userId" to userId)).await()
            }
            val task = dataDeferred.await()
            val data = task.data as List<HashMap<String, Object>>
            return@withContext data.map{RecipeMetadataDto.from(it)}
        }
    }
    override suspend fun getRecommended(userId: String): List<RecipeMetadataDto> {
        Log.d("GET_RECOMMENDED", "Running")

        return withContext(Dispatchers.IO) {
            val dataDeferred = async {
                getFunctions()
                    .getHttpsCallable("getRecommendedRecipes")
                    .call(hashMapOf("userId" to userId)).await()
            }
            val task = dataDeferred.await()
            val data = task.data as List<HashMap<String, Object>>
            Log.d("RECOMMENDED", "$data")
            return@withContext data.map{RecipeMetadataDto.from(it)}
        }
    }
    override suspend fun getMarketplaceItems(query: String?): List<MarketplaceItemMetadataDto> {
        Log.d("GET_MARKETPLACE_ITEMS", "Running")

        return withContext(Dispatchers.IO) {
            val dataDeferred = async {
                if(query != null) {
                    getFunctions()
                        .getHttpsCallable("getMarketplaceItemsMetadata")
                        .call(hashMapOf("query" to query)).await()

                } else {
                    getFunctions()
                        .getHttpsCallable("getMarketplaceItemsMetadata")
                        .call().await()
                }
            }

            val task = dataDeferred.await()
            val data = task.data as List<HashMap<String, Object>>
            return@withContext data.map{MarketplaceItemMetadataDto.from(it)}
        }
    }
    override suspend fun getMarketplaceItemById(id: String): MarketplaceItemDto {
        Log.d("GET_MARKETPLACE_ITEM_BY_ID", id)

        return withContext(Dispatchers.IO) {
            val dataDeferred = async {
                getFunctions()
                    .getHttpsCallable("getMarketplaceItemById")
                    .call(hashMapOf("itemId" to id)).await()
            }
            val task = dataDeferred.await()
            val data = task.data as HashMap<String, Object>
            return@withContext MarketplaceItemDto.from(data)
        }
    }

    override suspend fun getMarketplaceItemsByUserId(userId: String): List<MarketplaceItemMetadataDto> {
        Log.d("GET_MARKETPLACE_ITEM_BY_USER_ID", userId)

        return withContext(Dispatchers.IO) {
            val dataDeferred = async {
                getFunctions()
                    .getHttpsCallable("getMarketplaceItemsByUserId")
                    .call(hashMapOf("userId" to userId)).await()
            }
            val task = dataDeferred.await()
            val data = task.data as List<HashMap<String, Object>>
            return@withContext data.map{MarketplaceItemMetadataDto.from(it)}
        }
    }
}