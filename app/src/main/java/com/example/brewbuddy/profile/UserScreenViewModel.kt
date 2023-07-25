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
import com.example.brewbuddy.domain.model.User
import com.example.brewbuddy.domain.use_case.get_account.GetUserByIdUseCase
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
    private val getUserByIdUseCase: GetUserByIdUseCase,
    private val getMarketplaceItemsByUserIdUseCase: GetMarketplaceItemsByUserIdUseCase,
    savedStateHandle: SavedStateHandle
): ViewModel(){
    private val _recipesState = mutableStateOf(UserScreenState<List<RecipeMetadata>>())
    val recipesState: State<UserScreenState<List<RecipeMetadata>>> = _recipesState

    private val _listingState = mutableStateOf(UserScreenState<List<MarketplaceItemMetadata>>())
    val listingState: State<UserScreenState<List<MarketplaceItemMetadata>>> = _listingState

    private val _userState = mutableStateOf(UserScreenState<User>())
    val userState: State<UserScreenState<User>> = _userState

    private val userId = FirebaseAuth.getInstance().currentUser!!.uid

    init {
        getUserById(userId)
        getRecipesByUserId(userId)
        getMarketplaceItemsByUserId(userId)
    }
    private fun getUserById(userId: String) {
        getUserByIdUseCase(userId).onEach { result ->
            when(result) {
                is Resource.Success -> {
                    if (result.data != null){
                        _userState.value = UserScreenState(data = result.data)
                    }
                }

                is Resource.Error -> {
                    _userState.value = UserScreenState(error = result.message ?: "An unexpected error occurred.")
                }

                is Resource.Loading -> {
                    _userState.value = UserScreenState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
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
                        _recipesState.value = UserScreenState(data = result.data)
                    }
                }

                is Resource.Error -> {
                    _recipesState.value = UserScreenState(error = result.message ?: "An unexpected error occurred.")
                }

                is Resource.Loading -> {
                    _recipesState.value = UserScreenState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }
}