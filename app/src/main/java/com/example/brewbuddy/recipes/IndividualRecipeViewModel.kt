package com.example.brewbuddy.recipes

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.brewbuddy.common.Constants
import com.example.brewbuddy.common.Resource
import com.example.brewbuddy.domain.use_case.get_recipes.GetRecipeUseCase
import com.example.brewbuddy.profile.db
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class IndividualRecipeScreenViewModel  @Inject constructor(
    private val getRecipeUseCase: GetRecipeUseCase,
    savedStateHandle: SavedStateHandle
): ViewModel(){
    private val _state = mutableStateOf(RecipeState())
    val state: State<RecipeState> = _state

    private val _previouslyLiked = mutableStateOf(false)
    val previouslyLiked: State<Boolean> = _previouslyLiked

    init {
        Log.d("IndividualRecipeScreenViewModel", savedStateHandle.toString())
        savedStateHandle.get<String>(Constants.PARAM_RECIPE_ID)?.let { recipeId ->
            Log.d("IndividualRecipeScreenViewModel", recipeId)
            val id = recipeId.substringAfter("}")
            getRecipeById(id)
            getUserLikedRecipes(id)
        }
    }

    fun getRecipeById(recipeId: String) {
        getRecipeUseCase(recipeId).onEach { result ->
            when(result) {
                is Resource.Success -> {
                    _state.value = RecipeState(recipe = result.data)
                }
                is Resource.Error -> {
                    _state.value = RecipeState(error = result.message ?: "An unexpected error occurred.")
                }
                is Resource.Loading -> {
                    _state.value = RecipeState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun getUserLikedRecipes(id: String){
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val userRef = userId?.let { db.collection("users").document(userId) }
        var likedRecipes = listOf<String>()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val snapshot = userRef?.get()?.await()
                if (snapshot != null) {
                    likedRecipes = (snapshot.get("likedRecipeIds")?: "") as List<String>;
                    Log.d("LIST OF USER LIKED RECIPES", "$likedRecipes")
                    var test = likedRecipes.contains(id)
                    Log.d("TESTING", "$test")
                }
            }  catch (e: Exception) {
                Log.d("GET_USER_LIKED_RECIPES", "Error retrieving user's liked recipes")
            }
        }
        _previouslyLiked.value = likedRecipes.contains(id)
    }

}