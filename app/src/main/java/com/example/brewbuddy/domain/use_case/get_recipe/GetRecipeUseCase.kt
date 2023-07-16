package com.example.brewbuddy.domain.use_case.get_recipes

import com.example.brewbuddy.common.Resource
import com.example.brewbuddy.data.remote.dto.RecipeById.toRecipe
import com.example.brewbuddy.data.remote.dto.toRecipe
import com.example.brewbuddy.domain.model.Recipe
import com.example.brewbuddy.domain.repository.RecipeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetRecipeUseCase @Inject constructor(
    private val repository: RecipeRepository
) {
    operator fun invoke(recipeId: String): Flow<Resource<Recipe>> = flow {
        try {
            emit(Resource.Loading())
            val recipe = repository.getRecipeById(recipeId).toRecipe()
            if (recipe !== null) {
                emit(Resource.Success(recipe))
            }
        } catch(e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred."))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection."))
        }
    }
}