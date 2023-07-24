package com.example.brewbuddy.marketplace

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.brewbuddy.common.Constants
import com.example.brewbuddy.common.Resource
import com.example.brewbuddy.domain.model.MarketplaceItem
import com.example.brewbuddy.domain.model.PostState
import com.example.brewbuddy.domain.model.SearchResultState
import com.example.brewbuddy.domain.use_case.get_marketplace.GetMarketplaceItemUseCase
import com.example.brewbuddy.templates.PostViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class MarketplaceItemViewModel  @Inject constructor(
    private val getMarketplaceItemUseCase: GetMarketplaceItemUseCase,
  savedStateHandle: SavedStateHandle
): PostViewModel<MarketplaceItem>(Constants.PARAM_MARKETPLACE_ITEM_ID, savedStateHandle) {

    init {
        fetchPost()
    }

    override fun getPostById(postId: String) {
        getMarketplaceItemUseCase(postId).onEach { result ->
            when(result) {
                is Resource.Success -> {
                    _state.value = PostState(post = result.data)
                }
                is Resource.Error -> {
                    _state.value = PostState(error = result.message ?: "An unexpected error occurred.")
                }
                is Resource.Loading -> {
                    _state.value = PostState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)

    }
}