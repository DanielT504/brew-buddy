package com.example.brewbuddy

//import com.example.brewbuddy.recipes.TagType
import android.util.Log
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.brewbuddy.domain.model.Author
import com.example.brewbuddy.marketplace.Filter
import com.example.brewbuddy.recipes.RecipeResultsViewModel
import com.example.brewbuddy.recipes.RecipeScreenViewModel
import com.example.brewbuddy.recipes.TagList
import com.example.brewbuddy.ui.theme.AuthorCardDisplay
import com.example.brewbuddy.ui.theme.Cream
import com.example.brewbuddy.ui.theme.GreenLight
import com.example.brewbuddy.ui.theme.GreenMedium
import com.example.brewbuddy.ui.theme.SlateLight
import com.example.brewbuddy.ui.theme.TitleLarge

sealed class RecipeNavigationScreens(val route: String) {
    object IndividualRecipe : RecipeNavigationScreens("Recipes/{recipeId}")
    object RecipeResults : RecipeNavigationScreens("Recipes/{queryParams}")
}


@Composable
fun RecipesScreen (
    navController: NavHostController,
    viewModel: RecipeScreenViewModel = hiltViewModel()
) {

    Surface(modifier = Modifier.fillMaxSize(), color = Cream) {
        Column(modifier = Modifier
            .fillMaxSize()) {
            SearchBarWrapper(viewModel)
            Box(modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
            ) {

                if(viewModel.query.value.isEmpty()) {
                    TitleLarge(text = "Search for your next recipe.")
                } else {
                    RecipeSearchResults(navController, viewModel)
                }
            }
        }
    }

}

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalComposeUiApi::class,
    ExperimentalLayoutApi::class
)
@Composable
private fun SearchBar(viewModel: RecipeScreenViewModel) {
    var filtersExpanded by remember { mutableStateOf(false) }
    var searchQuery = viewModel.search.value
    Log.d("SEARCH BAR", "RECOMPOSE")
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    Column() {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = { /*todo*/ }) {
                Icon(
                    painterResource(id = R.drawable.icon_search),
                    contentDescription = null
                )
            }
            TextField(
                value = searchQuery,
                onValueChange = {
                    viewModel.setKeywords(it);
                    viewModel.search();
                },
                label = { Text("Search") },
                colors = TextFieldDefaults
                    .textFieldColors(
                        containerColor = Color.White,
                        textColor = Color.DarkGray,
                        unfocusedIndicatorColor = Color.LightGray,
                        focusedIndicatorColor = Color.Gray,
                        disabledLeadingIconColor = Color.DarkGray,
                        disabledIndicatorColor = Color.DarkGray
                    ),

                modifier = Modifier
                    .width(302.dp)
                    .padding(start = 16.dp),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = {
                    /*onSearch(text)*/
                    // Hide the keyboard after submitting the search
                    keyboardController?.hide()
                    //or hide keyboard
                    focusManager.clearFocus()

                })
            )

                Row(
                    modifier = Modifier.padding(top = 4.dp, end = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(1.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .height(52.dp)
                            .padding(top = 8.dp)
                            .padding(top = 8.dp, start = 2.dp, end = 8.dp)

                            .align(Alignment.CenterVertically)
                    ) {
                        IconButton(onClick = { filtersExpanded = !filtersExpanded }) {
                            BadgedBox(
                                badge = {
                                    Badge(containerColor = GreenMedium) {
                                        Text(
                                            text = viewModel.filters.size.toString(),
                                            color = Color.White
                                        )
                                    }
                                }
                            ) {
                                Icon(
                                    painterResource(id = R.drawable.icon_tune),
                                    contentDescription = null,
                                    modifier = Modifier.size(28.dp),
                                    tint = Color.Gray,
                                )
                            }
                        }
                    }
                    RecipeFilters(
                        state=filtersExpanded,
                        onDismissRequest = { filtersExpanded = false },
                        viewModel = viewModel
                    )
                }
        }
        Spacer(modifier = Modifier.height(8.dp))
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.padding(start = 12.dp, end = 12.dp)
        ) {
            for (filter in viewModel.filters) {
                FilterTag(filter = filter, viewModel = viewModel)
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun RecipeFilters(state: Boolean, onDismissRequest: () -> Unit, viewModel: RecipeScreenViewModel) {
    DropdownMenu(
        expanded = state,
        onDismissRequest = onDismissRequest,
        modifier = Modifier
            .background(color = Color.White)
            .fillMaxWidth()
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Column() {
                Text("Filters")
                TagList.forEach { tagInfo ->
                    val filter = Filter(name=tagInfo.name, filterLabel = tagInfo.label, enabled=true)
                    CheckboxFilter(
                        text = tagInfo.label,
                        checked = viewModel.filters.contains(filter),
                        onCheckChanged = {
                            updateActiveFilters(filter, viewModel)
                            viewModel.search()
                        }
                    )
                }
            }
            Column() {
                Text("Sort by")
                SortFilters.forEach { filter ->
                    DropdownMenuItem(
                        text = { Text(filter.filterLabel) },
                        onClick = {
                            updateActiveFilters(filter, viewModel)
                        }
                    )
                }
            }
        }
    }

}

@Composable
private fun CheckboxFilter(text: String, checked: Boolean, onCheckChanged: (Boolean) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(checked = checked, onCheckedChange = onCheckChanged)
        Text(text)
    }

}
private fun updateActiveFilters(
    filterToAdd: Filter,
    viewModel: RecipeScreenViewModel
) {

    val oldestToNewest = filters.last { filter: Filter -> filter.name == "dateAsce" }
    val newestToOldest = filters.last { filter: Filter -> filter.name == "dateDesc" }
    val priceLowToHigh = filters.last { filter: Filter -> filter.name == "priceAsce"}
    val priceHighToLow = filters.last { filter: Filter -> filter.name == "priceDesc" }
    if (viewModel.filters.contains(filterToAdd)) {
        Log.d("FILTERS", "Removing")
        viewModel.removeFilter(filterToAdd)
        return
    }
    if (filterToAdd.name == "dateDesc"
        && viewModel.filters.contains(oldestToNewest))
    {
        viewModel.removeFilter(oldestToNewest)
        viewModel.addFilter(filterToAdd)
        return
    }
    if (filterToAdd.name == "dateAsce"
        && viewModel.filters.contains(newestToOldest))
    {
        viewModel.removeFilter(newestToOldest)
        viewModel.addFilter(filterToAdd)

        return
    }
    if (filterToAdd.name == "priceAsce"
        && viewModel.filters.contains(priceHighToLow))
    {
        viewModel.removeFilter(priceHighToLow)
        viewModel.addFilter(filterToAdd)

        return
    }
    if (filterToAdd.name == "priceDesc"
        && viewModel.filters.contains(priceLowToHigh))
    {
        viewModel.removeFilter(priceLowToHigh)
        viewModel.addFilter(filterToAdd)

        return
    }
    viewModel.addFilter(filterToAdd)
    return
}

