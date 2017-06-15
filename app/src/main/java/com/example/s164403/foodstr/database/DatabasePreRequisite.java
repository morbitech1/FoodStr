package com.example.s164403.foodstr.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.s164403.foodstr.R;
import com.example.s164403.foodstr.database.Model.Task;

/**
 * Created by s164403 on 6/14/2017.
 */

public class DatabasePreRequisite   extends DatabaseTableDefinition{

    public static final String NAME = "prerequisites";
    public static final String COL1 = "precedingTask";
    public static final String COL2 = "task";

    public DatabasePreRequisite(SQLiteDatabase db){
        super(db);
    }

    @Override
    public String getDropQuery() {
        return "DROP TABLE IF EXISTS " + NAME;
    }

    @Override
    public String getCreateQuery() {
        return "CREATE TABLE " + NAME + " (" +
                COL1 + " INTEGER NOT NULL REFERENCES "+ DatabaseTask.NAME + "(" + DatabaseTask.COL1 + ")," +
                COL2 + " INTEGER NOT NULL REFERENCES "+ DatabaseTask.NAME + "(" + DatabaseTask.COL1 + ")," +
                "PRIMARY KEY (" + COL1 + ", " + COL2 + ")" +
                ")";
    }

}
