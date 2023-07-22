package com.example.brewbuddy.featured

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.brewbuddy.common.Resource
import com.example.brewbuddy.domain.use_case.get_recipes.GetPopularUseCase
import com.example.brewbuddy.domain.use_case.get_recipes.GetRecipesUseCase
import com.example.brewbuddy.featured.FeaturedState
import com.example.brewbuddy.getUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class FeaturedViewModel @Inject constructor(
    private val getRecipesUseCase: GetRecipesUseCase,
    private val getPopularUseCase: GetPopularUseCase,
): ViewModel() {

    private val _recipeState = mutableStateOf(FeaturedState())
    private val _popularState = mutableStateOf(FeaturedState())

    val recipeState: State<FeaturedState> = _recipeState
    val popularState: State<FeaturedState> = _popularState


    init {
        getPopular()
        getRecipes()
    }

    private fun getPopular() {
        getPopularUseCase().onEach { result ->
            when(result) {
                is Resource.Success -> {
                    _popularState.value = FeaturedState(data = result.data ?: emptyList())
                }
                is Resource.Error -> {
                    _popularState.value = FeaturedState(error = result.message ?: "An unexpected error occurred.")
                }
                is Resource.Loading -> {
                    _popularState.value = FeaturedState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun getRecipes() {
        getRecipesUseCase().onEach { result ->
            when(result) {
                is Resource.Success -> {
                    _recipeState.value = FeaturedState(data = result.data ?: emptyList())
                }
                is Resource.Error -> {
                    _recipeState.value = FeaturedState(error = result.message ?: "An unexpected error occurred.")
                }
                is Resource.Loading -> {
                    _recipeState.value = FeaturedState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }
}
