package com.example.s164403.foodstr.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.s164403.foodstr.R;
import com.example.s164403.foodstr.database.Model.Ingredient;
import com.example.s164403.foodstr.database.Model.RecipeIngredientRelation;

/**
 * Created by s164403 on 6/14/2017.
 */

public class DatabaseRecipeIngredient implements DatabaseTableDefinition{
    public static final String NAME = "RecipeIngredientRelationship";
    public static final String COL1 = "recipe_id";
    public static final String COL2 = "ingredient_id";
    public static final String COL3 = "factor";

    @Override
    public String getCreateQuery() {
        return  "CREATE TABLE IF NOT EXISTS " + NAME + " (" +
                COL1 + " INTEGER NOT NULL REFERENCES " + DatabaseRecipe.NAME + "("+DatabaseRecipe.COL1 + "),"+
                COL2 + " INTEGER NOT NULL REFERENCES " + DatabaseIngredient.NAME + "("+DatabaseIngredient.COL1 + "),"+
                COL3 + " INTEGER," +
                "PRIMARY KEY (" + COL1 + "," + COL2 + ")"+ ")";
    }

    @Override
    public String getDropQuery() {
        return "DROP TABLE IF EXISTS " + NAME;
    }


    public void addRelation(SQLiteDatabase db, RecipeIngredientRelation rir){
        DatabaseRecipe dbRecipe = new DatabaseRecipe();
        DatabaseIngredient dbIngredient = new DatabaseIngredient();
        long recipeId = dbRecipe.getId(db, rir.recipe.name);
        if(recipeId < 0)
            recipeId = dbRecipe.addRecipe(db, rir.recipe);
        rir.recipe.id = recipeId;

        for(Ingredient ingredient : rir.ingredients.keySet()){
            dbIngredient.addIngredient(db, ingredient);
            if(!hasEntry(db, rir.recipe.id, ingredient.id)) {
                ContentValues cv = new ContentValues();
                cv.put(COL1, rir.recipe.id);
                cv.put(COL2, ingredient.id);
                cv.put(COL3, rir.ingredients.get(ingredient));
                db.insert(NAME, null, cv);
            }
        }
    }

    public boolean hasEntry(SQLiteDatabase db, long recipeID, long ingredientID){
        Cursor cursor = db.rawQuery("SELECT * FROM "+ NAME + " WHERE " + COL1 + "="+recipeID + " AND " + COL2 + "="+ingredientID, null);
        boolean check = cursor.moveToFirst();
        cursor.close();
        return check;
    }
}
