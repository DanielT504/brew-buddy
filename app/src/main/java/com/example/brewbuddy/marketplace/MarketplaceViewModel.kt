package com.example.brewbuddy.marketplace

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.brewbuddy.common.Resource
import com.example.brewbuddy.data.remote.dto.MarketplaceItemMetadata
import com.example.brewbuddy.domain.model.SearchResultState
import com.example.brewbuddy.domain.use_case.get_marketplace_items.GetMarketplaceItemsUseCase
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
        getMarketplaceItems()
    }

    private fun getMarketplaceItems() {
        getMarketplaceItemsUseCase().onEach { result ->
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
