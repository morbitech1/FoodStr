package com.example.s164403.foodstr.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.s164403.foodstr.R;
import com.example.s164403.foodstr.database.Model.Ingredient;
import com.example.s164403.foodstr.database.Model.PrimaryUnit;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by s164403 on 6/14/2017.
 */

public class DatabaseIngredient extends SQLiteOpenHelper{
    public static final int VERSION = 2;
    public static final String NAME = "Ingredients";
    public static final String COL1 = "id";
    public static final String COL2 = "name";
    public static final String COL3 = "alias";
    // Maps to enum
    public static final String COL4 = "primaryUnit";

    public DatabaseIngredient(Context context){
        super(context, context.getString(R.string.database_name), null, VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL( "CREATE TABLE " + NAME + " (" +
                COL1 + " INTEGER PRIMARY KEY,"+
                COL2 + " CHAR(45) UNIQUE,"+
                COL3 + " TEXT,"+
                COL4 + " CHAR(10))"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + NAME);
        onCreate(db);
    }

    public List<Ingredient> getAllIngredients(){
        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM "+NAME ,null);
        List<Ingredient> result = new ArrayList<>();
        if(cursor.moveToFirst())
            do{
                result.add(makeIngredient(cursor));
            }while(cursor.moveToNext());
        cursor.close();
        return result;
    }

    public void addIngredients(List<Ingredient> ingredients){
        for(Ingredient ingredient : ingredients){
            addIngredient(ingredient);
        }
    }

    public boolean hasIngredient(String name){
        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM " + NAME + " WHERE " + COL2 +"= \""+name+"\"", null);
        boolean hasIngredient = cursor.moveToFirst();
        cursor.close();
        return hasIngredient;
    }

    public long addIngredient(Ingredient ingredient){
        if (!hasIngredient(ingredient.name)) {
            ContentValues cv = new ContentValues();
            cv.put(COL2, ingredient.name);
            cv.put(COL3, ingredient.getAliasCsv());
            cv.put(COL4, ingredient.primaryUnit.toString());
            return getWritableDatabase().insert(NAME, null, cv);
        }
        return -1;
    }

    /**
     * Finds an ingredient in the database, here based on id
     * @param id to look up
     * @return null if id not present an ingredient otherwise
     */
    public Ingredient getIngredient(int id){
        Cursor cursor = getReadableDatabase().rawQuery(
                "SELECT * FROM " + NAME + " WHERE " + COL1 + " = " + id,null);
        if(cursor.moveToFirst())
            return makeIngredient(cursor);
        return null;
    }

    public Ingredient getIngredient(String name){
        Cursor cursor = getReadableDatabase().rawQuery(
                "SELECT * FROM " + NAME + " WHERE " + COL2 + "=\"" + name + "\""
                ,null);
        if(cursor.moveToFirst())
            return makeIngredient(cursor);
        return null;
    }

    private Ingredient makeIngredient(Cursor cursor){
        return new Ingredient(
                cursor.getInt(0),
                cursor.getString(1),
                Ingredient.parseCsvAlias(cursor.getString(2)),
                PrimaryUnit.valueOf(cursor.getString(3)));
    }

}
