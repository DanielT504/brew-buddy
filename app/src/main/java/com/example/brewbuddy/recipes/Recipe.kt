package com.example.brewbuddy.recipes;

import android.nfc.Tag
import com.example.brewbuddy.R
import kotlin.collections.ArrayList

public class Recipe {
    var recipeName: String = "";
    var description: String = "";
    var ingredientList: List<IngredientSection> = listOf();
    var thumbnail: Int = R.drawable.default_recipe;
    var tags: List<TagType> = listOf();
    var backgroundImage: Any = "";
    var preparationSteps: List<PreparationStep> = listOf();

    constructor(
                 name: String = "",
                 desc: String = "",
                 recipeList: List<IngredientSection> = listOf(),
                 tags: List<TagType> = listOf(),
                 backgroundImage: Any = "",
                 preparationSteps: List<PreparationStep> = listOf(),
    ){
        this.recipeName = name;
        this.description = desc;
        this.ingredientList = recipeList;
        this.tags = tags;
        this.backgroundImage = backgroundImage;
        this.preparationSteps = preparationSteps;
    }

    fun getName(): String {
        return recipeName
    }


    fun getThumbNail(): Int {
        return thumbnail;
    }

}
