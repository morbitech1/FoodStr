package com.example.s164403.foodstr.database.Model;

import java.util.List;

/**
 * Created by aMoe on 14-06-2017.
 */

public class Ingredient {
    public int id;
    public int name;
    public String[] aliases;
    public PrimaryUnit primaryUnit;

    public List<Integer> relationIds;

    public List<RecipeIngredientRelation> getRecipeRelations() {
        throw new Error();
    }
}
