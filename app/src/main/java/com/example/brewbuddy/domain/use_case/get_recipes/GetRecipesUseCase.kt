package com.example.brewbuddy.domain.use_case.get_recipes

import com.example.brewbuddy.common.Resource
import com.example.brewbuddy.data.remote.dto.RecipeMetadataDto
import com.example.brewbuddy.data.remote.dto.toRecipe
import com.example.brewbuddy.data.remote.dto.toRecipeMetadata
import com.example.brewbuddy.domain.model.Recipe
import com.example.brewbuddy.domain.model.RecipeMetadata
import com.example.brewbuddy.domain.repository.RecipeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetRecipesUseCase @Inject constructor(
    private val repository: RecipeRepository
) {
    operator fun invoke(): Flow<Resource<List<RecipeMetadata>>> = flow {
        try {
            emit(Resource.Loading())
            val recipes = repository.getRecipes(null).map { it.toRecipeMetadata() }
            emit(Resource.Success(recipes))
        } catch(e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred."))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection."))
        }
    }
}