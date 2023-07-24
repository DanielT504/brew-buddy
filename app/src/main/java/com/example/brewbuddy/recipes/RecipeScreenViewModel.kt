package com.example.brewbuddy.recipes

import RecipeResultsState
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.brewbuddy.common.Constants
import com.example.brewbuddy.common.Resource
import com.example.brewbuddy.common.createQueryString
import com.example.brewbuddy.domain.model.RecipeMetadata
import com.example.brewbuddy.domain.model.SearchResultState
import com.example.brewbuddy.domain.use_case.get_recipes.GetRecipeResultsUseCase
import com.example.brewbuddy.getUser
import com.example.brewbuddy.marketplace.Filter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class RecipeScreenViewModel  @Inject constructor(
    private val getRecipeResultsUseCase: GetRecipeResultsUseCase,
    savedStateHandle: SavedStateHandle
): SearchViewModel<RecipeMetadata>(savedStateHandle) {

    init {
        getResults("")
    }
    override fun search() {
        _query.value = createQueryString(_search.value, _filters)

        getResults(_query.value)
    }

    private fun getResults(query: String) {
        getRecipeResultsUseCase(query).onEach { result ->
            when(result) {
                is Resource.Success -> {
                    _state.value = SearchResultState(results = result.data ?: emptyList())
                }
                is Resource.Error -> {
                    _state.value = SearchResultState(error = result.message ?: "An unexpected error occurred.")
                }
                is Resource.Loading -> {
                    _state.value = SearchResultState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

}