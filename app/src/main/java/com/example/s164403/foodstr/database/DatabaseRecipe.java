package com.example.s164403.foodstr.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.s164403.foodstr.R;
import com.example.s164403.foodstr.database.Model.Recipe;

/**
 * Created by s164403 on 6/14/2017.
 */

public class DatabaseRecipe extends SQLiteOpenHelper{
    public static final int VERSION = 1;
    public static final String NAME = "Recipe";
    public static final String COL1 = "id";
    public static final String COL2 = "name";
    public static final String COL3 = "pictureURL";

    public DatabaseRecipe(Context context){
        super(context, context.getString(R.string.database_name),null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL( "CREATE TABLE " + NAME + " (" +
                COL1 + " INTEGER PRIMARY KEY,"+
                COL2 + " TEXT," +
                COL3 + " TEXT)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + NAME);
        onCreate(db);
    }

    public long addRecipe(Recipe recipe){
        ContentValues cv = new ContentValues();
        cv.put(COL2, recipe.name);
        cv.put(COL3, recipe.pictureUrl);
        return getWritableDatabase().insert(NAME, null, cv);
    }
}
