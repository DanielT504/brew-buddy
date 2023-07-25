package com.example.brewbuddy.marketplace

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.example.brewbuddy.domain.model.MarketplaceItem
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
        var item = state.post ?: MarketplaceItem()
        var itemName = item.title
        var userName = item.author.username
        var description = item.description
        Surface(
            modifier=Modifier.verticalScroll(rememberScrollState()).fillMaxSize(),
            color = Cream
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier=Modifier.fillMaxSize()
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
                    modifier = Modifier
                        .padding(horizontal = 12.dp, vertical = 16.dp)
                        .padding(start = 8.dp, end = 8.dp, top = 16.dp, bottom=16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = itemName,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text("Posted by $userName", style = MaterialTheme.typography.bodyMedium)
                    ItemDetails(item)
                    Text(text="Description", style=MaterialTheme.typography.titleMedium)
                    Text(description)
                }
            }
        }
    }
}


@Composable
private fun ItemMetadata(icon: Int, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            painterResource(id = icon),
            modifier = Modifier.size(16.dp),
            contentDescription = null,
        )
        Text(
            modifier = Modifier.padding(start = 16.dp),

            text = text,
            style = MaterialTheme.typography.bodyLarge
        )
    }

}
@Composable
private fun ItemDetails(item: MarketplaceItem) {
    Column(modifier= Modifier
        .border(0.dp, Color.Transparent, shape = RoundedCornerShape(4.dp))
        .fillMaxWidth()
    ) {
        Text(text="Details", style=MaterialTheme.typography.titleMedium)
        ItemMetadata(R.drawable.icon_locator, "${item.city}, ${item.province}")
        ItemMetadata(R.drawable.icon_phone, item.contact)
        ItemMetadata(R.drawable.icon_dollar, if (item.price.toDouble() < 0) {
            "Contact seller"
        } else {
            item.price.toString()
        })
    }
}
