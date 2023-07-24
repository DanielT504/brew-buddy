package com.example.brewbuddy.profile

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.annotation.DrawableRes
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.BoxWithConstraints
import android.provider.CalendarContract
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.brewbuddy.AccessScreens
import com.example.brewbuddy.PinnedCard
import com.example.brewbuddy.ProfilePicture
import com.example.brewbuddy.R
import com.example.brewbuddy.getUser
import com.example.brewbuddy.ui.theme.GreyLight
import com.example.brewbuddy.ui.theme.GreyMedium
import com.example.brewbuddy.ui.theme.TitleLarge
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.brewbuddy.common.Constants.DEFAULT_BANNER_URL
import com.example.brewbuddy.domain.model.RecipeMetadata
import com.example.brewbuddy.recipes.IndividualIngredient
import com.example.brewbuddy.recipes.IndividualRecipeScreenViewModel
import com.example.brewbuddy.recipes.IndividualStep
import com.example.brewbuddy.recipes.IngredientsList
//import com.example.brewbuddy.recipes.RecipeBanner
//import com.example.brewbuddy.recipes.RecipeSection
import com.example.brewbuddy.recipes.UserScreenViewModel
import com.example.brewbuddy.shoplocator.Store
//import com.example.brewbuddy.store1
import com.example.brewbuddy.ui.theme.Cream
import com.example.brewbuddy.ui.theme.GreenDark
import com.example.brewbuddy.ui.theme.GreenLight
import com.example.brewbuddy.ui.theme.GreenMedium
import com.example.brewbuddy.ui.theme.SlateLight
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.json.JSONException
import org.json.JSONObject
import java.util.UUID

