package com.example.brewbuddy.marketplace

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.brewbuddy.R
import com.example.brewbuddy.ui.theme.Cream

@Composable
fun MarketplaceItemScreen(
    navController: NavHostController,
    viewModel: MarketplaceItemViewModel = hiltViewModel()
) {
    var state = viewModel.state.value


    if(state.error.isNotBlank()) {
        Text(
            text = state.error,
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        )
    } else if(state.isLoading){
        Surface(modifier = Modifier.fillMaxSize(), color = Cream) {
            Box() {
                CircularProgressIndicator(modifier = Modifier
                    .align(Alignment.Center)
                    .size(34.dp))
            }
        }
    } else {
        var itemName = state.post!!.title
        var city = state.post!!.city
        var province = state.post!!.province
        var userName = state.post!!.author!!.username
        var contact = state.post!!.contact
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row() {
                    Box() {
                        AsyncImage(model = state.post!!.imageUrl, contentDescription = null, modifier=Modifier.fillMaxWidth(), contentScale = ContentScale.FillWidth)
                        IconButton(onClick = { if (navController.previousBackStackEntry != null) navController.popBackStack() else null }) {
                            Icon(
                                tint = Cream,
                                painter = painterResource(id = R.drawable.icon_expand_circle_down),
                                contentDescription = "Navigate back",
                                modifier = Modifier
                                    .background(Color.Transparent)
                                    .size(64.dp)
                                    .rotate(90.0F)
                            )
                        }
                    }
                }
                Column(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 16.dp).padding(start = 8.dp, end=8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = itemName,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text("Posted by $userName", style = MaterialTheme.typography.bodyMedium)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painterResource(id = R.drawable.icon_locator),
                            modifier = Modifier.size(24.dp),
                            contentDescription = null,
                        )
                        Text(text = "$city, $province", style = MaterialTheme.typography.bodyMedium)
                        Icon(
                            painterResource(id = R.drawable.icon_phone),
                            modifier = Modifier.size(24.dp),
                            contentDescription = null,
                        )
                        Text(text = "$contact", style = MaterialTheme.typography.bodyMedium)

                    }
                }
            }
        }
    }
}
