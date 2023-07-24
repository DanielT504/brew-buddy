package com.example.brewbuddy.marketplace

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MarketplaceItemViewModel  @Inject constructor(
  /*  savedStateHandle: SavedStateHandle*/
): ViewModel() {

    init {
        Log.d("LOADED", "MarketplaceItem")
    }
}