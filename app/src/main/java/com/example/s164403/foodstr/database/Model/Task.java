package com.example.s164403.foodstr.database.Model;

import java.util.List;

/**
 * Created by aMoe on 14-06-2017.
 */

public class Task {
    public int id;
    public String name;
    public boolean requireAttention;
    public boolean cariesOnHot;

    public List<Integer> preRequisiteIds;

    public List<Task> getPreRequisites() {
        throw new Error();
    }

    public Recipe getRecipe() {
        throw new Error();
    }

    public Task(int id, String name, boolean requireAttention, boolean cariesOnHot, List<Integer> preRequisiteIds) {
        this.id = id;
        this.name = name;
        this.requireAttention = requireAttention;
        this.cariesOnHot = cariesOnHot;
        this.preRequisiteIds = preRequisiteIds;
    }
}
