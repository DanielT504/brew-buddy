package com.example.brewbuddy.marketplace

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.brewbuddy.R
import com.example.brewbuddy.ui.theme.Cream

@Composable
fun MarketplaceItemScreen(
    navController: NavHostController,
    viewModel: MarketPlaceItemViewModel = hiltViewModel()
) {
    var itemName = "20lbs of fresh-grounded black tea"
    var city = "Cambridge"
    var province = "ON"
    var userName = "John Doe"
    var contact = "519-123-4567"
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row() {
                Box() {
                    AsyncImage(model = R.drawable.x_recipe1, contentDescription = null)
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
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row() {
                    // ITEM NAME HERE
                    Text(
                        text = itemName,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(start = 8.dp, end = 8.dp)
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painterResource(id = R.drawable.icon_locator),
                        modifier = Modifier.size(32.dp),
                        contentDescription = null,
                    )
                    Text(text = "$city, $province", style = MaterialTheme.typography.bodyMedium)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painterResource(id = R.drawable.icon_phone),
                        modifier = Modifier.size(32.dp),
                        contentDescription = null,
                    )
                    Text(text = "$contact", style = MaterialTheme.typography.bodyMedium)
                }
                Row() {
                    Text("Posted by $userName", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }

}
