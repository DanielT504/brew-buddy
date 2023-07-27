package com.example.brewbuddy.domain.use_case.get_account

import com.example.brewbuddy.common.Resource
import com.example.brewbuddy.data.remote.dto.toPreferences
import com.example.brewbuddy.data.remote.dto.toUser
import com.example.brewbuddy.domain.model.Preferences
import com.example.brewbuddy.domain.model.User
import com.example.brewbuddy.domain.repository.RecipeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class SetUserPreferenceUseCase @Inject constructor(
    private val repository: RecipeRepository
) {
    operator fun invoke(userId: String, newPreferences: Preferences): Flow<Resource<Boolean>> = flow {
        try {
            emit(Resource.Loading())
            val result = repository.setUserPreferencesById(userId, newPreferences)
            emit(Resource.Success(result))
        } catch(e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred."))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection."))
        }
    }
}