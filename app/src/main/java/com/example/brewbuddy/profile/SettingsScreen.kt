package com.example.brewbuddy.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.example.brewbuddy.ui.theme.TitleLarge
import kotlin.math.roundToInt
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import android.app.Activity
import android.util.Log
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import com.example.brewbuddy.radius
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

val db = FirebaseFirestore.getInstance()
data class UserPreferences(var radius: Float, var vegan: Boolean, var vegetarian: Boolean, var lactoseFree: Boolean, var kosher: Boolean, var halal: Boolean, var glutenFree: Boolean, var nutFree: Boolean)
var currRadius: Float = 0f;
var currVegan: Boolean = false;
var currVegetarian: Boolean = false;
var currLactoseFree: Boolean = false;
var currKosher: Boolean = false;
var currHalal: Boolean = false;
var currGlutenFree: Boolean = false;
var currNutFree: Boolean = false;
var currKeto: Boolean = false;


private fun retrieveSettings() {
    val firestore = FirebaseFirestore.getInstance()
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    val preferencesRef = userId?.let { firestore.collection("user_preferences").document(it) }

    CoroutineScope(Dispatchers.IO).launch {
        try {
            val snapshot = preferencesRef?.get()?.await()
            if (snapshot != null) {
                currRadius = snapshot.getDouble("radius")?.toFloat() ?: 0f;
                currVegan= snapshot.getBoolean("vegan")?: false;
                currVegetarian = snapshot.getBoolean("vegetarian")?: false;
                currLactoseFree = snapshot.getBoolean("lactoseFree")?: false;
                currKeto = snapshot.getBoolean("keto")?: false;
                currKosher = snapshot.getBoolean("kosher")?: false;
                currHalal = snapshot.getBoolean("halal")?: false;
                currGlutenFree = snapshot.getBoolean("glutenFree")?: false;
                currNutFree = snapshot.getBoolean("nutFree")?: false;
            }

        } catch (e: Exception) {
            // Handle exceptions, such as network errors or document retrieval failures
        }
    }
}

