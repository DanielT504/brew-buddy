package com.example.brewbuddy.profile

import android.content.Context
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await
import com.example.brewbuddy.common.Constants
import com.example.brewbuddy.common.Resource
import com.example.brewbuddy.domain.model.PostState
import com.example.brewbuddy.domain.use_case.get_account.GetRegisterWithGoogleUseCase
import com.example.brewbuddy.domain.use_case.get_account.GetSignInUseCase
import com.example.brewbuddy.domain.use_case.get_recipes.GetRecipeUseCase
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

class AccountViewModel @Inject constructor(
    private val getSignInUseCase: GetSignInUseCase,
    private val getRegisterWithGoogleUseCase: GetRegisterWithGoogleUseCase,
    savedStateHandle: SavedStateHandle
): ViewModel() {
//    val currentUser =  MutableLiveData<User?>()
    private val _loginState = mutableStateOf(AccountState())
    val loginState: State<AccountState> = _loginState

    private val auth = Firebase.auth
    val db = FirebaseFirestore.getInstance()


    fun signIn(username: String, password: String, onSuccess: () -> Unit) {
        getSignInUseCase(username, password).onEach { result ->
            when(result) {
                is Resource.Success -> {
                    _loginState.value = AccountState(success = result.data ?: false)
                    if(result.data ?: false) {
                        onSuccess()
                    }
                }
                is Resource.Error -> {
                    _loginState.value = AccountState(error = result.message ?: "An unexpected error occurred.")
                }
                is Resource.Loading -> {
                    _loginState.value = AccountState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }
    fun registerUserWithGoogle(context: Context, username: String, email: String, onSuccess: () -> Unit) {
        getRegisterWithGoogleUseCase(context, username, email).onEach { result ->
            when(result) {
                is Resource.Success -> {
                    _loginState.value = AccountState(success = result.data ?: false)
                    onSuccess()
                }
                is Resource.Error -> {
                    _loginState.value = AccountState(error = result.message ?: "An unexpected error occurred.")
                }
                is Resource.Loading -> {
                    _loginState.value = AccountState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }
}