package com.example.brewbuddy.domain.use_case.get_account

import com.example.brewbuddy.common.Resource
import com.example.brewbuddy.data.remote.dto.toRecipeMetadata
import com.example.brewbuddy.domain.model.RecipeMetadata
import com.example.brewbuddy.domain.repository.RecipeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetSignInUseCase @Inject constructor(
    private val repository: RecipeRepository
) {
    operator fun invoke(username: String, password: String): Flow<Resource<Boolean>> = flow {
        try {
            emit(Resource.Loading())
            val success = repository.signIn(username, password)
            emit(Resource.Success(success))
        } catch(e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred."))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection."))
        }
    }
}