package com.example.brewbuddy


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.padding

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.FavoriteBorder

import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import androidx.navigation.compose.*
import com.example.brewbuddy.ui.theme.BrewBuddyTheme
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopLocatorScreen() {
    Surface(modifier = Modifier.fillMaxSize(), color= MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier
                .fillMaxWidth().padding(25.dp)
        ) {

            MapWrapper()
            StoreListSheet()
        }
    }

}
@Composable
@ExperimentalMaterial3Api
fun StoreListSheet() {
    Row(modifier = Modifier.fillMaxSize()) {
        ModalDrawerSheet(modifier = Modifier
            .height(400.dp)
            .align(Alignment.Bottom)
            .verticalScroll(
                rememberScrollState()
            ), drawerShape = MaterialTheme.shapes.extraSmall) {
            Text("Shops nearby", modifier = Modifier.padding(16.dp))
            Divider()
            StoreInfo(storeName = "C&D", address = "188 University Ave.", rating = 4.9, items = "Iced Oat Latte, Matcha Cappucino")
            Divider()
            StoreInfo(storeName = "Starbucks", address = "188 University Ave.", rating = 4.9, items = "Iced Oat Latte, Matcha Cappucino")
            Divider()
            StoreInfo(storeName = "Café Waterloo", address = "188 University Ave.", rating = 4.9, items = "Iced Oat Latte, Matcha Cappucino")
            Divider()
            StoreInfo(storeName = "Café Waterloo", address = "188 University Ave.", rating = 4.9, items = "Iced Oat Latte, Matcha Cappucino")
        }
    }
}

@Composable
fun MapWrapper() {
    val waterloo = LatLng(43.471701, -80.543566)
    val starbs = LatLng(43.472, -80.545)

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(waterloo, 15f)
    }

    GoogleMap(
        modifier = Modifier.height(300.dp),
        cameraPositionState = cameraPositionState
    ) {
        Marker(
            state = MarkerState(position = waterloo),
            title = "C&D",
            snippet = "Marker in Waterloo"
        )
        Marker(
            state = MarkerState(position = starbs),
            title = "Starbucks",
            snippet = "Marker in Waterloo"
        )
    }
}
@Composable
fun StoreInfo(storeName: String, address: String, rating: Double, items: String, modifier: Modifier = Modifier) {
    Row(modifier = Modifier
        .fillMaxWidth()
) {
        Column(modifier = Modifier
            .padding(4.dp,16.dp,16.dp,16.dp)
        ) {
            var selected by remember { mutableStateOf(false) }
            IconButton(
                onClick = {selected = !selected},
            ) {
                Icon(imageVector = if (!selected) Icons.Outlined.FavoriteBorder else Icons.Filled.Favorite,
                    contentDescription = null)
            }
        }
        Column() {
            Row(
                modifier = Modifier
                    .padding(0.dp,16.dp,16.dp,16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "$storeName",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "$address",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Row(
                modifier = Modifier
                    .padding(0.dp,16.dp,16.dp,16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "$rating",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "$items",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ShopLocatorPreview() {
    ShopLocatorScreen()
}


