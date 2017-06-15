package com.example.s164403.foodstr.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.s164403.foodstr.R;
import com.example.s164403.foodstr.database.Model.Ingredient;

import java.util.HashMap;

/**
 * Created by Morbi95 on 14-Jun-17.
 */

public class LocalDatabaseFridge implements DatabaseTableDefinition{
    private static final String NAME = "fridge";
    private static final String COL1 = "ingredientId";
    private static final String COL2 = "amount";

    @Override
    public String getCreateQuery() {
        return "CREATE TABLE "+ NAME +
                "("+
                COL1 + " PRIMARY KEY,"+
                COL2 + " DECIMAL(10,2)"+
                ")";
    }

    @Override
    public String getDropQuery() {
        return "DROP TABLE IF EXISTS " + NAME;
    }

    public HashMap<Ingredient, Integer> getIngredientsInFridge(SQLiteDatabase db){
        Cursor cursor = db.rawQuery("SELECT * FROM " + NAME, null);
        HashMap<Ingredient,Integer> res = new HashMap<>();
        DatabaseIngredient databaseIngredient = new DatabaseIngredient();
        if(cursor.moveToFirst())
            do{
                res.put(databaseIngredient.getIngredient(db, cursor.getInt(0)), cursor.getInt(1));
            }while (cursor.moveToNext());
        cursor.close();
        return res;
    }

    public long remove(SQLiteDatabase db, long id){
        return db.delete(NAME, COL1 + "="+id, null);
    }

    public long addIngredient(SQLiteDatabase db, long id, double amount){
        ContentValues cv = new ContentValues();
        cv.put(COL1, id);
        cv.put(COL2, amount);
        return db.insert(NAME, null, cv);
    }
}
