package com.example.s164403.foodstr.database.Model;

import android.database.Cursor;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by aMoe on 14-06-2017.
 */

public class RecipeIngredientRelation {
    final public Recipe recipe;
    final public Map<Ingredient, Double> ingredients;

    // Number of primaryUnits in ingredient per person
    public int factor;

    public RecipeIngredientRelation(Recipe recipe, Map<Ingredient, Double> ingredients) {
        this.recipe = recipe;
        this.ingredients = ingredients;
    }

}
