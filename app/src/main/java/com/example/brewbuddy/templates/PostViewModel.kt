package com.example.brewbuddy.templates

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.brewbuddy.common.Constants
import com.example.brewbuddy.domain.model.PostState

abstract class PostViewModel<T>(
    param: String,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    protected val _state = mutableStateOf(PostState<T>())
    val state: State<PostState<T>> = _state

    private var savedStateHandle = savedStateHandle
    private var param = param


    protected fun fetchPost() {
        savedStateHandle.get<String>(param)?.let { id ->
            Log.d("PostViewModel", param)
            Log.d("PostViewModel", id)
            val id = id.substringAfter("}")
            this.getPostById(id)
        }
    }
    protected abstract fun getPostById(postId: String);
}