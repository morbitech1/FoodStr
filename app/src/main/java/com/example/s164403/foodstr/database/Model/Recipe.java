package com.example.s164403.foodstr.database.Model;

import android.database.Cursor;

import com.example.s164403.foodstr.database.DatabaseRecipe;

import java.util.List;

/**
 * Created by aMoe on 14-06-2017.
 */

public class Recipe {

    public long id;
    final public String name;
    final public String pictureUrl;
    final public String description;

    public Recipe(long id, String name, String pictureUrl, String description) {
        this.id = id;
        this.name = name;
        this.pictureUrl = pictureUrl;
        this.description = description;
    }

    public Recipe(Cursor cursor) {
        this(
                cursor.getLong(cursor.getColumnIndex(DatabaseRecipe.COL1)),
                cursor.getString(cursor.getColumnIndex(DatabaseRecipe.COL2)),
                cursor.getString(cursor.getColumnIndex(DatabaseRecipe.COL3)),
                cursor.getString(cursor.getColumnIndex(DatabaseRecipe.COL4))
        );
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

    @Override
    public String toString() {
        return ""+id;
    }
}
