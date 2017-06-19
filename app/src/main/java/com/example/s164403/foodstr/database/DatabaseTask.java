package com.example.s164403.foodstr.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.example.s164403.foodstr.RecipeStep;
import com.example.s164403.foodstr.database.Model.Task;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by s164403 on 6/14/2017.
 */

public class DatabaseTask extends DatabaseTableDefinition {
    public static final String NAME = "task";
    public static final String COL1 = "id";
    public static final String COL2 = "recipe_id";
    public static final String COL3 = "duration";
    public static final String COL4 = "name";
    public static final String COL5 = "hot";
    public static final String COL6 = "hands";
    public static final String COL7 = "description";

    public DatabaseTask(){}
    public DatabaseTask(SQLiteDatabase db){
        this.db = db;
    }

    @Override
    public String getCreateQuery() {
        return "CREATE TABLE " + NAME + " (" +
                COL1 + " INTEGER PRIMARY KEY," +
                COL2 + " INTEGER NOT NULL REFERENCES "+ DatabaseRecipe.NAME + "(" + DatabaseRecipe.COL1 + ")," +
                COL3 + " INTEGER," +
                COL4 + " TEXT," +
                COL5 + " BOOLEAN," +
                COL6 + " BOOLEAN, " +
                COL7 + " TEXT)";
    }

    private Task fromCursor(Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndex(COL1));
        int recipeId = cursor.getInt(cursor.getColumnIndex(COL2));
        int duration = cursor.getInt(cursor.getColumnIndex(COL3));
        String name = cursor.getString(cursor.getColumnIndex(COL4));
        boolean hot = cursor.getInt(cursor.getColumnIndex(COL5)) != 0;
        boolean hands = cursor.getInt(cursor.getColumnIndex(COL6)) != 0;
        String description = cursor.getString(cursor.getColumnIndex(COL7));
        return new Task(id, recipeId, name,duration, hands, hot, description, db);
    }

    public boolean addTask(Task taskToAdd) {
        long id = taskToAdd.id;
        if (id <= 0 && !hasEntry(taskToAdd.name)) {
            ContentValues cv = new ContentValues();
            cv.put(COL2, taskToAdd.getRecipeId());
            cv.put(COL3, taskToAdd.duration);
            cv.put(COL4, taskToAdd.name);
            cv.put(COL5, taskToAdd.cariesOnHot);
            cv.put(COL6, taskToAdd.requireAttention);
            cv.put(COL7, taskToAdd.description);
            id = (int) db.insert(NAME, null, cv);
        }
        DatabasePreRequisite dpr = new DatabasePreRequisite(db);
        return id > 0 && dpr.setPreRequisites(id, taskToAdd.getPreRequisiteIds());
    }

    public List<RecipeStep> getStepsForRecipe(long recipeId) {
        Map<Long, RecipeStep> idStepMap = new HashMap<>();
        List<RecipeStep> steps = new LinkedList<>();
        List<Task> tasks = getTasksForRecipe(recipeId);
        for (Task task : tasks) {
            steps.add(task.getRecipeStep(idStepMap));
        }
        return steps;
    }

    private List<Task> getTasksForRecipe(long recipeId) {
        List<Task> res = new LinkedList<>();
        String query = "SELECT * FROM " + NAME +
                " WHERE " + COL2 + "=?";
        Cursor cursor = db.rawQuery(query, new String[] {Long.toString(recipeId)});
        if (cursor.moveToFirst()) {
            do {
                res.add(fromCursor(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return res;
    }

    public boolean hasEntry(String name) {
        Cursor cursor = db.rawQuery("SELECT " + COL1 + " FROM " + NAME + " WHERE " + COL4 +"= \""+name+"\"", null);
        boolean hasEntry = cursor.moveToFirst();
        cursor.close();
        return hasEntry;
    }

    public Task getEntry(long id) {
        Cursor cursor = db.rawQuery("SELECT * FROM " + NAME + " WHERE " + COL1 +"= \""+id+"\"", null);
        Task res = null;
        if (cursor.moveToFirst()) {
            res = fromCursor(cursor);
        }
        cursor.close();
        return res;
    }

    public List<Task> getPreRequisites(Long... ids) {
        List<Task> preRequisites = new LinkedList<>();

        List<String> idList = new LinkedList<>();
        for (Long id : ids) {
            idList.add(id.toString());
        }
        String[] strIds = idList.toArray(new String[idList.size()]);

        String query = "SELECT * FROM " + NAME
                + " WHERE " + COL1 + " IN (" + TextUtils.join(",", Collections.nCopies(ids.length, "?"))  + ")";
        Cursor cursor = db.rawQuery(query, strIds);
        if (cursor.moveToFirst()) {
            do {
                preRequisites.add(fromCursor(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return preRequisites;
    }

    @Override
    public String getDropQuery() {
        return "DROP TABLE IF EXISTS " + NAME;
    }

}
