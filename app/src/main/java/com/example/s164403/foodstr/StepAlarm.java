package com.example.s164403.foodstr;

import java.util.ArrayList;

/**
 * Created by Jonathan on 14-06-2017.
 */

public class StepAlarm {
    private ArrayList<RecipeStep> starting = new ArrayList<RecipeStep>();
    private ArrayList<RecipeStep> ending = new ArrayList<RecipeStep>();

    private int time;

    public StepAlarm(int time){this.time = time;};

    public void addStartingStep(RecipeStep step){if (!starting.contains(step)) starting.add(step);};
    public void addEndingStep(RecipeStep step){if (!ending.contains(step)) ending.add(step);};

    public void removeStartingStep(RecipeStep step){starting.remove(step);};
    public void removeEndingStep(RecipeStep step){ending.remove(step);};

    public ArrayList<RecipeStep> getStartingSteps(){return starting;};
    public ArrayList<RecipeStep> getEndingSteps(){return ending;};

    public int getTime(){return time;};

    public String toString(){
        String str = "FOODSTR ALARMS ["+getTime()+"]:\n";
        if (starting.size() > 0){
            str = str + "Starting:\n";
            for (RecipeStep step : starting){
                str = str + "\t- " + step.getName()+"\n";
            }
        }
        if (ending.size() > 0){
            str = str + "Ending:\n";
            for (RecipeStep step : ending){
                str = str + "\t- " + step.getName()+"\n";
            }
        }
        return str;
    }
}
