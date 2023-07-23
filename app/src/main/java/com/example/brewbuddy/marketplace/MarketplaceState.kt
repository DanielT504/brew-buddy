package com.example.brewbuddy.marketplace

import com.example.brewbuddy.data.remote.dto.MarketplaceItemMetadata

data class MarketplaceState(
    val isLoading: Boolean = false,
    val data: List<MarketplaceItemMetadata> = emptyList(),
    val error: String = "",
)
