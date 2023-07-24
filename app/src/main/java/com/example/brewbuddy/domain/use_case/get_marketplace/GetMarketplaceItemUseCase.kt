package com.example.brewbuddy.domain.use_case.get_marketplace

import android.util.Log
import com.example.brewbuddy.common.Resource
import com.example.brewbuddy.data.remote.dto.toMarketplaceItem
import com.example.brewbuddy.domain.model.MarketplaceItem
import com.example.brewbuddy.domain.repository.RecipeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject


class GetMarketplaceItemUseCase @Inject constructor(
    private val repository: RecipeRepository
) {
    operator fun invoke(itemId: String): Flow<Resource<MarketplaceItem>> = flow {
        try {
            emit(Resource.Loading())
            Log.d("getMarketplaceItem", itemId)
            val item = repository.getMarketplaceItemById(itemId).toMarketplaceItem()
            if (item !== null) {
                emit(Resource.Success(item))
            }
        } catch(e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred."))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection."))
        }
    }
}