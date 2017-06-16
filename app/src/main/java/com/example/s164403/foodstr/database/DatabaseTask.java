package com.example.s164403.foodstr.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.s164403.foodstr.R;

/**
 * Created by s164403 on 6/14/2017.
 */

public class DatabaseTask extends DatabaseTableDefinition {
    public static final String NAME = "task";
    public static final String COL1 = "id";
    public static final String COL2 = "recipe_id";
    public static final String COL3 = "duration";
    public static final String COL4 = "hot";
    public static final String COL5 = "hands";

    public DatabaseTask(SQLiteDatabase db){
        super(db);
    }

    @Override
    public String getCreateQuery() {
        return "CREATE TABLE " + NAME + " (" +
                COL1 + " INTEGER PRIMARY KEY," +
                COL2 + " INTEGER NOT NULL REFERENCES "+ DatabaseRecipe.NAME + "(" + DatabaseRecipe.COL1 + ")," +
                COL3 + " INTEGER," +
                COL4 + " BOOLEAN," +
                COL5 + " BOOLEAN)";
    }

    @Override
    public String getDropQuery() {
        return "DROP TABLE IF EXISTS " + NAME;
    }

}
