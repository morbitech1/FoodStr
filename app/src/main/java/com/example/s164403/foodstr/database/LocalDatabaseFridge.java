package com.example.s164403.foodstr.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.s164403.foodstr.Fridge;
import com.example.s164403.foodstr.database.Model.Ingredient;
import com.example.s164403.foodstr.database.Model.Recipe;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

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

    public Map<Recipe, Double> searchRecipesByScore(SQLiteDatabase db, int numOfPeople, int limit) {
        Map<Recipe, Double> res = new LinkedHashMap();
        String query = " SELECT * FROM " +
                "(" +
                " SELECT "+ DatabaseRecipeIngredient.COL1 +", AVG(Score) as RepScore FROM" +
                "(" +
                " SELECT " + DatabaseRecipeIngredient.COL1 + ", case when Score > 100 then 100 else Score end as Score FROM (" +
                " SELECT "+ DatabaseRecipeIngredient.NAME +"."+ DatabaseRecipeIngredient.COL1 +"," +
                DatabaseRecipeIngredient.NAME +"."+ DatabaseRecipeIngredient.COL2 +"," +
                " ifnull(100*("+ LocalDatabaseFridge.NAME + "." + LocalDatabaseFridge.COL2 +")/(" + DatabaseRecipeIngredient.NAME +"."+ DatabaseRecipeIngredient.COL3 + "*" + numOfPeople + "), 0) as Score" +
                " FROM " + DatabaseRecipeIngredient.NAME +
                " LEFT OUTER JOIN "+ LocalDatabaseFridge.NAME +" on " + LocalDatabaseFridge.NAME + "." + LocalDatabaseFridge.COL1 + " = " + DatabaseRecipeIngredient.NAME + "." + DatabaseRecipeIngredient.COL2 +
                ")" +
                ")" +
                "GROUP by " + DatabaseRecipeIngredient.COL1 + " ORDER BY RepScore DESC LIMIT " + limit +
                ") JOIN " + DatabaseRecipe.NAME + " on "+ DatabaseRecipe.NAME +"."+ DatabaseRecipe.COL1 +" = " + DatabaseRecipeIngredient.COL1;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            Log.i(Fridge.TAG, Arrays.deepToString(cursor.getColumnNames()));
            do {
                Recipe recipe = new Recipe(cursor);
                res.put(recipe, cursor.getDouble(cursor.getColumnIndex("RepScore")));
            } while(cursor.moveToNext());
        }
        cursor.close();
        return res;
    }
}
