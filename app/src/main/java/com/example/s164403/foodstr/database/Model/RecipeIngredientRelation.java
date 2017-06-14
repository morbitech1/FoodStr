package com.example.s164403.foodstr.database.Model;

/**
 * Created by aMoe on 14-06-2017.
 */

public class RecipeIngredientRelation {
    public Recipe recipe;
    public Ingredient ingredient;

    // Number of primaryUnits in ingredient per person
    public int factor;
}
