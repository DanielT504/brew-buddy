package com.example.brewbuddy.marketplace

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.brewbuddy.common.Resource
import com.example.brewbuddy.common.createQueryString
import com.example.brewbuddy.domain.model.MarketplaceItemMetadata
import com.example.brewbuddy.domain.model.SearchResultState
import com.example.brewbuddy.domain.use_case.get_marketplace.GetMarketplaceItemsUseCase
import com.example.brewbuddy.recipes.SearchViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class MarketplaceViewModel @Inject constructor(
    private val getMarketplaceItemsUseCase: GetMarketplaceItemsUseCase,
    savedStateHandle: SavedStateHandle
): SearchViewModel<MarketplaceItemMetadata>(savedStateHandle) {

    init {
        getResults()
    }
    override fun search() {
        _query.value = createQueryString(_search.value, _filters)

        getResults(_query.value)
    }
    private fun getResults(query: String = "") {
        getMarketplaceItemsUseCase(query).onEach { result ->
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
