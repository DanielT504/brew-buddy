package com.example.brewbuddy.recipes

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.brewbuddy.common.Constants
import com.example.brewbuddy.common.Resource
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
    savedStateHandle: SavedStateHandle
): ViewModel(){
    private val _state = mutableStateOf(UserScreenState())
    val state: State<UserScreenState> = _state

    private val userId = FirebaseAuth.getInstance().currentUser!!.uid
//    init {
//        Log.d("UserScreenViewModel", savedStateHandle.toString())
//        savedStateHandle.get<String>(Constants.PARAM_USER_ID)?.let { userId ->
//            Log.d("UserScreenViewModel Value", userId.substringAfter("}"))
//            val userId = userId.substringAfter("}")
//            getRecipesByUserId(userId)
//        }
//    }

    init {
        getRecipesByUserId(userId)
    }

    private fun getRecipesByUserId(userId: String) {
        getUserRecipesUseCase(userId).onEach { result ->
            when(result) {
                is Resource.Success -> {
                    if (result.data != null){
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