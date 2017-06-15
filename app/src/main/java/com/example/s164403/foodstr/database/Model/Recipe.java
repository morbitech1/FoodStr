package com.example.s164403.foodstr.database.Model;

import java.util.List;

/**
 * Created by aMoe on 14-06-2017.
 */

public class Recipe {

    public long id;
    public String name;
    public String pictureUrl;
    public String description;

    public Recipe(long id, String name, String pictureUrl, String description) {
        this.id = id;
        this.name = name;
        this.pictureUrl = pictureUrl;
        this.description = description;
    }

    public Recipe(String name, String pictureUrl, String description) {
        this(-1, name, pictureUrl, description);
    }
    public List<Integer> relationIds;

    public List<RecipeIngredientRelation> getIngredientRelations() {
        throw new Error();
    }

    public List<Task> getTasks() {
        throw new Error();
    }
}
