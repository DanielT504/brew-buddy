package com.example.brewbuddy.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.brewbuddy.templates.ResultState
import com.example.brewbuddy.ui.theme.Cream
import com.example.brewbuddy.ui.theme.LoadingScreen


@Composable
fun LoadingModal(size: Dp =34.dp, color: Color = MaterialTheme.colorScheme.primary) {
    Box(modifier=Modifier.fillMaxSize()) {
        CircularProgressIndicator(modifier = Modifier
            .align(Alignment.Center)
            .size(size),
            color = color
        )
    }
}

@Composable
fun ErrorModal(msg: String) {
    Text(
        text = msg,
        color = MaterialTheme.colorScheme.error,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    )

}
@Composable
fun Content(state: ResultState, content: @Composable () -> Unit) {
    if(state.error.isNotBlank()) {
        ErrorModal(state.error)
    } else if(state.isLoading){
        Surface(modifier=Modifier.fillMaxSize()) {
            LoadingModal()
        }
    } else {
        content()
    }
}