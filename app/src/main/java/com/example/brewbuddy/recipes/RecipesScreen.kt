package com.example.brewbuddy

//import com.example.brewbuddy.recipes.TagType
import android.app.appsearch.SearchResults
import android.util.Log
import androidx.compose.foundation.Canvas
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.brewbuddy.domain.model.Author
import com.example.brewbuddy.marketplace.Filter
import com.example.brewbuddy.recipes.RecipeTagList
import com.example.brewbuddy.recipes.RecipesViewModel
import com.example.brewbuddy.recipes.SearchViewModel
import com.example.brewbuddy.ui.theme.AuthorCardDisplay
import com.example.brewbuddy.ui.theme.Cream
import com.example.brewbuddy.ui.theme.GreenLight
import com.example.brewbuddy.ui.theme.GreenMedium
import com.example.brewbuddy.ui.theme.Label
import com.example.brewbuddy.ui.theme.SearchBar
import com.example.brewbuddy.ui.theme.SearchResultCard
import com.example.brewbuddy.ui.theme.SearchResults
import com.example.brewbuddy.ui.theme.SlateLight
sealed class RecipeNavigationScreens(val route: String) {
    object IndividualRecipe : RecipeNavigationScreens("Recipes/{recipeId}")
    object RecipeResults : RecipeNavigationScreens("Recipes/{queryParams}")
}


@Composable
fun RecipesScreen (
    navController: NavHostController,
    viewModel: RecipesViewModel = hiltViewModel()
) {

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            SearchBarWrapper(viewModel)
            Surface(modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
            ) {
                RecipeSearchResults(navController, viewModel)
            }
        }
    }

}

@Composable
private fun RecipeFilters(state: Boolean, onDismissRequest: () -> Unit, viewModel: RecipesViewModel) {
    DropdownMenu(
        expanded = state,
        onDismissRequest = onDismissRequest,
        modifier = Modifier
            .background(color = Color.White)
            .fillMaxWidth()
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Column(modifier=Modifier.width(200.dp)) {
                Label("Filters")
                RecipeTagList.forEach { tagInfo ->
                    val filter = Filter(name=tagInfo.name, filterLabel = tagInfo.label, enabled=true)
                    DropdownMenuItem(
                        leadingIcon = {if(viewModel.filters.contains(filter)) {
                            Icon(
                                painter = painterResource(id = R.drawable.icon_checked_box),
                                contentDescription = "Checked",
                                modifier = Modifier.size(24.dp),
                                tint = Color.Black
                            )
                        } else {
                            Icon(
                                painter = painterResource(id = R.drawable.icon_box),
                                contentDescription = "Checked",
                                modifier = Modifier.size(24.dp),
                                tint = Color.Black
                            )
                        }},
                        text = { Text(tagInfo.label) },
                        onClick = {
                            updateActiveFilters(filter, viewModel)
                            viewModel.search()
                        }
                    )
                }
            }
            Column() {
                Label("Sort by")
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

private fun updateActiveFilters(
    filterToAdd: Filter,
    viewModel: RecipesViewModel
) {

    val oldestToNewest = SortFilters.last { filter: Filter -> filter.name == "dateAsce" }
    val newestToOldest = SortFilters.last { filter: Filter -> filter.name == "dateDesc" }
    val likesLowToHigh = SortFilters.last { filter: Filter -> filter.name == "likesAsce"}
    val likesHighToLow = SortFilters.last { filter: Filter -> filter.name == "likesDesc" }
    if (viewModel.filters.contains(filterToAdd)) {
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
    if (filterToAdd.name == "likesAsce"
        && viewModel.filters.contains(likesHighToLow))
    {
        viewModel.removeFilter(likesHighToLow)
        viewModel.addFilter(filterToAdd)

        return
    }
    if (filterToAdd.name == "likesDesc"
        && viewModel.filters.contains(likesLowToHigh))
    {
        viewModel.removeFilter(likesLowToHigh)
        viewModel.addFilter(filterToAdd)

        return
    }
    viewModel.addFilter(filterToAdd)
    return
}


@Composable
private fun SearchBarWrapper(viewModel: RecipesViewModel) {
    var filtersExpanded = remember { mutableStateOf(false) }

    Box(modifier = Modifier
        .background(color = Color.White)) {
        SearchBar(viewModel, filtersExpanded) {
            RecipeFilters(
                state=filtersExpanded.value,
                onDismissRequest = { filtersExpanded.value = false },
                viewModel = viewModel
            )
        }
    }
}

@Composable
private fun RecipeSearchResults(navController: NavHostController, viewModel: RecipesViewModel) {
    SearchResults(viewModel = viewModel) {
        ResultCard(
            title = it.title,
            bannerUrl = it.bannerUrl,
            recipeId = it.id,
            author = it.author,
            navController = navController
        )
    }
}
@Composable
private fun ResultCard(
    title: String,
    bannerUrl: String,
    recipeId: String,
    author: Author,
    navController: NavHostController
) {
    SearchResultCard(
        image= bannerUrl,
        onClick = { navigateToRecipe(recipeId, navController) }
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
            Column(horizontalAlignment = Alignment.End) {
                AuthorCardDisplay(author, textColor=Color.Black)
            }
        }
    }
}

private val SortFilters = listOf(
    Filter("dateAsce", "Oldest to Newest", false),
    Filter("dateDesc", "Newest to Oldest", false),
    Filter("likesAsce", "Popularity (Low to High)", false),
    Filter("likesDesc", "Popularity (High to Low)", false)
)
