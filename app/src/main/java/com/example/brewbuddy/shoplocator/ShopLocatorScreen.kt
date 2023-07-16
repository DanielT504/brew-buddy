package com.example.brewbuddy



import android.location.Geocoder
import android.location.Location
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.maps.android.compose.CameraPositionState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.util.Locale
import androidx.compose.ui.unit.IntOffset
import kotlin.math.roundToInt


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
    Box(modifier = Modifier.fillMaxSize()) {
        MapWrapper(currentLocation, cameraPositionState)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = 200.dp)
        ) {
            StoreListSheet(currentLocation)
        }
    }
}
data class Cafe(val name: String, val address: String, val rating: Double)

@Composable
@ExperimentalMaterial3Api
fun StoreListSheet(currentLocation: Location) {
    var responseData by remember { mutableStateOf("") }
    val apiKey = "AIzaSyDQHE1mHDySR2X73vU3v0PEzXrVV5b_Kus"
    val geocoder = Geocoder(LocalContext.current, Locale.getDefault())
    val address = geocoder.getFromLocation(currentLocation.latitude, currentLocation.longitude, 1)
    val latitude = address?.firstOrNull()?.latitude ?: currentLocation.latitude
    val longitude = address?.firstOrNull()?.longitude ?: currentLocation.longitude

    val location = LatLng(latitude, longitude)
    var cafeList by remember { mutableStateOf<List<Cafe>>(emptyList()) }
    LaunchedEffect(Unit) {
        val url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json" +
                "?location=${location.latitude}%2C${location.longitude}" +
                "&radius=2000" +
                "&type=cafe" +
                "&keyword=independent"+
                "&keyword=organic|fair+trade|eco-friendly"+
                "&key=$apiKey"

        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .build()

        withContext(Dispatchers.IO) {
            try {
                val response: Response = client.newCall(request).execute()
                responseData = response.body?.string() ?: "Empty response"

                // Parse the JSON response
                val places = JSONObject(responseData).getJSONArray("results")
                val cafes = mutableListOf<Cafe>()

                for (i in 0 until places.length()) {
                    val placeObj = places.getJSONObject(i)
                    val name = placeObj.getString("name")
                    val address = placeObj.getString("vicinity").substringBefore(",")
                    val rating = placeObj.optDouble("rating", 0.0)

                    val cafe = Cafe(name, address, rating)
                    if (name != "Starbucks" && name!="Tim Hortons" && name!= "McDonald's"&& name!= "Williams Fresh Cafe") {
                        cafes.add(cafe)
                    }
                }
                cafeList = cafes

            } catch (e: Exception) {
                responseData = "Error: ${e.localizedMessage}"
            }
        }
    }
    var slideOffset by remember { mutableStateOf(0f) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.Transparent)
            .offset { IntOffset(0, (slideOffset * 900).roundToInt()) } // Adjust the height value (900) as needed
            .draggable(
                state = rememberDraggableState { delta ->
                    slideOffset += delta / 900 // Adjust the height value (900) as needed
                },
                orientation = Orientation.Vertical
            )
    ) {
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
                    modifier = Modifier.verticalScroll(rememberScrollState())
                ) {
                    Text(
                        "Shops nearby",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    for (cafe in cafeList) {
                        Divider()
                        StoreInfo(
                            storeName = cafe.name,
                            address = cafe.address,
                            rating = cafe.rating,
                            items = "Iced coffee"
                        )
                    }
                }
            }
        }
    }
}

data class CafeMarker(val name: String, val position: LatLng)

@Composable
fun MapWrapper(currentLocation: Location,
               cameraPositionState: CameraPositionState
) {
    var responseData by remember { mutableStateOf("") }
    val apiKey = "AIzaSyDQHE1mHDySR2X73vU3v0PEzXrVV5b_Kus"
    val geocoder = Geocoder(LocalContext.current, Locale.getDefault())
    val address = geocoder.getFromLocation(currentLocation.latitude, currentLocation.longitude, 1)
    val latitude = address?.firstOrNull()?.latitude ?: currentLocation.latitude
    val longitude = address?.firstOrNull()?.longitude ?: currentLocation.longitude

    val location = LatLng(latitude, longitude)
    var cafeList by remember { mutableStateOf<List<CafeMarker>>(emptyList()) }
    LaunchedEffect(Unit) {
        val url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json" +
                "?location=${location.latitude}%2C${location.longitude}" +
                "&radius=2000" +
                "&type=cafe" +
                "&keyword=independent"+
                "&keyword=organic|fair+trade|eco-friendly"+
                "&key=$apiKey"

        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .build()

        withContext(Dispatchers.IO) {
            try {
                val response: Response = client.newCall(request).execute()
                responseData = response.body?.string() ?: "Empty response"

                // Parse the JSON response
                val places = JSONObject(responseData).getJSONArray("results")
                val cafes = mutableListOf<CafeMarker>()

                for (i in 0 until places.length()) {
                    val placeObj = places.getJSONObject(i)
                    val name = placeObj.getString("name")
                    val loc = placeObj.getJSONObject("geometry")
                        .getJSONObject("location")
                    val latitude = loc.getDouble("lat")
                    val longitude = loc.getDouble("lng")

                    val cafeLocation = LatLng(latitude, longitude)
                    val cafeMarker = CafeMarker(name, cafeLocation)
                    if (name != "Starbucks" && name!="Tim Hortons" && name!= "McDonald's"&& name!= "Williams Fresh Cafe") {
                        cafes.add(cafeMarker)
                    }
                }
                cafeList = cafes

            } catch (e: Exception) {
                responseData = "Error: ${e.localizedMessage}"
            }
        }
    }

    val cameraPositionState1 = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(location, 15f)
    }


        GoogleMap(
            modifier = Modifier
                .fillMaxSize(),
            cameraPositionState = cameraPositionState1
        ) {
            for (cafe in cafeList) {
                Marker(
                    state = MarkerState(position = cafe.position),
                    title = cafe.name
                )
            }
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
                                textAlign = TextAlign.End,
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.bodyMedium
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




