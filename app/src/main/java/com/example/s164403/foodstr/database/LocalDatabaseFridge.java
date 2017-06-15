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

public class LocalDatabaseFridge extends SQLiteOpenHelper{
    private final Context context;
    private static final int VERSION = 2;
    private static final String NAME = "fridge";
    private static final String COL1 = "ingredientId";
    private static final String COL2 = "amount";

    public LocalDatabaseFridge(Context context){
        super(context, context.getString(R.string.database_name), null,VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS "+ NAME +
                "("+
                COL1 + " INTEGER UNIQUE,"+
                COL2 + " DECIMAL(10,2)"+
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + NAME);
        onCreate(db);
    }

    public HashMap<Ingredient, Integer> getIngredientsInFridge(){
        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM " + NAME, null);
        HashMap<Ingredient,Integer> res = new HashMap<>();
        DatabaseIngredient databaseIngredient = new DatabaseIngredient(context);
        if(cursor.moveToFirst())
            do{
                res.put(databaseIngredient.getIngredient(cursor.getInt(0)), cursor.getInt(1));
            }while (cursor.moveToNext());
        cursor.close();
        return res;
    }

    public int remove(int id){
        return getWritableDatabase().delete(NAME, COL1 + "="+id, null);
    }

    public long addIngredient(int id, double amount){
        ContentValues cv = new ContentValues();
        cv.put(COL1, id);
        cv.put(COL2, amount);
        return getWritableDatabase().insert(NAME, null, cv);
    }
}
