package com.example.brewbuddy.domain.use_case.get_marketplace

import com.example.brewbuddy.common.Resource
import com.example.brewbuddy.data.remote.dto.toMarketplaceItemMetadata
import com.example.brewbuddy.domain.model.MarketplaceItemMetadata
import com.example.brewbuddy.domain.repository.RecipeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetMarketplaceItemsByUserIdUseCase @Inject constructor(
    private val repository: RecipeRepository
) {
    operator fun invoke(userId: String): Flow<Resource<List<MarketplaceItemMetadata>>> = flow {
        try {
            emit(Resource.Loading())
            val marketplaceItems = repository.getMarketplaceItemsByUserId(userId).map { it.toMarketplaceItemMetadata() }
            emit(Resource.Success(marketplaceItems))
        } catch(e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred."))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection."))
        }
    }

}
