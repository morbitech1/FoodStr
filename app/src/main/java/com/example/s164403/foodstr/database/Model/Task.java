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
}
