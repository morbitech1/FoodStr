package com.example.s164403.foodstr;

import com.example.s164403.foodstr.database.Model.Recipe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jonathan on 14-06-2017.
 */

public class RecipeStep {
    private boolean hot;
    private int time = 10;
    private boolean hand;
    private List<RecipeStep> prerequisites = new ArrayList<RecipeStep>();
    private List<RecipeStep> predecessors = new ArrayList<RecipeStep>();
    private String name = "";
    private String description = "";

    private int starttime;
    private int line;

    public RecipeStep(String name){
        setName(name);
    }
    public RecipeStep(){};

    public RecipeStep(boolean hot, int time, boolean hand, List<RecipeStep> prerequisites, String name, String description) {
        this.hot = hot;
        this.time = time;
        this.hand = hand;
        this.name = name;
        this.predecessors = new ArrayList<>();
        this.prerequisites = new ArrayList<>();
        this.description = description;
        for (RecipeStep step : prerequisites){
            addPrerequisite(step);
        }

    }

    public void setName(String name) {this.name = name;}
    public String getName() {return name;}

    public void setDescription(String desc) {this.description = desc;}
    public String getDescription() {return description;}

    public List<RecipeStep> getPrerequisites() {
        return prerequisites;
    }
    public List<RecipeStep> getPredecessors() {
        return predecessors;
    }
    public void addPrerequisite(RecipeStep prerequisite) {
        prerequisites.add(prerequisite);
        prerequisite.addPredecessor(this);
    }
    public void removePrerequisite(RecipeStep step){
        prerequisites.remove(step);
        step.removePredecessor(this);
    }

    public void clearPreRequisites(){
        for (RecipeStep step : prerequisites) {
            step.predecessors.remove(this);
        }
        prerequisites.clear();
    }

    private void addPredecessor(RecipeStep step) {
        predecessors.add(step);
    }
    private void removePredecessor(RecipeStep step) {
        predecessors.remove(step);
    }

    public boolean isPrerequisite(RecipeStep step){
        return prerequisites.contains(step);
    }
    public boolean isPredecessor(RecipeStep step){
        return predecessors.contains(step);
    }

    public void setHot(boolean hot){this.hot = hot;}
    public boolean getHot(){return hot;}

    public void setNeedsHand(boolean hand){this.hand = hand;}
    public boolean getNeedsHand(){return hand;}

    public void setTime(int time){this.time = time;}
    public int getTime(){return time;}

    public void setStartTime(int starttime){this.starttime = starttime;}
    public int getStartTime(){return starttime;}

    public void setLine(int line){this.line = line;}
    public int getLine(){return line;}

    public String toString(){
        return "[Step: "+getName()+"] Start: "+getStartTime()+", Dur: "+getTime()+", Line: "+getLine()+", Hot: "+getHot()+", Hands: "+getNeedsHand();
    }
}