@Composable
private fun FilterTag(filter: Filter, viewModel: RecipeScreenViewModel) {
    Box(modifier = Modifier.padding(top = 6.dp)) {
        Box(
            modifier =
            Modifier
                .clip(RoundedCornerShape(14.dp))
                .background(color = GreenLight)
                .height(28.dp)
            ,
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 2.dp, horizontal = 8.dp)
            ) {
                Box(modifier = Modifier.align(Alignment.CenterVertically)) {
                    IconButton(onClick = { viewModel.filters.remove(filter) }, modifier = Modifier.size(16.dp)) {
                        Canvas(modifier = Modifier.size(16.dp)) {
                            drawCircle(color = SlateLight)
                        }
                        Icon(
                            tint = Color.Black,
                            painter = painterResource(id = R.drawable.icon_close),
                            contentDescription = "Display icon",
                            modifier = Modifier.size(10.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = filter.filterLabel,
                    color = Color.Black,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
private fun SearchBarWrapper(viewModel: RecipeScreenViewModel) {
    Box(modifier = Modifier
        .background(color = Color.White)) {
        SearchBar(viewModel)
    }
}

@Composable
private fun RecipeSearchResults(navController: NavHostController, viewModel: RecipeScreenViewModel) {
    val state = viewModel.state.value
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
        Surface(
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(color = Color.Transparent)
        )
        {

            Column(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, top = 34.dp, bottom = 94.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
            ) {
                for (el in state.results) {
                    ResultCard(
                        title = el.title,
                        bannerUrl = el.bannerUrl,
                        recipeId = el.id,
                        author = el.author,
                        navController = navController
                    )
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ResultCard(
    title: String,
    bannerUrl: String,
    recipeId: String,
    author: Author,
    navController: NavHostController
) {
    Card(
        modifier = Modifier
            .height(160.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
        onClick = { navigateToRecipe(recipeId, navController) }
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Column(modifier = Modifier.width(135.dp)) {
                AsyncImage(
                    model = bannerUrl,
                    contentDescription = "Recipe Banner",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Row() {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontSize = 20.sp
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(28.dp),
                    verticalAlignment = Alignment.Bottom,
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(bottom = 8.dp)
                ) {
//                    Column() {
//                        Row(modifier = Modifier.padding(bottom = 16.dp)) {
//                            Text(text = price, fontSize = 20.sp)
//                        }
//                        Row() {
//                            Text(
//                                text = "$city, $province",
//                                fontSize = 12.sp,
//                                fontWeight = FontWeight.Light,
//                                color = Color.DarkGray
//                            )
//                        }
//                    }
                    Column(horizontalAlignment = Alignment.End) {
                        AuthorCardDisplay(author)
//                        Row(
//                            horizontalArrangement = Arrangement.spacedBy(4.dp),
//                            verticalAlignment = Alignment.CenterVertically
//                        ) {
//                            Text(
//                                text = userName,
//                                fontSize = 12.sp,
//                                fontWeight = FontWeight.Light,
//                                color = Color.DarkGray
//                            )
//                            Box(contentAlignment = Alignment.Center) {
//                                Canvas(modifier = Modifier.size(22.dp)) {
//                                    drawCircle(color = Color.Gray)
//                                }
//                                Icon(
//                                    painterResource(id = R.drawable.icon_user),
//                                    contentDescription = "User image placeholder",
//                                    modifier = Modifier.size(20.dp)
//                                )
//                            }
//                        }
                    }
                }
            }
        }
    }
}
//
private val PreferenceFilters = listOf(
    TagList.forEach {tagInfo ->
        Filter(tagInfo.name, tagInfo.label, false);
    }
)

private val SortFilters = listOf(
    Filter("dateAsce", "Oldest to Newest", false),
    Filter("dateDesc", "Newest to Oldest", false),
    Filter("likesAsce", "Popularity (Low to High)", false),
    Filter("likesDesc", "Popularity (High to Low)", false)
)
//
//data class PreferenceFilter