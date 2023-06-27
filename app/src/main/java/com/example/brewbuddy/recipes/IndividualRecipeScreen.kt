package com.example.brewbuddy.recipes

import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.brewbuddy.R
import com.example.brewbuddy.randomSizedPhotos
import com.example.brewbuddy.ui.theme.Brown
import com.example.brewbuddy.ui.theme.Cream
import com.example.brewbuddy.ui.theme.GreenDark
import com.example.brewbuddy.ui.theme.GreenLight


sealed class RecipeNavigationScreens(val route: String) {
    object IndividualRecipe : RecipeNavigationScreens("Recipes/{recipe_name}")
}

@Composable
fun IndividualRecipeScreen(navController: NavHostController, param: String) {
    val title = param.substringAfter("}")
    var recipe = recipes.last { it.recipeName == title }
    Column (modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())){
        Box() {
            RecipeBanner(recipe.backgroundImage, recipe.recipeName, navController)
        }
        Box(modifier = Modifier
            .offset(y = -(20.dp))
            .fillMaxWidth()) {
            RecipeSection(recipe)
        }
    }
}

@Composable
private fun RecipeBanner(img: Any, title: String, navController: NavHostController) {
    val contextForToast = LocalContext.current.applicationContext
    Box(modifier = Modifier
        .height(230.dp)
        .fillMaxWidth(),
    ) {
/*        Image(
            painter = painterResource(id = img),
            contentDescription = "Recipe Banner",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()

        )*/
        AsyncImage(
            model = img,
            contentDescription = "Recipe Banner",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Row(modifier = Modifier.padding(horizontal = 12.dp, vertical = 16.dp)) {
            IconButton(onClick = { if (navController.previousBackStackEntry != null) navController.popBackStack() else null}) {
                Icon(
                    tint = Cream,
                    painter = painterResource(id = R.drawable.icon_expand_circle_down),
                    contentDescription = "Navigate back",
                    modifier = Modifier
                        .background(Color.Transparent)
                        .size(60.dp)
                        .rotate(90.0F)
                )
            }
        }
        Row(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween)
        {
            Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 24.dp)) {
                Row() {
                    Text(style = MaterialTheme.typography.titleLarge, text =  title, color =  Cream)
                }
                Row() {
                    Text(modifier = Modifier.padding(horizontal = 2.dp), text = "by Jane Doe", color = Cream)
                }
            }
            Column(
                modifier = Modifier
                    .padding(horizontal = 2.dp, vertical = 12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    Box( modifier = Modifier.align(Alignment.CenterVertically)) {
                        IconButton(onClick = { Toast.makeText(contextForToast, "Added to Favourites", Toast.LENGTH_SHORT).show()}) {
                            Canvas(modifier = Modifier.size(38.dp)) {
                                drawCircle(color = Cream)
                            }
                            Icon(
                                tint = Brown,
                                painter = painterResource(id = R.drawable.icon_favourite_heart),
                                contentDescription = "Favourite Recipe",
                                modifier = Modifier
                                    .size(24.dp)
                                    .align(Alignment.Center)
                            )
                        }
                    }
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            tint = Cream,
                            painter = painterResource(id = R.drawable.icon_more_vertical),
                            contentDescription = "Explore more",
                            modifier = Modifier.size(42.dp),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RecipeSection(recipe: Recipe) {
    Surface(
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(color = Color.Transparent)
    )
    {
        Column(horizontalAlignment = Alignment.Start,
        ) {
            Row(modifier = Modifier
                .padding(vertical = 14.dp, horizontal = 4.dp)
                .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            )
            {
                Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                    LabelledIcon(img = R.drawable.icon_time, label = "10 min")
                    Divider(modifier = Modifier
                        .width(1.dp)
                        .size(28.dp), color = Brown)
                    LabelledIcon(img = R.drawable.icon_mug, label = "Serves 1")
                    Divider(modifier = Modifier
                        .width(1.dp)
                        .size(28.dp), color = Brown)
                    LabelledIcon(img = R.drawable.icon_star_outline, label = "4.5")
                }
            }
            Row(modifier = Modifier.padding(top = 8.dp)) {
                TagsSection(tags = recipe.tags)
            }
            Row(modifier = Modifier.padding(top = 16.dp)) {
                IngredientsSection(ingredients = recipe.ingredientList)
            }
            Row(modifier = Modifier.padding(top = 24.dp)) {
                PreparationSection(recipe)
            }
        }
    }
}

@Composable
private fun LabelledIcon(@DrawableRes img: Int, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            tint = Brown,
            painter = painterResource(id = img),
            contentDescription = "Display icon",
            modifier = Modifier.size(28.dp)
        )
        Text(text = label, fontSize = 12.sp)
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun TagsSection(tags: List<TagType>) {
    FlowRow(
        modifier = Modifier
            .padding(start = 22.dp)
            .background(color = Color.Transparent),
        maxItemsInEachRow = 4,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        tags.forEach {
            Tag(it.iconTint, it.tagColor, it.tagText, it.img)
        }
    }
}

@Composable
private fun IngredientsSection(ingredients: List<Ingredient>) {
    Column(modifier = Modifier.padding(start = 24.dp)) {
        Row(modifier = Modifier.padding(bottom = 4.dp)) {
            Text("Ingredients", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }
        Column() {
            ingredients.forEach {
                Row() {
                    SectionHeading(it.ingredientName, preparationHeading = false)
                }
                for (i in 0 until it.ingredientComposite.quantities.size){
                    Row(){
                        IngredientBullet(
                            quantity = it.ingredientComposite.quantities[i],
                            subIngredientDetail = it.ingredientComposite.subIngredientDetails[i]
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionHeading(heading: String, preparationHeading: Boolean) {
    val headingString = if (preparationHeading) "For the $heading" else heading
    Text(
        modifier = Modifier
            .padding(top = 4.dp, bottom = 4.dp),
        fontStyle = FontStyle.Italic,
        text = headingString,
        style = MaterialTheme.typography.bodyMedium
        )
}

@Composable
private fun IngredientBullet(quantity: String, subIngredientDetail: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(start = 10.dp)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .width(70.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Canvas(modifier = Modifier.size(6.dp),) {
                    drawCircle(SolidColor(Color.Black))
                }
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = quantity, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
            }
        }
        Column(modifier = Modifier
            .align(Alignment.CenterVertically)
            .padding(start = 4.dp)
        ) {
            Text(text = subIngredientDetail, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
private fun PreparationSection(recipe: Recipe) {
    Column(modifier = Modifier.padding(start = 24.dp, bottom = 16.dp)) {
        Row(modifier = Modifier.padding(bottom = 4.dp)) {
            Text("Preparation", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }
        Column() {
            recipe.preparationSteps.forEach {
                Row() {
                    SectionHeading(it.mappedIngredient, preparationHeading = true)
                }
                for (i in 0 until it.steps.size) {
                    val stepNumber = (i + 1).toString()
                    Row(modifier = Modifier.padding(end = 4.dp)) {
                        Text(
                            modifier = Modifier.padding(start = 10.dp),
                            text = stepNumber + ". " + it.steps[i],
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun Tag(iconTint: Color,
                tagColor: Color,
                tagText: String,
                @DrawableRes img: Int
) {
    Box(modifier = Modifier.padding(top = 6.dp)) {
        Box(
            modifier =
            Modifier
                .clip(RoundedCornerShape(14.dp))
                .background(color = tagColor),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
            ) {
                Icon(
                    tint = iconTint,
                    painter = painterResource(id = img),
                    contentDescription = "Display icon",
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = tagText,
                    color = Color.White,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

private val recipes = listOf(
    Recipe(
        "Cappuccino Almond Pistachio",
        "Espresso",
        com.example.brewbuddy.testIngredients,
        tags = com.example.brewbuddy.testTags,
        backgroundImage = randomSizedPhotos[0],
        preparationSteps =  listOf(
            PreparationStep(
            mappedIngredient = "cappuccino",
            steps = listOf("Gather the ingredients.", "Add almond.", "Add to your espresso machine and leave for 10 minutes.")))
        ),
    Recipe(
        "The Perfect Espresso",
        "Espresso",
        com.example.brewbuddy.testIngredients,
        tags = com.example.brewbuddy.testTags,
        backgroundImage = randomSizedPhotos[4],
        preparationSteps =  listOf(
            PreparationStep(
                mappedIngredient = "espresso",
                steps = listOf("Gather the ingredients.", "Place the water into the boiler of your espresso machine.")))
    ),
    Recipe(
        "Iced Chai Tea Latte",
        "Espresso",
        com.example.brewbuddy.testIngredients,
        tags = com.example.brewbuddy.testTags,
        backgroundImage =  randomSizedPhotos[1],
        preparationSteps =  listOf(
            PreparationStep(
                mappedIngredient = "espresso",
                steps = listOf("Gather the ingredients.", "Place the water into the boiler of your espresso machine.")))
    ),
    Recipe(
        "Murphy's Special Matcha Tea",
        "Espresso",
        com.example.brewbuddy.testIngredients,
        tags = com.example.brewbuddy.testTags,
        backgroundImage = randomSizedPhotos[randomSizedPhotos.size - 2],
        preparationSteps =  listOf(
            PreparationStep(
                mappedIngredient = "espresso",
                steps = listOf("Gather the ingredients.", "Place the water into the boiler of your espresso machine.")))
    ),
    Recipe(
        "Yerba Mate Brew",
        "Espresso",
        com.example.brewbuddy.testIngredients,
        tags = com.example.brewbuddy.testTags,
        backgroundImage = randomSizedPhotos[randomSizedPhotos.size - 1],
        preparationSteps =  listOf(
            PreparationStep(
                mappedIngredient = "espresso",
                steps = listOf("Gather the ingredients.", "Place the water into the boiler of your espresso machine.")))
    ),
    Recipe(
        "Espresso",
        "Espresso",
        com.example.brewbuddy.testIngredients,
        tags = com.example.brewbuddy.testTags,
        backgroundImage = randomSizedPhotos[11],
    ),
    Recipe(
        "Espresso",
        "Espresso",
        com.example.brewbuddy.testIngredients,
        tags = com.example.brewbuddy.testTags,
        backgroundImage = randomSizedPhotos[2],
    ),
    Recipe(
        "Espresso",
        "Espresso",
        com.example.brewbuddy.testIngredients,
        tags = com.example.brewbuddy.testTags,
        backgroundImage = randomSizedPhotos[8],
    ),

    )



