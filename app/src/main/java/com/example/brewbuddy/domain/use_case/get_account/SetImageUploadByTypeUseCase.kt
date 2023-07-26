package com.example.brewbuddy.domain.use_case.get_account

import android.net.Uri
import com.example.brewbuddy.common.Resource
import com.example.brewbuddy.domain.repository.RecipeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import java.lang.Exception
import javax.inject.Inject

class SetImageUploadByTypeUseCase @Inject constructor(
    private val repository: RecipeRepository
) {
    operator fun invoke(userId: String, uri: Uri, type: String): Flow<Resource<Boolean>> = flow {
        try {
            emit(Resource.Loading())
            val result = repository.setImageUploadByType(userId, uri, type)
            emit(Resource.Success(result))
        } catch(e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred."))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection."))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Something broke."))
        }
    }
}
