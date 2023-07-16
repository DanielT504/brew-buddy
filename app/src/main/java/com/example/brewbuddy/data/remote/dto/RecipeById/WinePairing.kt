package com.example.brewbuddy.data.remote.dto.RecipeById

data class WinePairing(
    val pairedWines: List<String>,
    val pairingText: String,
    val productMatches: List<ProductMatche>
)