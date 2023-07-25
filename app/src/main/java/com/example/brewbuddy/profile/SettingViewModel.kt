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
import com.example.brewbuddy.domain.model.Preferences
import com.example.brewbuddy.domain.model.RecipeMetadata
import com.example.brewbuddy.domain.model.User
import com.example.brewbuddy.domain.use_case.get_account.GetUserByIdUseCase
import com.example.brewbuddy.domain.use_case.get_account.GetUserPreferenceUseCase
import com.example.brewbuddy.domain.use_case.get_account.SetUserPreferenceUseCase
import com.example.brewbuddy.domain.use_case.get_marketplace.GetMarketplaceItemsByUserIdUseCase
import com.example.brewbuddy.domain.use_case.get_recipes.GetUserRecipesUseCase
import com.example.brewbuddy.domain.use_case.get_user_liked_recipes.GetUserLikedRecipesUseCase
import com.example.brewbuddy.profile.PreferenceState
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
class SettingViewModel @Inject constructor(
    private val getUserPreferenceUseCase: GetUserPreferenceUseCase,
    private val setUserPreferenceUseCase: SetUserPreferenceUseCase,
    savedStateHandle: SavedStateHandle
): ViewModel(){
    private val _preferenceState = mutableStateOf(PreferenceState())
    val preferenceState: State<PreferenceState> = _preferenceState


    private val userId = FirebaseAuth.getInstance().currentUser!!.uid

    init {
        getPreferencesById(userId)
    }
    private fun getPreferencesById(userId: String) {
        getUserPreferenceUseCase(userId).onEach { result ->
            when(result) {
                is Resource.Success -> {
                    if (result.data != null){
                        Log.d("getPreferencesById", result.data.toString())
                        _preferenceState.value = PreferenceState(data = result.data)
                    }
                }

                is Resource.Error -> {
                    _preferenceState.value = PreferenceState(error = result.message ?: "An unexpected error occurred.")
                }

                is Resource.Loading -> {
                    _preferenceState.value = PreferenceState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun updatePreferencesById(
        radius: Float,
        vegan: Boolean,
        vegetarian: Boolean,
        dairyFree: Boolean,
        keto: Boolean,
        kosher: Boolean,
        halal: Boolean,
        glutenFree: Boolean,
        nutFree: Boolean,
        ingredients: List<String>
    ) {
        val newPreferences = Preferences(
            radius=radius,
            vegan=vegan,
            vegetarian = vegetarian,
            dairyFree = dairyFree,
            keto=keto,
            kosher=kosher,
            halal=halal,
            nutFree = nutFree,
            glutenFree = glutenFree,
            ingredients = ingredients
        )

        setUserPreferenceUseCase(userId, newPreferences).onEach { result ->
            when(result) {
                is Resource.Success -> {
                    if (result.data ?: false){
                        Log.d("setUserPreferenceUseCase", "Success")
                        _preferenceState.value = PreferenceState(data = newPreferences)
                    }
                }

                is Resource.Error -> {
                    _preferenceState.value = PreferenceState(error = result.message ?: "An unexpected error occurred.")
                }

                is Resource.Loading -> {
                    _preferenceState.value = PreferenceState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
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

//    private fun getUserLikedRecipes() {
//        val userId = FirebaseAuth.getInstance().currentUser?.uid
//        val userRef = userId?.let { db.collection("users").document(userId) }
//        viewModelScope.launch {
//            try {
//                val userSnapshot = userRef?.get()?.await()
//                val likedRecipeIds = userSnapshot?.let { snapshot ->
//                    snapshot["likedRecipeIds"] as? List<String> ?: emptyList()
//                } ?: emptyList()
//                val recipeDataList = mutableListOf<HashMap<String, Object>>()
//                for (recipeId in likedRecipeIds) {
//                    val recipeRef = db.collection("recipes").document(recipeId)
//                    try {
//                        val recipeSnapshot = recipeRef.get().await()
//                        if (recipeSnapshot.exists()) {
//                            val recipeData = recipeSnapshot.data
//                            recipeData?.let { recipeDataList.add(it as HashMap<String, Object>) }
//                        }
//                    } catch (e: Exception) {
//                        Log.d("FETCH_RECIPE", "Error fetching recipe with ID: $recipeId")
//                    }
//                }
//                val recipeDtoList = recipeDataList.map { RecipeDto.from(it) }
//                val recipes = mutableListOf<Recipe>()
//                for (recipeDto in recipeDtoList) {
//                    val recipe = recipeDto.toRecipe()
//                    recipes.add(recipe)
//                }
//                _userLikedRecipes.value = recipes.toMutableList()
//            } catch (e: Exception) {
//                Log.d("GET_USER_LIKED_RECIPES", "Error retrieving user's liked recipes: ${e.message}")
//            }
//        }
//    }
}