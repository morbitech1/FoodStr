package com.example.s164403.foodstr.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.s164403.foodstr.R;
import com.example.s164403.foodstr.database.Model.Ingredient;
import com.example.s164403.foodstr.database.Model.RecipeIngredientRelation;

/**
 * Created by s164403 on 6/14/2017.
 */

public class DatabaseRecipeIngredient extends SQLiteOpenHelper{

    public static final int VERSION = 4;
    public static final String NAME = "RecipeIngredientRelationship";
    public static final String COL1 = "recipe_id";
    public static final String COL2 = "ingredient_id";
    public static final String COL3 = "factor";
    private Context context;

    public DatabaseRecipeIngredient(Context context){
        super(context, context.getString(R.string.database_name),null,VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL( "CREATE TABLE IF NOT EXISTS " + NAME + " (" +
                COL1 + " INTEGER NOT NULL REFERENCES " + DatabaseRecipe.NAME + "("+DatabaseRecipe.COL1 + "),"+
                COL2 + " INTEGER NOT NULL REFERENCES " + DatabaseIngredient.NAME + "("+DatabaseIngredient.COL1 + "),"+
                COL3 + " INTEGER," +
                "PRIMARY KEY (" + COL1 + "," + COL2 + ")"+ ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + NAME);
        onCreate(db);
    }


    public void addRelation(RecipeIngredientRelation rir){
        DatabaseRecipe dbRecipe = new DatabaseRecipe(context);
        DatabaseIngredient dbIngredient = new DatabaseIngredient(context);
        long recipeId = dbRecipe.getId(rir.recipe.name);
        if(recipeId < 0)
            recipeId = dbRecipe.addRecipe(rir.recipe);
        rir.recipe.id = recipeId;

        for(Ingredient ingredient : rir.ingredients.keySet()){
            dbIngredient.addIngredient(ingredient);
            ContentValues cv = new ContentValues();
            cv.put(COL1, rir.recipe.id);
            cv.put(COL2, ingredient.id);
            cv.put(COL3, rir.ingredients.get(ingredient));
            getWritableDatabase().insert(NAME, null, cv);
        }
    }
}
