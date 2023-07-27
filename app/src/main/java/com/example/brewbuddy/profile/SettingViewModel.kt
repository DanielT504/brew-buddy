package com.example.brewbuddy.recipes

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.brewbuddy.common.Resource
import com.example.brewbuddy.domain.model.Preferences
import com.example.brewbuddy.domain.use_case.get_account.GetUserPreferenceUseCase
import com.example.brewbuddy.domain.use_case.get_account.SetImageUploadByTypeUseCase
import com.example.brewbuddy.domain.use_case.get_account.SetUserPreferenceUseCase
import com.example.brewbuddy.profile.PreferenceState
import com.example.brewbuddy.profile.UploadState
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val getUserPreferenceUseCase: GetUserPreferenceUseCase,
    private val setUserPreferenceUseCase: SetUserPreferenceUseCase,
    private val setImageUploadByTypeUseCase: SetImageUploadByTypeUseCase,
    savedStateHandle: SavedStateHandle
): ViewModel(){
    private val _preferenceState = mutableStateOf(PreferenceState())
    val preferenceState: State<PreferenceState> = _preferenceState

    private val _avatarState = mutableStateOf(UploadState())
    val avatarState: State<UploadState> = _avatarState


    private val _bannerState = mutableStateOf(UploadState())
    val bannerState: State<UploadState> = _bannerState

    private val userId = FirebaseAuth.getInstance().currentUser!!.uid

    init {
        getPreferencesById(userId)
    }
    private fun getPreferencesById(userId: String) {
        getUserPreferenceUseCase(userId).onEach { result ->
            when(result) {
                is Resource.Success -> {
                    if (result.data != null){
                        Log.d("getPreferencesById", result.data.toString())
                        _preferenceState.value = PreferenceState(data = result.data)
                    }
                }

                is Resource.Error -> {
                    _preferenceState.value = PreferenceState(error = result.message ?: "An unexpected error occurred.")
                }

                is Resource.Loading -> {
                    _preferenceState.value = PreferenceState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun updatePreferencesById(
        radius: Float,
        vegan: Boolean,
        vegetarian: Boolean,
        dairyFree: Boolean,
        keto: Boolean,
        kosher: Boolean,
        halal: Boolean,
        glutenFree: Boolean,
        nutFree: Boolean,
        ingredients: List<String>
    ) {
        val newPreferences = Preferences(
            radius=radius,
            vegan=vegan,
            vegetarian = vegetarian,
            dairyFree = dairyFree,
            keto=keto,
            kosher=kosher,
            halal=halal,
            nutFree = nutFree,
            glutenFree = glutenFree,
            ingredients = ingredients
        )

        setUserPreferenceUseCase(userId, newPreferences).onEach { result ->
            when(result) {
                is Resource.Success -> {
                    if (result.data ?: false){
                        Log.d("setUserPreferenceUseCase", "Success")
                        _preferenceState.value = PreferenceState(data = newPreferences)
                    }
                }

                is Resource.Error -> {
                    _preferenceState.value = PreferenceState(error = result.message ?: "An unexpected error occurred.")
                }

                is Resource.Loading -> {
                    _preferenceState.value = PreferenceState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }


    fun uploadImageById(uri: Uri, type: String) {
        setImageUploadByTypeUseCase(userId, uri, type).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    if (result.data != null) {
                        Log.d("uploadImageById", "Uploading ${type} success")
                        if(type === "AVATAR") {
                            _avatarState.value = UploadState()
                        } else {
                            _bannerState.value = UploadState()
                        }
                    }
                }

                is Resource.Error -> {
                    if(type === "AVATAR") {
                        _avatarState.value =
                            UploadState(error = result.message ?: "An unexpected error occurred.")
                    } else {
                        _bannerState.value =
                            UploadState(error = result.message ?: "An unexpected error occurred.")
                    }
                }

                is Resource.Loading -> {
                    if(type === "AVATAR") {
                        _avatarState.value = UploadState(isLoading = true)
                    } else {
                        _bannerState.value = UploadState(isLoading = true)
                    }
                }
            }
        }.launchIn(viewModelScope)
    }
}