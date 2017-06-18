package com.example.s164403.foodstr.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by s164403 on 6/14/2017.
 */

public class DatabasePreRequisite   extends DatabaseTableDefinition{

    public static final String NAME = "prerequisites";
    public static final String COL1 = "precedingTask";
    public static final String COL2 = "task";

    public DatabasePreRequisite(){}
    public DatabasePreRequisite(SQLiteDatabase db){
        this.db=db;
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

    public List<Long> getPreRequisiteIds(long taskId) {
        List<Long> ids = new LinkedList<>();
        String query = "SELECT " + COL1 +" FROM " + NAME
                + " WHERE " + COL2 + " = ?";
        Cursor cursor = db.rawQuery(query, new String[] { Long.toString(taskId)});
        if (cursor.moveToFirst()) {
            do {
                ids.add(cursor.getLong(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return ids;
    }

    public boolean setPreRequisites(long precedingTaskId, List<Long> preRequisites) {
        boolean success = true;
        for (long otherId : preRequisites) {
            long id = 0;
            if (otherId > 0 ) {
                ContentValues cv = new ContentValues();
                cv.put(COL1, otherId);
                cv.put(COL2, precedingTaskId);
                id = (int) db.insert(NAME, null, cv);
                success &= id > 0;
            }
        }
        return success;
    }

}
