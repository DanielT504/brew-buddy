package com.example.brewbuddy.domain.use_case.get_account

import com.example.brewbuddy.common.Resource
import com.example.brewbuddy.domain.repository.RecipeRepository
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetSignInWithUsernameUseCase @Inject constructor(
    private val repository: RecipeRepository
) {
    operator fun invoke(username: String, password: String): Flow<Resource<Boolean>> = flow {
        try {
            emit(Resource.Loading())
            val success = repository.signInWithUsername(username, password)
            emit(Resource.Success(success))
        } catch(e: FirebaseAuthInvalidCredentialsException) {
            emit(Resource.Error(e.localizedMessage ?: "Invalid username or password."))
        }catch(e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred."))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection."))
        }
    }
}