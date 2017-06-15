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

    /**
     *
     * @param db
     * @param id for ingredient
     * @param amount of the given ingredient
     * @return true if added false if updated
     */
    public boolean addIngredient(SQLiteDatabase db, long id, double amount){
        boolean added = false;
        ContentValues cv = new ContentValues();
        cv.put(COL1, id);
        cv.put(COL2, amount);
        Cursor cursor = db.rawQuery("SELECT * FROM " + NAME + " WHERE " +COL1 + "="+id, null);
        if(cursor.moveToFirst()){
            cv.put(COL2, amount + cursor.getDouble(cursor.getColumnIndex(COL2)));
            db.update(NAME, cv, COL1+ "="+id, null);
        }else {
            db.insert(NAME, null, cv);
            added = true;
        }
        cursor.close();
        return added;
    }

    public double getAmount(SQLiteDatabase db, long id){
        Cursor cursor = db.rawQuery("SELECT * FROM " + NAME + " WHERE "+COL1 +"="+id,null);
        double res = -1;
        if(cursor.moveToFirst())
            res = cursor.getDouble(cursor.getColumnIndex(COL2));
        return res;
    }
}
