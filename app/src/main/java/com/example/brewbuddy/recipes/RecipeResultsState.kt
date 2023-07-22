import com.example.brewbuddy.domain.model.Recipe
import com.example.brewbuddy.domain.model.RecipeMetadata

data class RecipeResultsState (
    val isLoading: Boolean = false,
    val results: List<RecipeMetadata> = emptyList(),
    val error: String = "",
)