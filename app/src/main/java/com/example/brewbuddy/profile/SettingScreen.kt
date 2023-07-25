package com.example.brewbuddy.profile

import android.content.Intent
import android.net.Uri
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
import androidx.core.net.toUri
import com.example.brewbuddy.common.Constants.DEFAULT_BANNER_URL
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import com.example.brewbuddy.ui.theme.Brown
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import java.util.UUID

val db = FirebaseFirestore.getInstance()

var currRadius: Float = 10f;
var currVegan: Boolean = false;
var currVegetarian: Boolean = false;
var currDairyFree: Boolean = false;
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
                currDairyFree = snapshot.getBoolean("dairyFree")?: false;
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
fun updateSettings(radius: Float?, vegan: Boolean?, vegetarian: Boolean?, dairyFree: Boolean?, keto: Boolean?, kosher: Boolean?, halal: Boolean?, glutenFree: Boolean?, nutFree: Boolean?) {
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    userId?.let {
        val preferencesRef = db.collection("user_preferences").document(userId)
        val prefs = hashMapOf(
            "radius" to radius,
            "vegan" to vegan,
            "vegetarian" to vegetarian,
            "dairyFree" to dairyFree,
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


fun updateBanner(bannerUrl: String) {
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    userId?.let {
        val userRef = db.collection("users").document(userId)
        val prefs = hashMapOf<String, Any>(
            "bannerUrl" to bannerUrl
        )
        userRef.update(prefs)
            .addOnSuccessListener {
                Log.d("EDIT_PROFILE_BANNER", "User banner updated")
            }
            .addOnFailureListener { exception ->
                Log.d("EDIT_PROFILE_BANNER", "Error changing banner: $exception")
            }
    }
}

fun updateAvatar(avatarUrl: String) {
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    userId?.let {
        val userRef = db.collection("users").document(userId)
        val prefs = hashMapOf<String, Any>(
            "avatarUrl" to avatarUrl
        )
        userRef.update(prefs)
            .addOnSuccessListener {
                Log.d("EDIT_PROFILE_AVATAR", "User avatar updated")
            }
            .addOnFailureListener { exception ->
                Log.d("EDIT_PROFILE_AVATAR", "Error changing avatar: $exception")
            }
    }
}

fun uploadSettingsImageToFirebaseStorage(imageUri: Uri, onUrlReady: (String) -> Unit) {
    if (imageUri != DEFAULT_BANNER_URL.toUri()) {
        val filename = "recipe_image_${UUID.randomUUID()}"
        val storage = Firebase.storage
        val storageRef = storage.reference

        val imageRef = storageRef.child("images/$filename")
        val uploadTask = imageRef.putFile(imageUri)
        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let { throw it }
            }
            imageRef.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUrl = task.result.toString()
                onUrlReady(downloadUrl)
            } else {
                onUrlReady(DEFAULT_BANNER_URL)
            }
        }
    } else {
        onUrlReady(DEFAULT_BANNER_URL)
    }
}

val coffeeIngredients = listOf(
    "Coffee beans",
    "Water",
    "Milk",
    "Cream",
    "Sugar",
    "Sweetener",
    "Honey",
    "Cocoa powder",
    "Cinnamon",
    "Nutmeg",
    "Whipped cream",
    "Ground cinnamon",
    "Ground nutmeg",
    "Ice cubes",
    "Espresso",
    "Condensed milk",
    "Evaporated milk",
    "Coconut milk",
    "Almond milk",
    "Soy milk",
    "Oat milk",
    "Cardamom",
    "Agave syrup",
    "Maple syrup",
    "Lemon zest",
    "Ginger",
    "Lavender",
    "Mint leaves",
    "Turmeric",
    "Coconut oil",
    "Brown sugar",
    "Vanilla extract",
    "Almond extract"
)

private fun retrieveIngredients(onIngredientsLoaded: (List<String>) -> Unit) {
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    userId?.let {
        val preferencesRef = db.collection("user_preferences").document(userId)
        preferencesRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val ingredients = documentSnapshot.get("ingredients") as? List<String> ?: emptyList()
                    onIngredientsLoaded(ingredients)
                } else {
                    onIngredientsLoaded(emptyList())
                }
            }
            .addOnFailureListener { e ->
                // Handle failure
            }
    }
    }



fun updateIngredients(ingredient: String, add: Boolean) {
    //add ingredient to ingredients array in db
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    userId?.let {
        val preferencesRef = db.collection("user_preferences").document(userId)
        if (add) {
            preferencesRef.update("ingredients", FieldValue.arrayUnion(ingredient))
                .addOnSuccessListener {
                    // Handle success
                }
                .addOnFailureListener { e ->
                    // Handle failure
                }
        }
        else {
            preferencesRef.update("ingredients", FieldValue.arrayRemove(ingredient))
                .addOnSuccessListener {
                    // Handle success
                }
                .addOnFailureListener { e ->
                    // Handle failure
                }
        }
        }
}

