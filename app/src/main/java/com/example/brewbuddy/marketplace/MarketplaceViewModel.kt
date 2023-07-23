package com.example.brewbuddy.marketplace

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.brewbuddy.common.Resource
import com.example.brewbuddy.domain.use_case.get_marketplace_items.GetMarketplaceItemsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class MarketplaceViewModel @Inject constructor(
    private val getMarketplaceItemsUseCase: GetMarketplaceItemsUseCase
): ViewModel() {

    private val _marketplaceState = mutableStateOf(MarketplaceState())
    val marketplaceState: State<MarketplaceState> = _marketplaceState

    init {
        getMarketplaceItems()
    }

    private fun getMarketplaceItems() {
        getMarketplaceItemsUseCase().onEach { result ->
            when(result) {
                is Resource.Success -> {
                    _marketplaceState.value = MarketplaceState(data = result.data ?: emptyList())
                }
                is Resource.Error -> {
                    _marketplaceState.value = MarketplaceState(error = result.message ?: "An unexpected error occurred.")
                }
                is Resource.Loading -> {
                    _marketplaceState.value = MarketplaceState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }
}
