package com.example.brewbuddy.domain.use_case.get_user_liked_recipes


import com.example.brewbuddy.common.Resource
import com.example.brewbuddy.data.remote.dto.toRecipeMetadata
import com.example.brewbuddy.domain.model.RecipeMetadata
import com.example.brewbuddy.domain.repository.RecipeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetUserLikedRecipesUseCase @Inject constructor(
    private val repository: RecipeRepository
) {
    operator fun invoke(userId: String): Flow<Resource<List<RecipeMetadata>>> = flow {
        try {
            emit(Resource.Loading())
            val recipes = repository.getUserLikedRecipes(userId).map { it.toRecipeMetadata() }
            emit(Resource.Success(recipes))
        } catch(e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred."))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection."))
        }
    }
}