package com.example.brewbuddy.profile

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
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
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextField
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PaintingStyle.Companion.Stroke
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.brewbuddy.PinnedCard
import com.example.brewbuddy.ProfilePicture
import com.example.brewbuddy.getUser
import com.example.brewbuddy.ui.theme.GreyLight
import com.example.brewbuddy.ui.theme.GreyMedium
import com.example.brewbuddy.ui.theme.TitleLarge
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.brewbuddy.data.remote.dto.Imperial
import com.example.brewbuddy.data.remote.dto.Ingredient
import com.example.brewbuddy.data.remote.dto.IngredientList
import com.example.brewbuddy.data.remote.dto.Instructions
import com.example.brewbuddy.data.remote.dto.Measures
import com.example.brewbuddy.data.remote.dto.Metric
import com.example.brewbuddy.data.remote.dto.Step
import com.example.brewbuddy.domain.model.Author
import com.example.brewbuddy.domain.model.PostMetadata
import com.example.brewbuddy.domain.model.Recipe
import com.example.brewbuddy.domain.model.RecipeMetadata
import com.example.brewbuddy.marketplace.MarketplaceItemModal
import com.example.brewbuddy.marketplace.MarketplaceViewModel
import com.example.brewbuddy.navigateToItem
import com.example.brewbuddy.navigateToRecipe
import com.example.brewbuddy.recipes.IndividualIngredient
import com.example.brewbuddy.recipes.UserScreenViewModel
import com.example.brewbuddy.shoplocator.Store
import com.example.brewbuddy.ui.theme.Cream
import com.example.brewbuddy.ui.theme.GreenDark
import com.example.brewbuddy.ui.theme.GreenLight
import com.example.brewbuddy.ui.theme.GreenMedium
import com.example.brewbuddy.ui.theme.SlateDark
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
fun mapTags(recipe: Recipe): List<String> {
    var tags = mutableListOf<String>()
    if(recipe.vegan){
        tags.add("vegan")
    }
    if (recipe.vegetarian){
        tags.add("vegetarian")
    }
    if(recipe.glutenFree){
        tags.add("glutenFree")
    }
    if(recipe.dairyFree){
        tags.add("dairyFree")
    }
    if(recipe.sustainable){
        tags.add("sustainable")
    }
    return tags
}

fun generateKeywords(recipe: Recipe): List<String> {
    val allWords = recipe.title.split(" ")
    val normalizedWords = allWords.map { word ->
        word.toLowerCase().replace(Regex("[^a-zA-Z0-9]"), "")
    }
    val blackListWords = listOf(
        "as",
        "the",
        "is",
        "at",
        "in",
        "with",
        "a",
        "&",
        "and",
        "to",
        "how",
        "you",
        "all"
    )
    val keywords = normalizedWords.filter { word ->
        word !in blackListWords
    }
    return keywords
}

