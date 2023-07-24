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
import com.example.brewbuddy.common.Resource
import com.example.brewbuddy.common.createQueryString
import com.example.brewbuddy.domain.model.SearchResultState
import com.example.brewbuddy.domain.use_case.get_recipes.GetRecipeResultsUseCase
import com.example.brewbuddy.marketplace.Filter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

open class SearchViewModel<T>(
    savedStateHandle: SavedStateHandle
): ViewModel() {
    protected val _filters = mutableStateListOf<Filter>()
    val filters: SnapshotStateList<Filter> = _filters

    protected val _search = mutableStateOf(String())
    val search: State<String> = _search

    protected val _state = mutableStateOf(SearchResultState<T>())
    val state: State<SearchResultState<T>> = _state


    fun setKeywords(str: String) {
        _search.value = str
    }

    fun removeFilter(filter: Filter) {
        _filters.remove(filter)

    }
    fun addFilter(filter: Filter) {
        _filters.add(filter)

    }

    protected val _query = mutableStateOf(String())
    val query: State<String> = _query

    open fun search() {}
}