fun updateSettings(radius: Float?, vegan: Boolean?, vegetarian: Boolean?, lactoseFree: Boolean?, keto: Boolean?, kosher: Boolean?, halal: Boolean?, glutenFree: Boolean?, nutFree: Boolean?) {
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    userId?.let {
        val preferencesRef = db.collection("user_preferences").document(userId)
        val prefs = hashMapOf(
            "radius" to radius,
            "vegan" to vegan,
            "vegetarian" to vegetarian,
            "lactoseFree" to lactoseFree,
            "keto" to keto,
            "kosher" to kosher,
            "halal" to halal,
            "glutenFree" to glutenFree,
            "nutFree" to nutFree
        )
        preferencesRef.set(prefs)
            .addOnSuccessListener {
                // Successfully updated the radius in Firestore
                Log.d("EDIT_PREFS", "User prefs changed in user pref")
            }
            .addOnFailureListener { exception ->
                Log.d("EDIT_PREFS", "Error changing user prefs: $exception")
            }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(menuButton: @Composable () -> Unit) {
    retrieveSettings()
    Surface() {
        Column(modifier = Modifier.verticalScroll(rememberScrollState())
        ) {
            menuButton()
            TitleLarge(text = "Settings")
            Box(modifier = Modifier.padding(40.dp, 16.dp, 40.dp, 0.dp)) {
                Text(
                    text = "Set your maximum shop radius",
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
            val sliderPosition = remember { mutableStateOf(currRadius) }
            Box(modifier = Modifier.padding(40.dp, 16.dp, 40.dp, 0.dp)) {
                Slider(
                    modifier = Modifier.semantics { contentDescription = "Localized Description" },
                    value = sliderPosition.value,
                    onValueChange = { sliderPosition.value = it },
                    valueRange = 0f..50f,
                    onValueChangeFinished
                    = {
                        //todo
                    },
                    steps = 9
                )
            }
            Box(modifier = Modifier.padding(40.dp, 16.dp, 40.dp, 0.dp)) {
                Text(text = sliderPosition.value.roundToInt().toString()+" km")
            }
            Box(modifier = Modifier.padding(40.dp, 24.dp, 40.dp, 0.dp)) {
                Text(
                    text = "Choose your dietary preferences",
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
            val (veganState, onVeganChange) = remember { mutableStateOf(currVegan) }
            Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .toggleable(
                            value = veganState,
                            onValueChange = { onVeganChange(!veganState) },
                            role = Role.Checkbox
                        )
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = veganState,
                        onCheckedChange = null // null recommended for accessibility with screenreaders
                    )
                    Text(
                        text = "Vegan",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }
            val (vegetarianState, onVegetarianChange) = remember { mutableStateOf(currVegetarian) }
            Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .toggleable(
                            value = vegetarianState,
                            onValueChange = { onVegetarianChange(!vegetarianState) },
                            role = Role.Checkbox
                        )
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = vegetarianState,
                        onCheckedChange = null // null recommended for accessibility with screenreaders
                    )
                    Text(
                        text = "Vegetarian",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }
            val (lactoseState, onLactoseChange) = remember { mutableStateOf(currLactoseFree) }
            Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .toggleable(
                            value = lactoseState,
                            onValueChange = { onLactoseChange(!lactoseState) },
                            role = Role.Checkbox
                        )
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = lactoseState,
                        onCheckedChange = null // null recommended for accessibility with screenreaders
                    )
                    Text(
                        text = "Lactose-free",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }
            val (ketoState, onKetoChange) = remember { mutableStateOf(currKeto) }
            Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .toggleable(
                            value = ketoState,
                            onValueChange = { onKetoChange(!ketoState) },
                            role = Role.Checkbox
                        )
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = ketoState,
                        onCheckedChange = null // null recommended for accessibility with screenreaders
                    )
                    Text(
                        text = "Keto",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }
            val (kosherState, onKosherChange) = remember { mutableStateOf(currKosher) }
            Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .toggleable(
                            value = kosherState,
                            onValueChange = { onKosherChange(!kosherState) },
                            role = Role.Checkbox
                        )
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = kosherState,
                        onCheckedChange = null // null recommended for accessibility with screenreaders
                    )
                    Text(
                        text = "Kosher",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }
            val (halalState, onHalalChange) = remember { mutableStateOf(currHalal) }
            Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .toggleable(
                            value = halalState,
                            onValueChange = { onHalalChange(!halalState) },
                            role = Role.Checkbox
                        )
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = halalState,
                        onCheckedChange = null // null recommended for accessibility with screenreaders
                    )
                    Text(
                        text = "Halal",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }
            val (glutenState, onGlutenChange) = remember { mutableStateOf(currGlutenFree) }
            Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .toggleable(
                            value = glutenState,
                            onValueChange = { onGlutenChange(!glutenState) },
                            role = Role.Checkbox
                        )
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = glutenState,
                        onCheckedChange = null // null recommended for accessibility with screenreaders
                    )
                    Text(
                        text = "Gluten-free",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }
            val (nutState, onNutChange) = remember { mutableStateOf(currNutFree) }
            Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .toggleable(
                            value = glutenState,
                            onValueChange = { onNutChange(!nutState) },
                            role = Role.Checkbox
                        )
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = nutState,
                        onCheckedChange = null // null recommended for accessibility with screenreaders
                    )
                    Text(
                        text = "Nut-free",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }
            Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = {
                            updateSettings(sliderPosition.value, veganState, vegetarianState, lactoseState, ketoState, kosherState, halalState, glutenState, nutState );
                        },
                        shape = RoundedCornerShape(50.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Text(text = "Save preferences")
                    }
                }
            }
            Box(modifier = Modifier.padding(400.dp, 0.dp, 40.dp, 0.dp)) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .toggleable(
                            value = glutenState,
                            onValueChange = { onNutChange(!nutState) },
                            role = Role.Checkbox
                        )
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = nutState,
                        onCheckedChange = null // null recommended for accessibility with screenreaders
                    )
                    Text(
                        text = "Nut-free",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }

        }
    }
}

