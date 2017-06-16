package com.example.s164403.foodstr.database;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.s164403.foodstr.R;

import java.util.List;

/**
 * Created by aMoe on 15-06-2017.
 */

public class MainDatabaseHelper extends SQLiteOpenHelper {
    public static final int VERSION = 3;

    private final DatabaseTableDefinition[] definitions = {
            new DatabaseIngredient(),
            new DatabaseRecipe(),
            new DatabaseTask(),
            new DatabaseRecipeIngredient(),
            new LocalDatabaseFridge(),
            new DatabasePreRequisite()};

    public MainDatabaseHelper(Context context){
        super(context,context.getString(R.string.database_name),null, VERSION);
        for(DatabaseTableDefinition dbt : definitions){
            dbt.setDatabase(getWritableDatabase());
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for (DatabaseTableDefinition definition : definitions) {
            db.execSQL(definition.getCreateQuery());
        }
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for (DatabaseTableDefinition definition : definitions) {
            db.execSQL(definition.getDropQuery());
        }
        onCreate(db);
    }
}