@Composable
fun IngredientButton(ingredient: String) {
    var selectedIngredientsList by remember { mutableStateOf(emptyList<String>()) }
    var ingredientsFetched by remember {
        mutableStateOf(false)
    }
    // Fetch ingredients from Firestore when the composable is first recomposed
    LaunchedEffect(Unit) {
        retrieveIngredients() { ingredients ->
            selectedIngredientsList = ingredients
            ingredientsFetched = true

        }
    }
    if (ingredientsFetched) {
        var isSelected = selectedIngredientsList.contains(ingredient)
        var selected by remember { mutableStateOf(isSelected) }

        Button(
            onClick = {
                updateIngredients(ingredient, !selected)

                selected = !selected
            },
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (selected) Brown else Color.White,
                contentColor = if (selected) Color.White else Color.Black,
            ),
            modifier = Modifier
                .padding(8.dp)
                .wrapContentWidth()
                .height(50.dp)
        ) {
            Text(text = ingredient)
        }
    }
}

@Composable
fun CoffeeIngredientsRow(ingredientList: List<String>) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (ingredient in ingredientList) {
            IngredientButton(ingredient = ingredient)
        }
    }
}

@Composable
fun CoffeeIngredientsCluster() {
    val rows = 4 // Number of rows in the cluster
    val columns = 3 // Number of ingredients per row

    Column {
        repeat(rows) { rowIndex ->
            val startIndex = rowIndex * columns
            val endIndex = minOf(startIndex + columns, coffeeIngredients.size)
            val rowIngredients = coffeeIngredients.subList(startIndex, endIndex)
            CoffeeIngredientsRow(ingredientList = rowIngredients)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(
    navController: NavController,
    loginUserViewModel: LoginUserViewModel,
    currentUserRepository: CurrentUserRepository,
    menuButton: @Composable () -> Unit
) {
    val currentUserRepository = CurrentUserRepository()
    val loginUserViewModel: LoginUserViewModel = viewModel()
    retrieveSettings()

    // Get the coroutine scope
    val coroutineScope = rememberCoroutineScope()

    val scrollState = rememberScrollState()
    var sliderPosition by remember { mutableStateOf(currRadius) }
    val (veganState, onVeganChange) = remember { mutableStateOf(currVegan) }
    val (vegetarianState, onVegetarianChange) = remember { mutableStateOf(currVegetarian) }
    val (dairyState, onDairyChange) = remember { mutableStateOf(currDairyFree) }
    val (ketoState, onKetoChange) = remember { mutableStateOf(currKeto) }
    val (kosherState, onKosherChange) = remember { mutableStateOf(currKosher) }
    val (halalState, onHalalChange) = remember { mutableStateOf(currHalal) }
    val (glutenState, onGlutenChange) = remember { mutableStateOf(currGlutenFree) }
    val (nutState, onNutChange) = remember { mutableStateOf(currNutFree) }

    var profilePictureUri by remember { mutableStateOf<Uri?>(DEFAULT_BANNER_URL.toUri()) }
    var bannerPictureUri by remember { mutableStateOf<Uri?>(DEFAULT_BANNER_URL.toUri()) }

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
                                value = dairyState,
                                onValueChange = { onDairyChange(!dairyState) },
                                role = Role.Checkbox
                            )
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = dairyState,
                            onCheckedChange = null // null recommended for accessibility with screenreaders
                        )
                        Text(
                            text = "Dairy-free",
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
                Box(modifier = Modifier.padding(40.dp, 24.dp, 40.dp, 0.dp)) {
                    Text(
                        text = "What ingredients do you have?",
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }
            item {
                Box{
                    CoffeeIngredientsCluster()
                }
            }
            item {
//              update profile picture
                ImageUpload("Select New Profile Picture", returnImageUri = {newUri -> profilePictureUri = newUri})
            }

            item {
                ImageUpload("Select New Banner Picture", returnImageUri = {newUri -> bannerPictureUri = newUri})
            }


            item {
                Box(modifier = Modifier.padding(40.dp, 24.dp, 40.dp, 32.dp)) {
                    Button(
                        onClick = {
                            profilePictureUri?.let {
                                uploadSettingsImageToFirebaseStorage(
                                    it
                                ) { newValue: String ->
                                    Log.d("NEWAVATARVALUE", newValue)
                                    updateAvatar(newValue)
                                }
                            }

                            bannerPictureUri?.let {
                                uploadSettingsImageToFirebaseStorage(
                                    it
                                ) { newValue: String ->
                                    updateBanner(newValue)
                                }
                            }

                            updateSettings(
                                sliderPosition,
                                veganState,
                                vegetarianState,
                                dairyState,
                                ketoState,
                                kosherState,
                                halalState,
                                glutenState,
                                nutState,
                            );
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
                                handleAccountDeletion(currentUserRepository, loginUserViewModel, navController)
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
    loginUserViewModel: LoginUserViewModel,
    navController: NavController
) {
    val currentUser = currentUserRepository.getCurrentUser()
    if (currentUser != null) {
        val deleteResult = currentUserRepository.deleteAccount(currentUser.uid)

        if (deleteResult) {
            loginUserViewModel.setUser(null)

            val intent = Intent(navController.context, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            navController.context.startActivity(intent)

            (navController.context as? ComponentActivity)?.finish()
        } else {
            // Handle deletion failure
        }
    }
}