private fun getIndex(currentIndex: Int, startIndex: Int, pageCount: Int): Int {
    val diff = currentIndex - startIndex;
    if (diff < 0) {
        return currentIndex % pageCount
    }
    return diff % pageCount
}
fun postRecipe(recipe: IngredientsList?, title: String?, summary: String?, imageUri: String) {
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    Log.d("Upload Image Success2", imageUri)
    userId?.let {
        val recipesRef = db.collection("recipes").document(userId)
        val recipeInfo = hashMapOf(
            "bannerUrl" to imageUri,
            "ingredientLists" to recipe,
            "title" to title,
            "summary" to summary,
            "authorId" to userId,
//            "summary" to lactoseFree
        )
        recipesRef.set(recipeInfo)
        .addOnSuccessListener {
            Log.d("Upload Image Success", imageUri)
        }
        .addOnFailureListener { exception ->
            Log.d("Upload Image", "Error uploading recipe: $exception")
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Carousel(pagerState: PagerState = remember{ PagerState() },) {
    val pageCount = 5
    val bounds = 100 // arbitrarily large # to give the illusion of infinite scroll
    val startIndex = bounds / 2

    val focusColor = GreyMedium
    val unfocusedColor = GreyLight
//    val tempRecipe = Recipe("Latte")

    Column(modifier=Modifier.fillMaxWidth()) {

        HorizontalPager(
            modifier = Modifier,
            state = pagerState,
            pageSpacing = 8.dp,
            contentPadding = PaddingValues(horizontal = 30.dp),
            pageSize = PageSize.Fixed(220.dp),
            key = null,
            beyondBoundsPageCount = 1,
            pageCount = pageCount,
            pageContent = { index ->
                val page = getIndex(index, startIndex, pageCount)

                // TODO: Temporarily commented out because there is no pinned recipes data being returned by a user
//                Box(contentAlignment = Alignment.Center) {
//                    PinnedCard(modifier = Modifier
//                        .width(210.dp)
//                        .height(150.dp), tempRecipe)
//
//                }
            },
        )

        Row(
            modifier= Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            repeat(pageCount) { index ->
                val color = if ( getIndex(pagerState.currentPage, startIndex, pageCount) == index) focusColor else unfocusedColor
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(12.dp)
                        .background(color)
                )
            }

        }

    }
}

@Composable
fun ImageGrid(
    columns: Int,
    modifier: Modifier = Modifier,
    recipes: List<RecipeMetadata> = emptyList(),
) {
//    val images = listOf(
//        R.drawable.x_recipe1,
//        R.drawable.x_recipe2,
//        R.drawable.x_recipe3,
//        R.drawable.x_recipe4,
//        R.drawable.x_recipe5,
//        R.drawable.x_recipe6,
//        R.drawable.x_recipe7,
//        R.drawable.x_recipe8,
//        R.drawable.x_recipe9,
//    )
//    var itemCount = images.size
    Log.d("TEST_IMAGE_GRID2", recipes.size.toString())
    val imageUrls = recipes.map { it.bannerUrl }
    var itemCount = imageUrls.size
    Log.d("TEST_IMAGE_GRID", itemCount.toString())
    Column(modifier = modifier) {
        var rows = (itemCount / columns)
        if (itemCount.mod(columns) > 0) {
            rows += 1
        }

        for (rowId in 0 until rows) {
            val firstIndex = rowId * columns

            Row {
                for (columnId in 0 until columns) {
                    val index = firstIndex + columnId
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        if (index < itemCount) {
//                            navController = NavController()

                            // todo: make images clickable
                            BoxWithConstraints(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(1f)
                                    .padding(4.dp)
//                                    .clickable( onClick = { navController.navigate(AccessScreens.Login.route)} )
                            ) {
                                val imageUrl = imageUrls[index]
                                AsyncImage(
                                    model = imageUrl,
                                    contentDescription = "Recipe Image",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(4.dp)
                                        .aspectRatio(1F),
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StepInput(stepNumber: Number, inputStep: String? = null, onStepChange: (IndividualStep) -> Unit) {
    var step by remember { mutableStateOf("") }
    if (inputStep != null) {
        step = inputStep
    }

    Column() {
        Row(Modifier.fillMaxWidth()) {
            TextField(
                value = step,
                onValueChange = { newValue ->
                    step = newValue
                    onStepChange(IndividualStep(stepNumber, step))
                },
                label = { Text("Add Step", style = TextStyle(fontSize = 12.sp, color = Color.Gray)) },
                modifier = Modifier
                    .weight(1f)
                    .padding(4.dp),
            )
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IngredientInput(ingredientData: IndividualIngredient? = null, onIngredientChange: (IndividualIngredient) -> Unit)  {
    var ingredient by remember { mutableStateOf("")}
    var quantity by remember { mutableStateOf("")}
    var quantityAsNum by remember { mutableStateOf<Number>(0)}
    var unit by remember { mutableStateOf("")}

    if (ingredientData != null) {
        ingredient = ingredientData.label
        quantity = ingredientData.quantity.toString()
        quantityAsNum = ingredientData.quantity
        unit = ingredientData.unit
    }

    Column() {
        Row(Modifier.fillMaxWidth()) {
            TextField(
                value = ingredient,
                onValueChange = { newValue ->
                    ingredient = newValue
                    onIngredientChange(
                        IndividualIngredient(quantityAsNum, unit, newValue)
                    )
                },
                label = { Text("Ingredient", style = TextStyle(fontSize = 12.sp, color = Color.Gray)) },
                modifier = Modifier
                    .weight(1f)
                    .padding(4.dp),
            )
        }
        Row(Modifier.fillMaxWidth()) {
            TextField(
                value = quantity,
                onValueChange = { newQuantity ->
                    quantity = newQuantity
                    quantityAsNum = newQuantity.toFloatOrNull() ?: 0.0
                    onIngredientChange(
                        IndividualIngredient(quantityAsNum, unit, ingredient)
                    )
                },
                label = { Text("Quantity", style = TextStyle(fontSize = 12.sp, color = Color.Gray)) },
                modifier = Modifier
                    .weight(0.5f)
                    .padding(4.dp),
            )
            TextField(
                value = unit,
                onValueChange = { newValue ->
                    unit = newValue
                    onIngredientChange(
                        IndividualIngredient(quantityAsNum, newValue, ingredient)
                    )
                },
                label = { Text("Unit", style = TextStyle(fontSize = 12.sp, color = Color.Gray)) },
                modifier = Modifier
                    .weight(0.5f)
                    .padding(4.dp),
            )
        }
    }
}

fun uploadImageToFirebaseStorage(imageUri: Uri?, onUrlReady: (String) -> Unit) {
    if (imageUri != null) {
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

@Composable
fun ImageUpload(returnImageUri: (Uri?) -> Unit) {
    // credit to Kiran Bahalaskar for image upload demo code used for most of this function
    // https://www.youtube.com/watch?v=ec8YymnjQSE&ab_channel=KBCODER
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    val bitmap = remember { mutableStateOf<Bitmap?>(null) }

    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            imageUri = uri
            if (uri != null) {
                returnImageUri(uri)
            }
        }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        imageUri?.let {
            if (Build.VERSION.SDK_INT < 28) {
                bitmap.value = MediaStore.Images
                    .Media.getBitmap(context.contentResolver, it)
            } else {
                val source = ImageDecoder.createSource(context.contentResolver, it)
                bitmap.value = ImageDecoder.decodeBitmap(source)
            }

            bitmap.value?.let { btm ->
                Image(
                    bitmap = btm.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier
                        .size(400.dp)
                        .padding(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (imageUri == null){
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = GreenLight),
                onClick = {
                launcher.launch("image/*")
                }
            ) {
                Text(text = "Select Image")
            }
        } else {
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = GreenLight),
                onClick = {
                    imageUri = null
                    returnImageUri(imageUri)
                }
            ) {
                Text(text = "Remove Image")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeModal(openDialog: MutableState<Boolean>, onClose: () -> Unit) {
    val ingredients = remember { mutableStateListOf<IndividualIngredient>(IndividualIngredient(0, "", "")) }
    val instructions = remember { mutableStateListOf<IndividualStep>(IndividualStep(0, "")) }
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var uri by remember { mutableStateOf<Uri?>(null) }


    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = { onClose() },
            confirmButton = {
                Column() {
                    Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
                        Button(
                            modifier = Modifier
                                .padding(end = 4.dp)
                                .weight(0.5f),
                            colors = ButtonDefaults.buttonColors(containerColor = GreenMedium),
                            onClick = { ingredients.add(IndividualIngredient(0, "", "")) }
                        ) {
                            Text("Add Ingredient")
                        }
                        Button(
                            modifier = Modifier.weight(0.4f),
                            colors = ButtonDefaults.buttonColors(containerColor = GreenMedium),
                            onClick = { instructions.add(IndividualStep(0, "")) }
                        ) {
                            Text("Add Step")
                        }
                    }
                    Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                        Button(
                            modifier = Modifier
                                .padding(end = 4.dp)
                                .weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = GreenDark),
                            onClick = {
                                ingredients.clear()
                                ingredients.add(IndividualIngredient(0, "", ""))
                                instructions.clear()
                                instructions.add(IndividualStep(0, ""))
                                title = ""
                                description = ""
                                uri = null

                                onClose()
                            }
                        ) {
                            Text("Exit")
                        }
                        Button(
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = GreenDark),
                            onClick = {
                                var labelList = mutableStateListOf<String>()
                                var unitList = mutableStateListOf<String>()
                                var quantityList = mutableStateListOf<Number>()

                                ingredients.forEach { ingredient ->
                                    labelList.add(ingredient.label)
                                    unitList.add(ingredient.unit)
                                    quantityList.add(ingredient.quantity)
                                }

                                val completedRecipe = IngredientsList(quantityList, unitList, labelList)
                                var uriAsString = ""
                                uploadImageToFirebaseStorage(
                                    uri
                                ) { newValue: String ->
                                    uriAsString = newValue
                                    postRecipe(completedRecipe, title, description, newValue)
                                    Log.d("NEWVAL", newValue)
                                    Log.d("URIASSTRING", uriAsString)
                                }
                                onClose()
                            }
                        ) {
                            Text("Confirm")
                        }
                    }
                }
            },
            dismissButton = {},
            icon = {},
            title = {
                Text(text = "Upload Recipe")
            },
            text = {
                Column(modifier = Modifier.height(400.dp), verticalArrangement = Arrangement.SpaceBetween) {
                    Column(Modifier.verticalScroll(rememberScrollState())) {
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(4.dp)) {
                            Text(
                                text = "Recipe Title",
                                style = TextStyle(fontSize = 20.sp)
                            )
                        }
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(bottom = 10.dp)) {
                            TextField(
                                value = title,
                                onValueChange = { newValue: String -> title = newValue },
                                label = { Text(text = "Title") },
                            )
                        }
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(4.dp)) {
                            Text(
                                text = "Ingredients",
                                style = TextStyle(fontSize = 20.sp)
                            )
                        }
                        ingredients.forEachIndexed { index, ingredient ->
                            val temp = index + 1
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(4.dp))
                            {
                                Text(
                                    text = "Ingredient $temp"
                                )
                                Box(
                                    modifier = Modifier
                                        .clickable(onClick = { ingredients.removeAt(index) })
                                        .padding(horizontal = 8.dp, vertical = 0.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "Remove", color = Color.Gray)
                                }

                            }
                            Row(modifier = Modifier.padding(bottom = 8.dp)) {
                                IngredientInput(
                                    ingredientData = ingredient,
                                    onIngredientChange = { updatedIngredient ->
                                        ingredients[index] = updatedIngredient
                                    }
                                )
                            }
                        }
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(4.dp)) {
                            Text(
                                text = "Instructions",
                                style = TextStyle(fontSize = 20.sp)
                            )
                        }
                        instructions.forEachIndexed { index, instruction ->
                            val temp = index + 1
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(4.dp))
                            {
                                Text(
                                    text = "Step $temp"
                                )
                                Box(
                                    modifier = Modifier
                                        .clickable(onClick = { instructions.removeAt(index) })
                                        .padding(horizontal = 8.dp, vertical = 0.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "Remove", color = Color.Gray)
                                }

                            }
                            Row(modifier = Modifier.padding(bottom = 8.dp)) {
                                StepInput(
                                    index + 1,
                                    inputStep = instruction.stepInfo,
                                    onStepChange = { updatedStep ->
                                        instructions[index] = updatedStep
                                    }
                                )
                            }
                        }
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(4.dp)) {
                            Text(
                                text = "Description",
                                style = TextStyle(fontSize = 20.sp)
                            )
                        }
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(bottom = 10.dp)) {
                            TextField(
                                value = description,
                                onValueChange = { newValue: String -> description = newValue },
                                label = { Text(text = "Description") },
                            )
                        }
                        Row() {
                            ImageUpload(returnImageUri = {newUri -> uri = newUri})
                        }
                    }
                }
            },
            shape = MaterialTheme.shapes.large,
            iconContentColor = Color.Black,
            titleContentColor = Color.Black,
            textContentColor = Color.Black,
        )
    }
}


fun parseData(jsonData: JSONObject): List<Store> {
    val parsedList = mutableListOf<Store>()

    try {
        val longitudeObj = jsonData.getJSONObject("longitude")
        val ratingObj = jsonData.getJSONObject("rating")
        val itemsObj = jsonData.getJSONObject("items")
        val savedObj = jsonData.getJSONObject("saved")
        val storeNameObj = jsonData.getJSONObject("storeName")
        val latitudeObj = jsonData.getJSONObject("latitude")
        Log.d("parsed", longitudeObj.toString())
        val length = maxOf(
            longitudeObj.length(),
            ratingObj.length(),
            itemsObj.length(),
            savedObj.length(),
            storeNameObj.length(),
            latitudeObj.length()
        )

        for (i in 0 until length) {
            val parsedData = Store(
                storeNameObj.getString(i.toString()),
                "",
                latitudeObj.getDouble(i.toString()),

                longitudeObj.getDouble(i.toString()),
                listOf(itemsObj.getString(0.toString())),
                ratingObj.getDouble(i.toString()),
                savedObj.getBoolean(i.toString()),
            )
            parsedList.add(parsedData)
        }

    } catch (e: JSONException) {
        e.printStackTrace()
    }

    return parsedList
}
var storesOnProfile = listOf<Store>()
private fun retrieveSavedStores() {
    val firestore = FirebaseFirestore.getInstance()
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    val storesRef = userId?.let { firestore.collection("saved_stores").document(it) }

    CoroutineScope(Dispatchers.IO).launch {
        try {
            val snapshot = storesRef?.get()?.await()
            if (snapshot != null) {
                if (snapshot.exists()) {
                    val savedStoresMap = snapshot.data
                    Log.d("GET_STORE", savedStoresMap.toString())
                    if (savedStoresMap != null) {
                        storesOnProfile = parseData(JSONObject(savedStoresMap))
                        }
                        Log.d("PROFILE_STORES", storesOnProfile.toString())
                    }
                } else {
                    Log.d("GET_STORE", "Document not found")
                }
            }
         catch (e: Exception) {
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun UserScreen(
    menuButton: @Composable () -> Unit,
    viewModel: UserScreenViewModel = hiltViewModel()
) {
    var state = viewModel.state.value
    val user = getUser()
    // todo: change to lazycolumn
    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        ProfileHeader(user, menuButton)

        Column(modifier = Modifier.fillMaxSize()) {
            TitleLarge(text="Pinned Recipes")
            Carousel()

            Box(modifier = Modifier.padding(top = 35.dp)) {
                TitleLarge(text = "Your Recipes")
            }







            if(state.error.isNotBlank()) {
                Log.d("ADFIFAHEIDFHADIFH", state.data.size.toString())
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
                if (state.data != null) {
                    Log.d("!!!!!!!!!!!!!!!", state.data.size.toString())
                    ImageGrid(columns = 3, modifier = Modifier.padding(16.dp), state.data)
                }
                else {
                    Log.d("BOOOOOOOO", state.data.size.toString())
                    Text( text = "You have not uploaded any recipes" )
                }
            }


//            ImageGrid(3, modifier = Modifier.padding(16.dp))


            var showDialog = remember { mutableStateOf(false) }
            Button(
                modifier = Modifier.padding(16.dp),
                onClick = {
                    showDialog.value = true
                }
            ) {
                Text(text = "Upload Recipe")
            }
            RecipeModal(showDialog,  onClose = { showDialog.value = false })
            Box() {
                TitleLarge(text = "Saved Shops near you")
            }
            Box(modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, shape = RoundedCornerShape(32.dp))
                .padding(16.dp, 0.dp, 16.dp, 100.dp)) {
                retrieveSavedStores()
                if (!storesOnProfile.isNullOrEmpty()) {
                    MapWrapper(stores = storesOnProfile)
                }
                else {
                    Text(
                        text="You haven't saved any shops near you yet!",
                        style=MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 12.dp)
                    )
                }
                }
            }

        }

    }

@Composable
fun MapWrapper(stores: List<Store>) {
    val savedStore = LatLng(stores[stores.size-1].latitude, stores[stores.size-1].longitude)

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(savedStore, 15f)
    }

    GoogleMap(
        modifier = Modifier
            .height(300.dp),
        cameraPositionState = cameraPositionState
    ) {
        for (store in stores) {
            Marker(
                state = MarkerState(position = LatLng(store.latitude, store.longitude)),
                title = store.storeName
            )
        }
    }
}

@Composable
fun ProfileHeader(user: User, menuButton: @Composable () -> Unit) {
    val profilePictureSize = 126.dp
    val username = user.getUsername()
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(){
            ProfileBanner(user.getBannerUrl())
            menuButton()
        }
        Box(
            modifier= Modifier
                .offset(y = -(profilePictureSize / 2 + 25.dp))
                .fillMaxWidth()
        ) {
            Surface(
                modifier= Modifier
                    .offset(y = profilePictureSize / 2)
                    .height(profilePictureSize - 25.dp)
                    .fillMaxWidth(),
                shape= RoundedCornerShape(topEnd = 25.dp, topStart = 25.dp)
            ) {
                Text(
                    text=username,
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.offset(y=profilePictureSize/2)
                )

            }
            Box(modifier=Modifier.align(Alignment.TopCenter)) {
                ProfilePicture(user.getAvatarUrl(), profilePictureSize)
            }
        }
    }
}
@Composable
fun ProfileBanner(bannerUrl: String) {
    Box(modifier = Modifier.height(200.dp)) {
        AsyncImage(
            model = bannerUrl,
            contentDescription = "Profile Banner",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}