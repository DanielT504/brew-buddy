package com.example.brewbuddy.featured

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.brewbuddy.common.Resource
import com.example.brewbuddy.domain.use_case.get_recipes.GetRecipesUseCase
import com.example.brewbuddy.featured.FeaturedState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class FeaturedViewModel @Inject constructor(
    private val getRecipesUseCase: GetRecipesUseCase,
    private val getPopularUseCase: GetRecipesUseCase,
): ViewModel() {

    private val _state = mutableStateOf(FeaturedState())
    val state: State<FeaturedState> = _state

    init {
        getPopular()
        getRecipes()
    }

    private fun getPopular() {
        getPopularUseCase().onEach { result ->
            when(result) {
                is Resource.Success -> {
                    _state.value = FeaturedState(popular = result.data ?: emptyList())
                }
                is Resource.Error -> {
                    _state.value = FeaturedState(popularError = result.message ?: "An unexpected error occurred.")
                }
                is Resource.Loading -> {
                    _state.value = FeaturedState(isPopularLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun getRecipes() {
        getRecipesUseCase().onEach { result ->
            when(result) {
                is Resource.Success -> {
                    _state.value = FeaturedState(recipes = result.data ?: emptyList())
                }
                is Resource.Error -> {
                    _state.value = FeaturedState(recipesError = result.message ?: "An unexpected error occurred.")
                }
                is Resource.Loading -> {
                    _state.value = FeaturedState(isRecipesLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }
}
