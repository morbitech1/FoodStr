package com.example.s164403.foodstr.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.s164403.foodstr.database.Model.Ingredient;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by s164403 on 6/14/2017.
 */

public class DatabaseIngredient extends DatabaseTableDefinition {
    public static final String NAME = "Ingredients";
    public static final String COL1 = "id";
    public static final String COL2 = "name";
    public static final String COL3 = "alias";
    // Maps to enum
    public static final String COL4 = "primaryUnit";

    public DatabaseIngredient(){}
    public DatabaseIngredient(SQLiteDatabase db){
        this.db=db;
    }

    @Override
    public String getCreateQuery() {
        return "CREATE TABLE " + NAME + " (" +
                COL1 + " INTEGER PRIMARY KEY,"+
                COL2 + " CHAR(45),"+
                COL3 + " TEXT,"+
                COL4 + " CHAR(10)," +
                "UNIQUE("+COL2+" COLLATE NOCASE)" +
                ")";
    }

    @Override
    public String getDropQuery() {
        return "DROP TABLE IF EXISTS " + NAME;
    }

    public List<Ingredient> getAllIngredients(){
        Cursor cursor = db.rawQuery("SELECT * FROM "+NAME ,null);
        List<Ingredient> result = new ArrayList<>();
        if(cursor.moveToFirst())
            do{
                result.add(new Ingredient(cursor));
            }while(cursor.moveToNext());
        cursor.close();
        return result;
    }

    public boolean hasIngredient(String name){
        Cursor cursor = db.rawQuery("SELECT * FROM " + NAME + " WHERE " + COL2 +"= \""+name+"\"", null);
        boolean hasIngredient = cursor.moveToFirst();
        cursor.close();
        return hasIngredient;
    }

    public long getId( String name){
        Cursor cursor = db.rawQuery("SELECT "+COL1+" FROM " + NAME + " WHERE " + COL2 +"= \""+name+"\"", null);
        long id;
        if(cursor.moveToFirst())
            id = cursor.getLong(0);
        else
            id = -1;
        cursor.close();
        return id;
    }

    public long addIngredient(Ingredient ingredient){
        long id = getId(ingredient.name);
        if (id < 0) {
            ContentValues cv = new ContentValues();
            cv.put(COL2, ingredient.name);
            cv.put(COL3, ingredient.getAliasCsv());
            cv.put(COL4, ingredient.primaryUnit.toString());
            id = (int) db.insert(NAME, null, cv);
        }
        ingredient.id = id;
        return id;
    }

    /**
     * Finds an ingredient in the database, here based on id
     * @param id to look up
     * @return null if id not present an ingredient otherwise
     */
    public Ingredient getIngredient( long id){
        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + NAME + " WHERE " + COL1 + " = " + id,null);
        Ingredient res = null;
        if(cursor.moveToFirst())
            res =  new Ingredient(cursor);
        cursor.close();
        return res;
    }

    public Ingredient getIngredient(String name){
        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + NAME + " WHERE " + COL2 + "=\"" + name + "\""
                ,null);
        Ingredient res = null;
        if(cursor.moveToFirst())
            res = new Ingredient(cursor);
        cursor.close();
        return res;
    }



}