fun postRecipe(recipe: Recipe) {
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    val tags = mapTags(recipe)
    val keywords = generateKeywords(recipe)
    userId?.let {
        val recipesRef = db.collection("recipes").document()
        val recipeInfo = hashMapOf(
            "id" to "testing testing",
            "authorId" to recipe.author.id,
            "bannerUrl" to recipe.bannerUrl,
            "ingredientLists" to recipe.ingredientLists,
            "instructions" to recipe.instructions,
            "preparationMinutes" to recipe.preparationMinutes,
            "servings" to recipe.servings,
            "summary" to recipe.summary,
            "title" to recipe.title,
            "dairyFree" to recipe.dairyFree,
            "glutenFree" to recipe.glutenFree,
            "sustainable" to recipe.sustainable,
            "vegan" to recipe.vegan,
            "vegetarian" to recipe.vegetarian,
            "tags" to tags,
            "keywords" to keywords
        )
        recipesRef.set(recipeInfo)
        .addOnSuccessListener {
            Log.d("Upload Recipe Success", recipe.bannerUrl)
        }
        .addOnFailureListener { exception ->
            Log.d("Upload Recipe", "Error uploading recipe: $exception")
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Carousel(
    pagerState: PagerState = remember{ PagerState() },
    viewModel: UserScreenViewModel = hiltViewModel()
) {
    val pageCount = 5
    val bounds = 100 // arbitrarily large # to give the illusion of infinite scroll
    val startIndex = bounds / 2

    val focusColor = GreyMedium
    val unfocusedColor = GreyLight

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
               /* val page = getIndex(index, startIndex, pageCount)*/
              Box(contentAlignment = Alignment.Center) {
                   PinnedCard(modifier = Modifier
                       .width(210.dp)
                       .height(150.dp),
                       viewModel.userLikedRecipes.value[index]
                   )
                }
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
    navFunction: (id: String) -> Unit,
    columns: Int,
    modifier: Modifier = Modifier,
    recipes: List<PostMetadata> = emptyList(),
    uploadButton: @Composable () -> Unit
) {
    var itemCount = recipes.size + 1 // +1 for the button
    Log.d("ITEMCOUNT", itemCount.toString())
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
                        if (index < itemCount - 1) {
                            BoxWithConstraints(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(1f)
                                    .padding(4.dp)
                                    .clickable(onClick = {
                                        navFunction(recipes[index].id)
                                    })
                            ) {
                                val bannerUrl = recipes[index].bannerUrl
                                AsyncImage(
                                    model = bannerUrl,
                                    contentDescription = "Recipe Image",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(4.dp)
                                        .aspectRatio(1F),
                                )
                            }
                        } else if (index == itemCount - 1) {
                            uploadButton()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun UploadButton(title: String, onClick: () -> Unit) {
    //https://stackoverflow.com/questions/66427587/how-to-have-dashed-border-in-jetpack-compose
    val stroke = Stroke(width = 4f,
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
    )
    var colour =GreyMedium
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .padding(8.dp)
            .drawBehind {
                drawRoundRect(color = colour, style = stroke)
            }
            .clickable(onClick = {
                onClick()
            })
    ) {
        Column(modifier=Modifier.fillMaxSize()
            , verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                painter = painterResource(id = R.drawable.icon_add_outline),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint= colour
            )
            Text(textAlign = TextAlign.Center, modifier=Modifier.fillMaxWidth(), text=title, color= colour)
        }
    }

}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StepInput(stepNumber: Number, inputStep: String? = null, onStepChange: (Step) -> Unit) {
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
                    onStepChange(Step(stepNumber as Int, step))
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

fun uploadImageToFirebaseStorage(inputRecipe: Recipe, imageUri: Uri?, onUrlReady: (Recipe) -> Unit) {
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
                inputRecipe.bannerUrl = downloadUrl
                onUrlReady(inputRecipe)
            } else {
                onUrlReady(inputRecipe)
            }
        }
    } else {
        onUrlReady(inputRecipe)
    }
}

@Composable
fun ImageUpload(buttonText: String, returnImageUri: (Uri?) -> Unit) {
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
                Text(text = buttonText)
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

fun convertToIngredientDto(ingredient: IndividualIngredient) : Ingredient{
    val imperialValue = Imperial(amount = ingredient.quantity, unitLong = ingredient.unit, unitShort = ingredient.unit)
    val metricValue = Metric(amount = ingredient.quantity, unitLong = ingredient.unit, unitShort = ingredient.unit)
    val measures = Measures(metric = metricValue, imperial = imperialValue)
    return Ingredient(name = ingredient.label, quantity = measures)
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeModal(openDialog: MutableState<Boolean>, onClose: () -> Unit) {
    val ingredients = remember { mutableStateListOf<Ingredient>(convertToIngredientDto(IndividualIngredient(0, "", ""))) }
    val instructions = remember { mutableStateListOf<Step>(Step(0, "")) }
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var servings by remember { mutableStateOf("") }
    var prepMinutes by remember { mutableStateOf("") }
    var uri by remember { mutableStateOf<Uri?>(null) }

    var isVegan: Boolean = false;
    var isVegetarian: Boolean = false;
    var isDairyFree: Boolean = false;
    var isGlutenFree: Boolean = false;
    var isKeto: Boolean = false;
    var isSustainable: Boolean = false;
    val (veganState, onVeganChange) = remember { mutableStateOf(isVegan) }
    val (vegetarianState, onVegetarianChange) = remember { mutableStateOf(isVegetarian) }
    val (dairyState, onDairyChange) = remember { mutableStateOf(isDairyFree) }
    val (ketoState, onKetoChange) = remember { mutableStateOf(isKeto) }
    val (glutenState, onGlutenChange) = remember { mutableStateOf(isGlutenFree) }
    val (sustainableState, onSustainableChange) = remember { mutableStateOf(isSustainable) }

    val currentUser = getUser()

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
                            onClick = {
                                val ingredientToAdd = convertToIngredientDto(IndividualIngredient(0, "", ""))
                                ingredients.add(ingredientToAdd)
                            }
                        ) {
                            Text("Add Ingredient")
                        }
                        Button(
                            modifier = Modifier.weight(0.4f),
                            colors = ButtonDefaults.buttonColors(containerColor = GreenMedium),
                            onClick = { instructions.add(Step(0, "")) }
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
                                val ingredientToAdd = convertToIngredientDto(IndividualIngredient(0, "", ""))
                                ingredients.add(ingredientToAdd)
                                instructions.clear()
                                instructions.add(Step(0, ""))
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
                                var completedRecipe = Recipe(
                                    likes = 0,
                                    vegetarian = vegetarianState,
                                    vegan = veganState,
                                    glutenFree = glutenState,
                                    dairyFree = dairyState,
                                    sustainable = sustainableState,
                                    preparationMinutes = prepMinutes.toInt(),
                                    title = title,
                                    servings = servings.toInt(),
                                    summary = description,
                                    instructions = listOf(Instructions(name = title, steps = instructions)),
                                    ingredientLists = listOf(IngredientList("title", ingredients)),
                                    author = Author(
                                        id = currentUser.getUserId(),
                                        username = currentUser.getUsername(),
                                        avatarUrl = currentUser.getAvatarUrl()
                                    )
                                )
                                uploadImageToFirebaseStorage(
                                    completedRecipe,
                                    uri
                                ) { newValue: Recipe ->
                                    completedRecipe = newValue
                                    postRecipe(newValue)
                                    Log.d("NEWVAL", newValue.bannerUrl)
//                                    Log.d("URIASSTRING", uriAsString)
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
                                text = "Servings",
                                style = TextStyle(fontSize = 20.sp)
                            )
                        }
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(bottom = 10.dp)) {
                            TextField(
                                value = servings,
                                onValueChange = { newValue: String -> servings = newValue },
                                label = { Text(text = "Number of servings") },
                            )
                        }
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(4.dp)) {
                            Text(
                                text = "Preparation Time",
                                style = TextStyle(fontSize = 20.sp)
                            )
                        }
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(bottom = 10.dp)) {
                            TextField(
                                value = prepMinutes,
                                onValueChange = { newValue: String -> prepMinutes = newValue },
                                label = { Text(text = "Minutes taken to prepare") },
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
                                    ingredientData = IndividualIngredient(
                                        quantity = ingredient.quantity.imperial.amount,
                                        unit = ingredient.quantity.imperial.unitShort,
                                        label = ingredient.name
                                    ),
                                    onIngredientChange = { updatedIngredient ->
                                        ingredients[index] = convertToIngredientDto(updatedIngredient)
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
                                    inputStep = instruction.step,
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

                        Row(Modifier.fillMaxWidth().padding(4.dp)) {
                            Text(
                                text = "Select all tags that apply",
                                style = TextStyle(fontSize = 20.sp)
                            )
                        }
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
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .toggleable(
                                    value = sustainableState,
                                    onValueChange = { onSustainableChange(!sustainableState) },
                                    role = Role.Checkbox
                                )
                                .padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = sustainableState,
                                onCheckedChange = null // null recommended for accessibility with screenreaders
                            )
                            Text(
                                text = "Sustainable",
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(start = 16.dp)
                            )
                        }
                        Row() {
                            ImageUpload("Select Image", returnImageUri = {newUri -> uri = newUri})
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

@Composable
private fun <T>UserPostsGrid(state: UserScreenState<T>, title: String, content: @Composable () -> Unit) {
    Box(modifier = Modifier.padding(top = 35.dp)) {
        TitleLarge(text =  title)
    }
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
        content()
    }
}
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun UserScreen(
    menuButton: @Composable () -> Unit,
    navController: NavHostController,
    viewModel: UserScreenViewModel = hiltViewModel(),
    marketplaceViewModel: MarketplaceViewModel = hiltViewModel()
) {
    var recipesState = viewModel.state.value
    var listingState = viewModel.listingState.value
    val user = getUser()
    // todo: change to lazycolumn

    var showDialog = remember { mutableStateOf(false) }
    var showMarketplaceDialog = remember { mutableStateOf(false) }

    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        ProfileHeader(user, menuButton)

        Column(modifier = Modifier.fillMaxSize()) {
            TitleLarge(text="Pinned Recipes")
            if(viewModel.userLikedRecipes.value.isNotEmpty()) {
                Carousel()
            } else {
                Text(
                    text="You haven't liked any recipes yet!",
                    modifier = Modifier.padding(16.dp)
                )
            }
            UserPostsGrid(state=recipesState, title="Your Recipes") {
                Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    ImageGrid(
                        navFunction = {id: String -> navigateToRecipe(id, navController) },
                        columns = 3,
                        modifier = Modifier.padding(8.dp),
                        recipes = recipesState.data,
                        uploadButton={UploadButton("Upload Recipe", onClick={showDialog.value = true})}

                    )

                }
            }


            UserPostsGrid(state=listingState, title="Your Listings") {
                Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    ImageGrid(
                        navFunction = { id: String -> navigateToItem(id, navController) },
                        columns = 3,
                        modifier = Modifier.padding(8.dp),
                        recipes = listingState.data,
                        uploadButton = { UploadButton("Upload Listing", onClick = {showMarketplaceDialog.value = true}) }
                    )
                }
            }
            RecipeModal(showDialog,  onClose = { showDialog.value = false })
            MarketplaceItemModal(marketplaceViewModel, showMarketplaceDialog,  onClose = { showMarketplaceDialog.value = false })

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