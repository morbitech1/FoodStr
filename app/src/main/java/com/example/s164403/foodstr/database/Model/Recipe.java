package com.example.s164403.foodstr.database.Model;

import java.util.List;

/**
 * Created by aMoe on 14-06-2017.
 */

public class Recipe {

    public int id;
    public String name;
    public String pictureUrl;

    public Recipe(int id, String name, String pictureUrl) {
        this.id = id;
        this.name = name;
        this.pictureUrl = pictureUrl;
    }

    public List<Integer> relationIds;

    public List<RecipeIngredientRelation> getIngredientRelations() {
        throw new Error();
    }

    public List<Task> getTasks() {
        throw new Error();
    }
}
