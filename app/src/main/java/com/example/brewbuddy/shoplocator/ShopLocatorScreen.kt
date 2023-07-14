package com.example.brewbuddy


import android.location.Location
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.*
import com.example.brewbuddy.shoplocator.LocationPermissionsAndSettingDialogs
import com.example.brewbuddy.shoplocator.LocationUtils
import com.example.brewbuddy.shoplocator.Store
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.maps.android.compose.CameraPositionState



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopLocatorScreen(fusedLocationProviderClient: FusedLocationProviderClient) {
    var currentLocation by remember { mutableStateOf(LocationUtils.getDefaultLocation()) }

    val cameraPositionState = rememberCameraPositionState()
    cameraPositionState.position = CameraPosition.fromLatLngZoom(
        LocationUtils.getPosition(currentLocation), 12f)

    var requestLocationUpdate by remember { mutableStateOf(true)}

    if(requestLocationUpdate) {
        LocationPermissionsAndSettingDialogs(
            updateCurrentLocation = {
                requestLocationUpdate = false
                LocationUtils.requestLocationResultCallback(fusedLocationProviderClient) { locationResult ->

                    locationResult.lastLocation?.let { location ->
                        currentLocation = location
                    }

                }
            }
        )
    }

    Column (modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())){
        Box(modifier = Modifier
            .fillMaxWidth()) {
            MapWrapper(currentLocation,
                cameraPositionState)
            Box(modifier = Modifier
                .fillMaxWidth()
                .offset(y = 200.dp)) {
                StoreListSheet()
            }
        }

    }
}

@Composable
@ExperimentalMaterial3Api
fun StoreListSheet() {
    Surface(
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.Transparent)
    )
    {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(900.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier=Modifier.verticalScroll(rememberScrollState())
            ) {
                Text("Shops nearby", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Divider()
                StoreInfo(
                    storeName = "C&D",
                    address = "188 University Ave.",
                    rating = 4.9,
                    items = "Iced Oat Latte, Matcha Cappucino"
                )
            Divider()
                StoreInfo(
                    storeName = "Starbucks",
                    address = "188 University Ave.",
                    rating = 4.9,
                    items = "Iced Oat Latte, Matcha Cappucino"
                )
            Divider()
            StoreInfo(storeName = "Café Waterloo", address = "188 University Ave.", rating = 4.9, items = "Iced Oat Latte, Matcha Cappucino")
            Divider()
            StoreInfo(storeName = "Café Waterloo", address = "188 University Ave.", rating = 4.9, items = "Iced Oat Latte, Matcha Cappucino")

            Divider()
            StoreInfo(storeName = "Café Waterloo", address = "188 University Ave.", rating = 4.9, items = "Iced Oat Latte, Matcha Cappucino")
        }
        }
    }
}


@Composable
fun MapWrapper(currentLocation: Location,
               cameraPositionState: CameraPositionState
) {
    val waterloo = LatLng(43.471701, -80.543566)
    val starbs = LatLng(43.472, -80.545)
    Places.initialize(LocalContext.current, "AIzaSyDQHE1mHDySR2X73vU3v0PEzXrVV5b_Kus")
    val placesClient: PlacesClient = Places.createClient(LocalContext.current)


    val cameraPositionState1 = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(waterloo, 15f)
    }


        GoogleMap(
            modifier = Modifier
                .height(300.dp),
            cameraPositionState = cameraPositionState1
        ) {
            Marker(
                state = MarkerState(position = LocationUtils.getPosition(currentLocation)),
                title = "Current Position"
            )
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
    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
        ) {
            Row(
                modifier = Modifier
                    .padding(vertical = 14.dp, horizontal = 4.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            )
            {
                Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                    Column(

                    ) {
                        var selected by remember { mutableStateOf(false) }
                        IconButton(
                            onClick = {
                                store1.saved = !store1.saved
                                selected = !selected
                                      },
                            modifier=Modifier.padding(0.dp, 24.dp, 0.dp, 16.dp)
                        ) {
                            Icon(
                                imageVector = if (!selected) Icons.Outlined.FavoriteBorder else Icons.Filled.Favorite,
                                contentDescription = null
                            )
                        }
                    }
                    Column() {
                        Row(
                            modifier = Modifier
                                .padding(0.dp, 16.dp, 16.dp, 16.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "$storeName",
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.titleSmall
                            )
                            Text(
                                text = "$address",
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.titleSmall
                            )
                        }
                        Row(
                            modifier = Modifier
                                .padding(0.dp, 16.dp, 16.dp, 16.dp)
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
        }
    }
}

public val store1 =
    Store(storeName = "C&D",
        address = "188 University Ave.",
        latitude = 43.471701,
        longitude = -80.543566,
        items= listOf("Coffee", "Espresso"),
        rating=4.9, saved = false)


@Preview(showBackground = true)
@Composable
fun ShopLocatorPreview() {
    val location = LocalContext.current
    val fusedLocationProviderClient = remember {
        LocationServices.getFusedLocationProviderClient(location)
    }

    ShopLocatorScreen(fusedLocationProviderClient)
}




