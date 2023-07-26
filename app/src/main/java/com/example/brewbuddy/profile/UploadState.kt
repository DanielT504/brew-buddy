package com.example.brewbuddy.profile

import com.example.brewbuddy.templates.ResultState

data class UploadState(
    override val isLoading: Boolean = false,
    override val error: String = ""
): ResultState()
