package com.example.brewbuddy.marketplace

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.brewbuddy.common.Resource
import com.example.brewbuddy.common.createQueryString
import com.example.brewbuddy.domain.model.MarketplaceItemMetadata
import com.example.brewbuddy.domain.model.SearchResultState
import com.example.brewbuddy.domain.use_case.get_marketplace.GetMarketplaceItemsUseCase
import com.example.brewbuddy.profile.db
import com.example.brewbuddy.recipes.SearchViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone
import javax.inject.Inject

@HiltViewModel
class MarketplaceViewModel @Inject constructor(
    private val getMarketplaceItemsUseCase: GetMarketplaceItemsUseCase,
    savedStateHandle: SavedStateHandle
): SearchViewModel<MarketplaceItemMetadata>(savedStateHandle) {

    init {
        getResults()
    }
    override fun search() {
        _query.value = createQueryString(_search.value, _filters)

        getResults(_query.value)
    }

    fun post(
        isEquipment: Boolean,
        isIngredients: Boolean,
        contact: String,
        city: String,
        province: String,
        title: String,
        description: String,
        imageUrl: String,
        price: Number
    ) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        Log.d("Upload Image Success2", imageUrl)
        var tagArr = ArrayList<String>()

        if(isEquipment) {
            tagArr.add("equipment")
        }
        if(isIngredients) {
            tagArr.add("ingredients")
        }

        val df  = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        df.timeZone = TimeZone.getTimeZone("UTC")

        val nowAsISO: String = df.format(Date())
        val tags = tagArr.toList()
        val keyArr = title.lowercase().split(" ").toTypedArray()
        val keywords = keyArr.toList()
        userId?.let {
            val ref = db.collection("marketplace").document()
            val itemInfo = hashMapOf(
                "imageUrl" to imageUrl,
                "city" to city,
                "title" to title,
                "description" to description,
                "keywords" to keywords,
                "tags" to tags,
                "equipment" to isEquipment,
                "ingredients" to isIngredients,
                "price" to price,
                "authorId" to userId,
                "contact" to contact,
                "province" to province,
                "date" to nowAsISO
            )
            ref.set(itemInfo)
                .addOnSuccessListener {
                    Log.d("Upload Image Success", imageUrl)
                    search()
                }
                .addOnFailureListener { exception ->
                    Log.d("Upload Image", "Error uploading recipe: $exception")
                }
        }
    }
    private fun getResults(query: String = "") {
        getMarketplaceItemsUseCase(query).onEach { result ->
            when(result) {
                is Resource.Success -> {
                    _state.value = SearchResultState(results = result.data ?: emptyList())
                }
                is Resource.Error -> {
                    _state.value = SearchResultState(error = result.message ?: "An unexpected error occurred.")
                }
                is Resource.Loading -> {
                    _state.value = SearchResultState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }
}
