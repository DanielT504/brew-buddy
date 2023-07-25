package com.example.brewbuddy.recipes

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.brewbuddy.common.Resource
import com.example.brewbuddy.data.remote.dto.RecipeDto
import com.example.brewbuddy.data.remote.dto.toRecipe
import com.example.brewbuddy.domain.model.Recipe
import com.example.brewbuddy.domain.model.MarketplaceItemMetadata
import com.example.brewbuddy.domain.model.RecipeMetadata
import com.example.brewbuddy.domain.model.User
import com.example.brewbuddy.domain.use_case.get_account.GetUserByIdUseCase
import com.example.brewbuddy.domain.use_case.get_marketplace.GetMarketplaceItemsByUserIdUseCase
import com.example.brewbuddy.domain.use_case.get_recipes.GetUserRecipesUseCase
import com.example.brewbuddy.domain.use_case.get_user_liked_recipes.GetUserLikedRecipesUseCase
import com.example.brewbuddy.profile.UserState
import com.example.brewbuddy.profile.db
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel  @Inject constructor(
    private val getUserRecipesUseCase: GetUserRecipesUseCase,
    private val getUserByIdUseCase: GetUserByIdUseCase,
    private val getUserLikedRecipesUseCase: GetUserLikedRecipesUseCase,
    private val getMarketplaceItemsByUserIdUseCase: GetMarketplaceItemsByUserIdUseCase,
    savedStateHandle: SavedStateHandle
): ViewModel(){
    private val _recipesState = mutableStateOf(UserState<List<RecipeMetadata>>())
    val recipesState: State<UserState<List<RecipeMetadata>>> = _recipesState

    private val _listingState = mutableStateOf(UserState<List<MarketplaceItemMetadata>>())
    val listingState: State<UserState<List<MarketplaceItemMetadata>>> = _listingState

    private val _userState = mutableStateOf(UserState<User>())
    val userState: State<UserState<User>> = _userState

    var _userLikedRecipes = mutableStateOf<List<Recipe>>(emptyList())
    val userLikedRecipes: State<List<Recipe>> get() = _userLikedRecipes

    private val userId = FirebaseAuth.getInstance().currentUser!!.uid

    init {
        getUserById(userId)
        getRecipesByUserId(userId)
        getUserLikedRecipes()
        getMarketplaceItemsByUserId(userId)
    }
    private fun getUserById(userId: String) {
        getUserByIdUseCase(userId).onEach { result ->
            when(result) {
                is Resource.Success -> {
                    if (result.data != null){
                        _userState.value = UserState(data = result.data)
                    }
                }

                is Resource.Error -> {
                    _userState.value = UserState(error = result.message ?: "An unexpected error occurred.")
                }

                is Resource.Loading -> {
                    _userState.value = UserState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }
    private fun getMarketplaceItemsByUserId(userId: String) {
        getMarketplaceItemsByUserIdUseCase(userId).onEach { result ->
            when(result) {
                is Resource.Success -> {
                    if (result.data != null){
                        _listingState.value = UserState(data = result.data)
                    }
                }

                is Resource.Error -> {
                    _listingState.value = UserState(error = result.message ?: "An unexpected error occurred.")
                }

                is Resource.Loading -> {
                    _listingState.value = UserState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }
    private fun getRecipesByUserId(userId: String) {
        getUserRecipesUseCase(userId).onEach { result ->
            when(result) {
                is Resource.Success -> {
                    if (result.data != null){
                        _recipesState.value = UserState(data = result.data)
                    }
                }

                is Resource.Error -> {
                    _recipesState.value = UserState(error = result.message ?: "An unexpected error occurred.")
                }

                is Resource.Loading -> {
                    _recipesState.value = UserState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun updateBanner(bannerUrl: String) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        userId?.let {
            val userRef = db.collection("users").document(userId)
            val prefs = hashMapOf<String, Any>(
                "bannerUrl" to bannerUrl
            )
            userRef.update(prefs)
                .addOnSuccessListener {
                    getUserById(userId)
                    Log.d("EDIT_PROFILE_BANNER", "User banner updated")
                }
                .addOnFailureListener { exception ->
                    Log.d("EDIT_PROFILE_BANNER", "Error changing banner: $exception")
                }
        }
    }

/*    private fun getUserLikedRecipesDepreciated(userId: String) {
        getUserLikedRecipesUseCase(userId).onEach { result ->
            when(result) {
                is Resource.Success -> {
                    if (result.data != null){
                        _userLikedRecipes.value = UserScreenState(data = result.data)
                    }
                }

                is Resource.Error -> {
                    _userLikedRecipes.value = UserScreenState(error = result.message ?: "An unexpected error occurred.")
                }

                is Resource.Loading -> {
                    _userLikedRecipes.value = UserScreenState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }*/

    private fun getUserLikedRecipes() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val userRef = userId?.let { db.collection("users").document(userId) }
        viewModelScope.launch {
            try {
                val userSnapshot = userRef?.get()?.await()
                val likedRecipeIds = userSnapshot?.let { snapshot ->
                    snapshot["likedRecipeIds"] as? List<String> ?: emptyList()
                } ?: emptyList()
                val recipeDataList = mutableListOf<HashMap<String, Object>>()
                for (recipeId in likedRecipeIds) {
                    val recipeRef = db.collection("recipes").document(recipeId)
                    try {
                        val recipeSnapshot = recipeRef.get().await()
                        if (recipeSnapshot.exists()) {
                            val recipeData = recipeSnapshot.data
                            recipeData?.let { recipeDataList.add(it as HashMap<String, Object>) }
                        }
                    } catch (e: Exception) {
                        Log.d("FETCH_RECIPE", "Error fetching recipe with ID: $recipeId")
                    }
                }
                val recipeDtoList = recipeDataList.map { RecipeDto.from(it) }
                val recipes = mutableListOf<Recipe>()
                for (recipeDto in recipeDtoList) {
                    val recipe = recipeDto.toRecipe()
                    recipes.add(recipe)
                }
                _userLikedRecipes.value = recipes.toMutableList()
            } catch (e: Exception) {
                Log.d("GET_USER_LIKED_RECIPES", "Error retrieving user's liked recipes: ${e.message}")
            }
        }
    }
}