package com.example.s164403.foodstr.database.Model;

import java.util.HashMap;

/**
 * Created by aMoe on 14-06-2017.
 */

public class RecipeIngredientRelation {
    public Recipe recipe;
    public HashMap<Ingredient, Double> ingredients;


    public RecipeIngredientRelation(Recipe recipe, HashMap<Ingredient, Double> ingredients) {
        this.recipe = recipe;
        this.ingredients = ingredients;

    }
}
