package com.example.brewbuddy.recipes

import android.content.Context
import android.util.Log
import androidx.compose.runtime.State
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.brewbuddy.common.Constants
import com.example.brewbuddy.common.Resource
import com.example.brewbuddy.domain.use_case.get_recipes.GetRecipeUseCase
import com.example.brewbuddy.profile.db
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import dagger.hilt.android.lifecycle.HiltViewModel
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

    private val _isFavourite = mutableStateOf<Boolean>(false)
    val isFavourite: State<Boolean> get() = _isFavourite

    init {
        Log.d("IndividualRecipeScreenViewModel", savedStateHandle.toString())
        savedStateHandle.get<String>(Constants.PARAM_RECIPE_ID)?.let { recipeId ->
            Log.d("IndividualRecipeScreenViewModel", recipeId)
            val id = recipeId.substringAfter("}")
            getRecipeById(id)
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

    fun checkFavouriteFromDatabase(id: String) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val userRef = userId?.let { db.collection("users").document(userId) }
        var likedRecipes = listOf<String>()

        viewModelScope.launch {
                try {
                    val snapshot = userRef?.get()?.await()
                    if (snapshot != null) {
                        likedRecipes = (snapshot.get("likedRecipeIds") ?: "") as List<String>;
                        Log.d("LIST OF USER LIKED RECIPES", "$likedRecipes")
                        var test = likedRecipes.contains(id)
                        Log.d("TESTING", "$test")
                    }
                } catch (e: Exception) {
                    Log.d("GET_USER_LIKED_RECIPES", "Error retrieving user's liked recipes")
                }
            _isFavourite.value = likedRecipes.contains(id)
        }
    }

    fun toggleFavourite(recipeId: String, applicationContext: Context) {
        val isCurrentlyFavourited = _isFavourite.value
        _isFavourite.value = !isCurrentlyFavourited

        viewModelScope.launch {
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            userId?.let {
                val userRef = db.collection("users").document(userId)
                val recipeRef = db.collection("recipes").document(recipeId)
                if (!isCurrentlyFavourited) {
                    userRef.update("likedRecipeIds", FieldValue.arrayUnion(recipeId))
                        .addOnSuccessListener {
                            Log.d("LIKED_RECIPE", "User liked recipe $recipeId")
                        }
                        .addOnFailureListener { exception ->
                            Log.d("LIKED_RECIPE", "Error liking recipe $recipeId: $exception")
                        }
                    recipeRef.update("likes", FieldValue.increment(1))
                        .addOnSuccessListener {
                                Log.d("UPDATED_RECIPE_LIKES", "$recipeId")
                        }
                        .addOnFailureListener { exception ->
                            Log.d("UNABLE_TO_UPDATE_RECIPE_LIKES", "$recipeId: $exception")
                        }
                    Toast.makeText(applicationContext, "Added to favourites",  Toast.LENGTH_SHORT).show()
                }
                if (isCurrentlyFavourited) {
                    userRef.update("likedRecipeIds", FieldValue.arrayRemove(recipeId))
                        .addOnSuccessListener {
                            Log.d("UNLIKED_RECIPE", "User unliked recipe $recipeId")
                        }
                        .addOnFailureListener { exception ->
                            Log.d("UNLIKED_RECIPE", "Error unliking recipe $recipeId: $exception")
                        }
                    recipeRef.update("likes", FieldValue.increment(-1))
                        .addOnSuccessListener {
                            Log.d("UPDATED_RECIPE_LIKES", "$recipeId")
                        }
                        .addOnFailureListener { exception ->
                            Log.d("UNABLE_TO_UPDATE_RECIPE_LIKES", "$recipeId: $exception")
                        }
                    Toast.makeText(applicationContext, "Removed from favourites",  Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

}