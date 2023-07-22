package com.example.brewbuddy.marketplace

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.brewbuddy.domain.use_case.get_recipes.GetRecipeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MarketPlaceItemViewModel  @Inject constructor(
  /*  savedStateHandle: SavedStateHandle*/
): ViewModel() {

    init {
        Log.d("LOADED", "MarketplaceItem")
    }
}