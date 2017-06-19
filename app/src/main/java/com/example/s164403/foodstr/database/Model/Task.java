package com.example.s164403.foodstr.database.Model;

import android.database.sqlite.SQLiteDatabase;

import com.example.s164403.foodstr.RecipeStep;
import com.example.s164403.foodstr.database.DatabasePreRequisite;
import com.example.s164403.foodstr.database.DatabaseRecipe;
import com.example.s164403.foodstr.database.DatabaseTask;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by aMoe on 14-06-2017.
 */

public class Task {
    public long id;
    public String name;
    public int duration;
    public boolean requireAttention;
    public boolean cariesOnHot;
    public String description;
    private long recipeId;
    private Recipe recipeCache;
    private SQLiteDatabase db;


    private List<Long> preRequisiteIds;

    public List<Task> getPreRequisites() {
        DatabaseTask dbt = new DatabaseTask(db);
        return dbt.getPreRequisites(preRequisiteIds.toArray(new Long[preRequisiteIds.size()]));
    }

    public Recipe getRecipe() {
        if (recipeCache == null){
            recipeCache = new DatabaseRecipe().getRecipe(recipeId);
        }
        return recipeCache;
    }

    public Task(long id, long recipeId, String name, int duration, boolean requireAttention, boolean cariesOnHot, List<Long> preRequisiteIds, String description) {
        this.id = id;
        this.name = name;
        this.duration = duration;
        this.requireAttention = requireAttention;
        this.cariesOnHot = cariesOnHot;
        this.recipeId = recipeId;
        this.preRequisiteIds = preRequisiteIds;
        this.description = description;
    }

    public Task(long id, long recipeId, String name, int duration, boolean requireAttention, boolean cariesOnHot, String description, SQLiteDatabase db) {
        this.id = id;
        this.name = name;
        this.duration = duration;
        this.requireAttention = requireAttention;
        this.cariesOnHot = cariesOnHot;
        this.recipeId = recipeId;
        this.preRequisiteIds = preRequisiteIds;
        this.description = description;
        this.db = db;
    }

    public RecipeStep getRecipeStep(Map<Long, RecipeStep> cacheMap) {
        List<Task> preRequisite = getPreRequisites();
        List<RecipeStep> preRequisiteSteps = new LinkedList<>();
        RecipeStep res = cacheMap.get(id);
        for (Task task : preRequisite) {
            RecipeStep step = cacheMap.get(task.id);
            if (step == null) {
                step = task.getRecipeStep(cacheMap);
                cacheMap.put(task.id, step);
            }
            preRequisiteSteps.add(step);
            cacheMap.put(id, step);
        }
        if (res != null) {
            res.clearPreRequisites();
            for(RecipeStep recipeStep : preRequisiteSteps)
                res.addPrerequisite(recipeStep);
        } else {
            res = new RecipeStep(cariesOnHot, duration, requireAttention, preRequisiteSteps, name);
            cacheMap.put(id, res);
        }
        return res;
    }

    public long getRecipeId() {
        return recipeId;
    }

    public RecipeStep getRecipeStep() {
        return getRecipeStep(new HashMap<Long, RecipeStep>());
    }


    public List<Long> getPreRequisiteIds() {
        return preRequisiteIds;
    }
}
