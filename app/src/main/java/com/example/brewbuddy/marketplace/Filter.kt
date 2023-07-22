package com.example.brewbuddy.marketplace

import androidx.compose.runtime.MutableState

data class Filter (
     val name: String,
     val filterLabel: String,
     var enabled: Boolean,
)