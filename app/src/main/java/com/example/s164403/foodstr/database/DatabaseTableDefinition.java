package com.example.s164403.foodstr.database;

import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by aMoe on 15-06-2017.
 */

public interface DatabaseTableDefinition {
    String getCreateQuery();
    String getDropQuery();
}
