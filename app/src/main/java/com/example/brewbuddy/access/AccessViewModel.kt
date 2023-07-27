package com.example.brewbuddy.access

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.brewbuddy.common.Resource
import com.example.brewbuddy.domain.use_case.get_account.GetSignInWithGoogleUseCase
import com.example.brewbuddy.domain.use_case.get_account.GetSignInWithUsernameUseCase
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject


// global state view model to get currently logged in user.`
// if this is null, then we are looking at the login screen
@HiltViewModel

class AccessViewModel @Inject constructor(
    private val getSignInWithUsernameUseCase: GetSignInWithUsernameUseCase,
    private val getSignInWithGoogleUseCase: GetSignInWithGoogleUseCase,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _loginState = mutableStateOf(AccessState())
    val loginState: State<AccessState> = _loginState

    private val auth = Firebase.auth
    private val db = FirebaseFirestore.getInstance()


    fun signIn(username: String, password: String, onSuccess: () -> Unit) {
        getSignInWithUsernameUseCase(username, password).onEach { result ->
            when(result) {
                is Resource.Success -> {
                    _loginState.value = AccessState(success = result.data ?: false)
                    onSuccess()
                }
                is Resource.Error -> {
                    _loginState.value = AccessState(error = result.message ?: "An unexpected error occurred.")
                }
                is Resource.Loading -> {
                    _loginState.value = AccessState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }
    fun registerUserWithGoogle(context: Context, username: String, email: String, onSuccess: () -> Unit) {
        getSignInWithGoogleUseCase(context, username, email).onEach { result ->
            when(result) {
                is Resource.Success -> {
                    _loginState.value = AccessState(success = result.data ?: false)
                    if(result.data ?: false) {
                        onSuccess()
                    }
                }
                is Resource.Error -> {
                    _loginState.value = AccessState(error = result.message ?: "An unexpected error occurred.")
                }
                is Resource.Loading -> {
                    _loginState.value = AccessState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }
}