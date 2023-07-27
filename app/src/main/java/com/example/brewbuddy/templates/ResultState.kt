package com.example.brewbuddy.templates

abstract class ResultState {
    abstract val isLoading: Boolean
    abstract val error: String
}