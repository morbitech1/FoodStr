package com.example.s164403.foodstr.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.s164403.foodstr.database.Model.Recipe;

/**
 * Created by s164403 on 6/14/2017.
 */

public class DatabaseRecipe extends DatabaseTableDefinition{
    public static final String NAME = "Recipe";
    public static final String COL1 = "id";
    public static final String COL2 = "name";
    public static final String COL3 = "pictureURL";
    public static final String COL4 = "description";

    public DatabaseRecipe(){}
    public DatabaseRecipe(SQLiteDatabase db){
        this.db = db;
    }

    @Override
    public String getCreateQuery() {
        return "CREATE TABLE IF NOT EXISTS " + NAME + " (" +
                COL1 + " INTEGER PRIMARY KEY,"+
                COL2 + " TEXT NOT NULL," +
                COL3 + " TEXT," +
                COL4 + " TEXT )";
    }

    @Override
    public String getDropQuery() {
        return "DROP TABLE IF EXISTS " + NAME;
    }

    public long addRecipe(Recipe recipe){
        ContentValues cv = new ContentValues();
        cv.put(COL2, recipe.name);
        cv.put(COL3, recipe.pictureUrl);
        cv.put(COL4, recipe.description);
        return db.insert(NAME, null, cv);
    }

    public boolean hasRecipe(String name){
        Cursor cursor = db.rawQuery("SELECT * FROM " + NAME + " WHERE " + COL2 + "=\""+name+"\"", null);
        boolean check = cursor.moveToFirst();
        cursor.close();
        return check;
    }

    public long getId(SQLiteDatabase db, String name){
        Cursor cursor = db.rawQuery("SELECT * FROM " + NAME + " WHERE " + COL2 + "=\""+name+"\"", null);
        long id = -1;
        if(cursor.moveToFirst())
            id = cursor.getLong(cursor.getColumnIndex(COL1));
        cursor.close();
        return id;
    }
}
