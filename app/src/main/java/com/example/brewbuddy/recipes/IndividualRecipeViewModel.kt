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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class IndividualRecipeScreenViewModel  @Inject constructor(
    private val getRecipeUseCase: GetRecipeUseCase,
    savedStateHandle: SavedStateHandle
): ViewModel(){
    private val _state = mutableStateOf(RecipeState())
    val state: State<RecipeState> = _state

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

}