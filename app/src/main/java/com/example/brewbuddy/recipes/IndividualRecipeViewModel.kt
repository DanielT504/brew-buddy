package com.example.brewbuddy.recipes

import android.content.Context
import android.util.Log
import androidx.compose.runtime.State
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.brewbuddy.common.Constants
import com.example.brewbuddy.common.Resource
import com.example.brewbuddy.domain.model.PostState
import com.example.brewbuddy.domain.model.Recipe
import com.example.brewbuddy.domain.use_case.get_recipes.GetRecipeUseCase
import com.example.brewbuddy.profile.db
import com.example.brewbuddy.templates.PostViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class IndividualRecipeScreenViewModel  @Inject constructor(
    private val getRecipeUseCase: GetRecipeUseCase,
    savedStateHandle: SavedStateHandle
): PostViewModel<Recipe>(Constants.PARAM_RECIPE_ID, savedStateHandle){
    private val _isFavourite = mutableStateOf<Boolean>(false)
    val isFavourite: State<Boolean> get() = _isFavourite

    private val _numberOfLikes = mutableStateOf(0)
    val numberOfLikes: State<Number> get() = _numberOfLikes

    private val _feedbackState = MutableLiveData<String>()
    val feedbackState: LiveData<String> get()= _feedbackState


    init {
        fetchPost()
    }

    fun onTextChanged(newText: String) {
        _feedbackState.value = newText
    }

    override fun getPostById(recipeId: String) {
        getRecipeUseCase(recipeId).onEach { result ->
            when(result) {
                is Resource.Success -> {
                    _state.value = PostState(post = result.data)
                    _numberOfLikes.value = result.data!!.likes
                }
                is Resource.Error -> {
                    _state.value = PostState(error = result.message ?: "An unexpected error occurred.")
                }
                is Resource.Loading -> {
                    _state.value = PostState(isLoading = true)
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
                    _numberOfLikes.value += 1
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
                    _numberOfLikes.value -= 1
                    Toast.makeText(applicationContext, "Removed from favourites",  Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun postRecipeFeedback(recipeId: String, applicationContext: Context) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val feedbackData = hashMapOf(
            "recipeId" to recipeId,
            "userId" to userId,
            "feedback" to _feedbackState
        )
        viewModelScope.launch {
            db.collection("feedback").add(feedbackData).addOnSuccessListener {
                Log.d("FEEDBACK_SUBMITTED", "Feedback submitted for $recipeId by $userId")
                _feedbackState.value = ""
                Toast.makeText(applicationContext, "Feedback submitted",  Toast.LENGTH_SHORT).show()
            }
        }
    }

}