package com.example.brewbuddy.recipes

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.brewbuddy.common.Resource
import com.example.brewbuddy.common.createQueryString
import com.example.brewbuddy.domain.model.RecipeMetadata
import com.example.brewbuddy.domain.model.SearchResultState
import com.example.brewbuddy.domain.use_case.get_recipes.GetRecipeResultsUseCase
import com.example.brewbuddy.marketplace.Filter
import com.example.brewbuddy.profile.currGlutenFree
import com.example.brewbuddy.profile.currHalal
import com.example.brewbuddy.profile.currKeto
import com.example.brewbuddy.profile.currKosher
import com.example.brewbuddy.profile.currDairyFree
import com.example.brewbuddy.profile.currNutFree
import com.example.brewbuddy.profile.currRadius
import com.example.brewbuddy.profile.currVegan
import com.example.brewbuddy.profile.currVegetarian
import com.example.brewbuddy.profile.db
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class RecipesViewModel  @Inject constructor(
    private val getRecipeResultsUseCase: GetRecipeResultsUseCase,
    savedStateHandle: SavedStateHandle
): SearchViewModel<RecipeMetadata>(savedStateHandle) {

    private fun hashToFilterList(map: HashMap<String, Boolean>) {

    }
    init {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        userId?.let {
            val ref = db.collection("user_preferences").document(userId)
            ref.get()
                .addOnSuccessListener {snapshot ->
                    if (snapshot != null) {
                        RecipeTagList.forEach { recipeTagInfo ->
                            val bool = snapshot.getBoolean(recipeTagInfo.name) ?: false

                            if(bool) {
                                addFilter(Filter(name=recipeTagInfo.name, filterLabel = recipeTagInfo.label, enabled=true))
                            }
                        }
                    }
                    search()
                }
                .addOnFailureListener { exception ->
                    Log.d("MarketplaceViewModel", "Error getting user preferences: $exception")
                }
            getResults()

        }

    }
    override fun search() {
        _query.value = createQueryString(_search.value, _filters)

        getResults(_query.value)
    }

    private fun getResults(query: String = "") {
        getRecipeResultsUseCase(query).onEach { result ->
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