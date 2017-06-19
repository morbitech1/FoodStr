package com.example.s164403.foodstr.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.s164403.foodstr.FridgeFragment;
import com.example.s164403.foodstr.database.Model.Ingredient;
import com.example.s164403.foodstr.database.Model.Recipe;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Morbi95 on 14-Jun-17.
 */

public class LocalDatabaseFridge extends DatabaseTableDefinition{
    public static final String NAME = "fridge";
    public static final String COL1 = "ingredientId";
    public static final String COL2 = "amount";

    public LocalDatabaseFridge(){}
    public LocalDatabaseFridge(SQLiteDatabase db){
        this.db = db;
    }

    @Override
    public String getCreateQuery() {
        return "CREATE TABLE "+ NAME +
                "("+
                COL1 + " INTEGER PRIMARY KEY,"+
                COL2 + " DECIMAL(10,2)"+
                ")";
    }

    @Override
    public String getDropQuery() {
        return "DROP TABLE IF EXISTS " + NAME;
    }

    public HashMap<Ingredient, Double> getIngredientsInFridge(){
        Cursor cursor = db.rawQuery("SELECT * FROM " + NAME, null);
        HashMap<Ingredient,Double> res = new HashMap<>();
        DatabaseIngredient databaseIngredient = new DatabaseIngredient(db);
        if(cursor.moveToFirst())
            do{
                res.put(databaseIngredient.getIngredient(cursor.getInt(0)), cursor.getDouble(1));
            }while (cursor.moveToNext());
        cursor.close();
        return res;
    }

    public long remove(long id){
        return db.delete(NAME, COL1 + "="+id, null);
    }

    /**
     *
     * @param id for ingredient
     * @param amount of the given ingredient
     * @return true if added false if updated
     */
    public boolean addIngredient(long id, double amount){
        boolean added = false;
        ContentValues cv = new ContentValues();
        cv.put(COL1, id);
        cv.put(COL2, amount);
        Cursor cursor = db.rawQuery("SELECT * FROM " + NAME + " WHERE " +COL1 + "="+id, null);
        if(cursor.moveToFirst()){
            double current = cursor.getDouble(cursor.getColumnIndex(COL2));
            cv.put(COL2, amount + (current >= 0 ? current : 0));
            db.update(NAME, cv, COL1+ "="+id, null);
        }else {
            db.insert(NAME, null, cv);
            added = true;
        }
        cursor.close();
        return added;
    }

    public double getAmount(long id){
        Cursor cursor = db.rawQuery("SELECT * FROM " + NAME + " WHERE "+COL1 +"="+id,null);
        double res = 0;
        if(cursor.moveToFirst())
            res = cursor.getDouble(cursor.getColumnIndex(COL2));
        cursor.close();
        return res;
    }

    public void changeAmount(long id, double amount){
        ContentValues cv = new ContentValues();
        cv.put(COL2, amount);
        db.update(NAME, cv, COL1 + "=" + id,null);
    }
}
