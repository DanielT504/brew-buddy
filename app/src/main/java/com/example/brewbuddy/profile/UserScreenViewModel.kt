package com.example.brewbuddy.recipes

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.brewbuddy.common.Constants
import com.example.brewbuddy.common.Resource
import com.example.brewbuddy.domain.model.MarketplaceItemMetadata
import com.example.brewbuddy.domain.model.RecipeMetadata
import com.example.brewbuddy.domain.use_case.get_marketplace.GetMarketplaceItemsByUserIdUseCase
import com.example.brewbuddy.domain.use_case.get_recipes.GetUserRecipesUseCase
import com.example.brewbuddy.profile.UserScreenState
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class UserScreenViewModel  @Inject constructor(
    private val getUserRecipesUseCase: GetUserRecipesUseCase,
    private val getMarketplaceItemsByUserIdUseCase: GetMarketplaceItemsByUserIdUseCase,
    savedStateHandle: SavedStateHandle
): ViewModel(){
    private val _state = mutableStateOf(UserScreenState<RecipeMetadata>())
    val state: State<UserScreenState<RecipeMetadata>> = _state

    private val _listingState = mutableStateOf(UserScreenState<MarketplaceItemMetadata>())
    val listingState: State<UserScreenState<MarketplaceItemMetadata>> = _listingState

    private val userId = FirebaseAuth.getInstance().currentUser!!.uid

    init {
        getRecipesByUserId(userId)
        getMarketplaceItemsByUserId(userId)
    }

    private fun getMarketplaceItemsByUserId(userId: String) {
        getMarketplaceItemsByUserIdUseCase(userId).onEach { result ->
            when(result) {
                is Resource.Success -> {
                    if (result.data != null){
                        _listingState.value = UserScreenState(data = result.data)
                    }
                }

                is Resource.Error -> {
                    _listingState.value = UserScreenState(error = result.message ?: "An unexpected error occurred.")
                }

                is Resource.Loading -> {
                    _listingState.value = UserScreenState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)

    }
    private fun getRecipesByUserId(userId: String) {
        getUserRecipesUseCase(userId).onEach { result ->
            when(result) {
                is Resource.Success -> {
                    if (result.data != null){
                        Log.d("getrecipes result banner", result.data[0].bannerUrl)
                        _state.value = UserScreenState(data = result.data)
                    }
                }

                is Resource.Error -> {
                    _state.value = UserScreenState(error = result.message ?: "An unexpected error occurred.")
                }

                is Resource.Loading -> {
                    _state.value = UserScreenState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }
}