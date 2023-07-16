package com.example.brewbuddy.recipes

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.brewbuddy.common.Resource
import com.example.brewbuddy.domain.use_case.get_recipes.GetRecipesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class RecipesScreenViewModel @Inject constructor(
    private val getRecipesUseCase: GetRecipesUseCase
): ViewModel() {

    private val _state = mutableStateOf(RecipesState())
    val state: State<RecipesState> = _state

    init {
        getRecipes()
    }

    private fun getRecipes() {
        getRecipesUseCase().onEach { result ->
            when(result) {
                is Resource.Success -> {
                    _state.value = RecipesState(recipes = result.data ?: emptyList())
                }
                is Resource.Error -> {
                    _state.value = RecipesState(error = result.message ?: "An unexpected error occurred.")
                }
                is Resource.Loading -> {
                    _state.value = RecipesState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }
}
