package com.example.brewbuddy.recipes

import RecipeResultsState
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.brewbuddy.common.Constants
import com.example.brewbuddy.common.Resource
import com.example.brewbuddy.domain.use_case.get_recipes.GetRecipeResultsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class RecipeResultsViewModel  @Inject constructor(
    private val getRecipeResultsUseCase: GetRecipeResultsUseCase,
    savedStateHandle: SavedStateHandle
): ViewModel(){
//    private val _state = mutableStateOf(RecipeResultsState())
//    val state: State<RecipeResultsState> = _state
//
//    init {
//        savedStateHandle.get<String>(Constants.PARAM_QUERY)?.let { query ->
//            Log.d("RecipeResultsViewModel", query)
//            val id = query.substringAfter("}")
//            getResults(id)
//        }
//    }
//
//    private fun getResults(query: String) {
//        getRecipeResultsUseCase(query).onEach { result ->
//            when(result) {
//                is Resource.Success -> {
//                    _state.value = RecipeResultsState(results = result.data)
//                }
//                is Resource.Error -> {
//                    _state.value = RecipeResultsState(error = result.message ?: "An unexpected error occurred.")
//                }
//                is Resource.Loading -> {
//                    _state.value = RecipeResultsState(isLoading = true)
//                }
//            }
//        }.launchIn(viewModelScope)
//    }

}