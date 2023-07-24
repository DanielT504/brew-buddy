package com.example.brewbuddy.profile

import android.content.Intent
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.brewbuddy.MainActivity
import com.example.brewbuddy.ui.theme.TitleLarge
import kotlin.math.roundToInt
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await

val db = FirebaseFirestore.getInstance()

var currRadius: Float = 10f;
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
fun SettingScreen(
    navController: NavController,
    currentUserViewModel: CurrentUserViewModel,
    currentUserRepository: CurrentUserRepository,
    menuButton: @Composable () -> Unit
) {
    val currentUserRepository = CurrentUserRepository()
    val currentUserViewModel: CurrentUserViewModel = viewModel()
    retrieveSettings()

    // Get the coroutine scope
    val coroutineScope = rememberCoroutineScope()

    val scrollState = rememberScrollState()
    var sliderPosition by remember { mutableStateOf(currRadius) }
    val (veganState, onVeganChange) = remember { mutableStateOf(currVegan) }
    val (vegetarianState, onVegetarianChange) = remember { mutableStateOf(currVegetarian) }
    val (lactoseState, onLactoseChange) = remember { mutableStateOf(currLactoseFree) }
    val (ketoState, onKetoChange) = remember { mutableStateOf(currKeto) }
    val (kosherState, onKosherChange) = remember { mutableStateOf(currKosher) }
    val (halalState, onHalalChange) = remember { mutableStateOf(currHalal) }
    val (glutenState, onGlutenChange) = remember { mutableStateOf(currGlutenFree) }
    val (nutState, onNutChange) = remember { mutableStateOf(currNutFree) }

    Surface(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(0.dp, 0.dp, 0.dp, 128.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            item {
                menuButton()
                TitleLarge(text = "Settings")
            }
            item {
                Box(modifier = Modifier.padding(40.dp, 16.dp, 40.dp, 0.dp)) {
                    Text(
                        text = "Set your maximum shop radius",
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }

            item {
                Box(modifier = Modifier.padding(40.dp, 16.dp, 40.dp, 0.dp)) {
                    Slider(
                        modifier = Modifier.semantics {
                            contentDescription = "Localized Description"
                        },
                        value = sliderPosition,
                        onValueChange = { sliderPosition = it },
                        valueRange = 0f..50f,
                        onValueChangeFinished = {
                            //todo
                        },
                        steps = 9
                    )
                }
            }

            item {
                Box(modifier = Modifier.padding(40.dp, 16.dp, 40.dp, 0.dp)) {
                    Text(text = sliderPosition.roundToInt().toString() + " km")
                }
            }

            item {
                Box(modifier = Modifier.padding(40.dp, 24.dp, 40.dp, 0.dp)) {
                    Text(
                        text = "Choose your dietary preferences",
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }

            item {
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
            }

            item {
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
            }

            item {
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
            }

            item {
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
            }
            item {
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
            }
            item {
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
            }
            item {
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
            }
            item {
                Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .toggleable(
                                value = nutState,
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

            item {
                Box(modifier = Modifier.padding(40.dp, 24.dp, 40.dp, 32.dp)) {
                    Button(
                        onClick = {
                            updateSettings(sliderPosition, veganState, vegetarianState, lactoseState, ketoState, kosherState, halalState, glutenState, nutState );
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

            item {
                Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
                    Button(
                        onClick = {
                            // Logout the user and navigate back to the main activity
                            // currentUserViewModel.setUser(null)

                            // Start the MainActivity to simulate a restart
                            val intent = Intent(navController.context, MainActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                            navController.context.startActivity(intent)

                            // Finish the current activity to clear it from the back stack
                            (navController.context as? ComponentActivity)?.finish()
                        },
                        shape = RoundedCornerShape(50.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Text(text = "Logout")
                    }
                }
            }

            item {
                Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                handleAccountDeletion(currentUserRepository, currentUserViewModel, navController)
                            }
                        },
                        shape = RoundedCornerShape(50.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Text(text = "Delete Account")
                    }
                }
            }
        }
    }
}

private suspend fun handleAccountDeletion(
    currentUserRepository: CurrentUserRepository,
    currentUserViewModel: CurrentUserViewModel,
    navController: NavController
) {
    val currentUser = currentUserRepository.getCurrentUser()
    if (currentUser != null) {
        val deleteResult = currentUserRepository.deleteAccount(currentUser.uid)

        if (deleteResult) {
            currentUserViewModel.setUser(null)

            val intent = Intent(navController.context, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            navController.context.startActivity(intent)

            (navController.context as? ComponentActivity)?.finish()
        } else {
            // Handle deletion failure
        }
    }
}