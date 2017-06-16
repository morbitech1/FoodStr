package com.example.s164403.foodstr.database;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by aMoe on 15-06-2017.
 */

public abstract class DatabaseTableDefinition {
    SQLiteDatabase db;

    public void setDatabase(SQLiteDatabase db){
        this.db = db;
    }
    abstract String getCreateQuery();
    abstract String getDropQuery();
}
