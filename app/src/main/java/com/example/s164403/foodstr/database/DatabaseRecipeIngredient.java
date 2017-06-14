package com.example.s164403.foodstr.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.s164403.foodstr.R;

/**
 * Created by s164403 on 6/14/2017.
 */

public class DatabaseRecipeIngredient extends SQLiteOpenHelper{

    public static final int VERSION = 1;
    public static final String NAME = "RecipeIngredientRelationship";
    public static final String COL1 = "ingredientId";
    public static final String COL2 = "recipeId";
    public static final String COL3 = "amount";

    public DatabaseRecipeIngredient(Context context){
        super(context, context.getString(R.string.database_name),null,VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL( "CREATE TABLE " + NAME + " (" +
                COL1 + " INTEGER FOREIGN KEY,"+
                COL2 + " INTEGER FOREIGN KEY," +
                COL3 + " INTEGER)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DELETE TABLE IF EXISTS " + NAME);
        onCreate(db);
    }
}
