package com.example.brewbuddy.domain.model

data class SearchResultState<T> (
    val isLoading: Boolean = false,
    val error: String = "",
    val results: List<T> = emptyList()
)