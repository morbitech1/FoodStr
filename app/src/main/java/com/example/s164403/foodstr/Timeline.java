package com.example.s164403.foodstr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

/**
 * Created by Jonathan on 14-06-2017.
 */

public class Timeline {
    //Recipe recipe;
    private ArrayList<RecipeStep> steps = new ArrayList<RecipeStep>();
    private HashMap<Integer, String> alarms;

    private int finishtime;

    private static Comparator<RecipeStep> sortByLowestTime = new Comparator<RecipeStep>() {
        @Override
        public int compare(RecipeStep r1, RecipeStep r2) {
            return r1.getTime() > r2.getTime() ? 1 : (r1.getTime() < r2.getTime() ? -1 : 0);
        }
    };
    private static Comparator<RecipeStep> sortByHighestTime = new Comparator<RecipeStep>() {
        @Override
        public int compare(RecipeStep r1, RecipeStep r2) {
            return r1.getTime() < r2.getTime() ? 1 : (r1.getTime() > r2.getTime() ? -1 : 0);
        }
    };


    //public Timeline(Recipe recipe){
        //this.recipe = recipe;
    //}
    public Timeline(){}

    public void addStep(RecipeStep step){
        steps.add(step);
    }

    public void removeStep(RecipeStep step){
        steps.remove(step);
    }

    public int getAvailableHands(){
        return 3;
    }

    public void sort(){
        //Reset our alarms - we will calculate them here

        //Sort the important steps (the warm steps that require hands)

        ArrayList<RecipeStep> prioritysteps = new ArrayList<RecipeStep>();
        ArrayList<RecipeStep> normalsteps = new ArrayList<RecipeStep>();

        for (RecipeStep step : steps){
            if (step.getHot() && step.getNeedsHand()){
                prioritysteps.add(step);
            }else{
                normalsteps.add(step);
            }
        }
        //Sort the important chained steps so that they start with the shortest
        prioritysteps.sort(sortByLowestTime);

        //Set some sorting variables
        int nexthand[] = new int[getAvailableHands()];
        int coldline = getAvailableHands();

        //All steps that are warm and needs hands go first
        for (RecipeStep step : prioritysteps){
            if (step != null) {
                int mintime = Integer.MAX_VALUE;
                int minline = Integer.MAX_VALUE;
                for (int i = 0; i < nexthand.length; i++) {
                    if (nexthand[i] < mintime) {
                        mintime = nexthand[i];
                        minline = i;
                    }
                }

                step.setStartTime(mintime);
                step.setLine(minline);

                nexthand[minline] = nexthand[minline] + step.getTime();
            }
        }

        //Sort the rest with highest first for a nice step-like formation
        normalsteps.sort(sortByHighestTime);

        //The sorting of the rest doesn't matter;
        for (RecipeStep step : normalsteps){
            if (step != null) {
                if (step.getNeedsHand()) {
                    //If it requires hands, do the same logic as before
                    //find the earliest space in the available hands' lines
                    int mintime = Integer.MAX_VALUE;
                    int minline = Integer.MAX_VALUE;
                    for (int i = 0; i < nexthand.length; i++) {
                        if (nexthand[i] < mintime) {
                            mintime = nexthand[i];
                            minline = i;
                        }
                    }

                    step.setStartTime(mintime);
                    step.setLine(minline);

                    nexthand[minline] = nexthand[minline] + step.getTime();
                }else{
                    step.setStartTime(0);
                    step.setLine(coldline);
                    coldline++;
                }
            }
        }
    }

    public HashMap<Integer, String> getAlarmTimes(){
        return alarms;
    }

    public static void main(String args[]){
        Timeline tester = new Timeline();

        RecipeStep r1 = new RecipeStep("1");
        r1.setTime(2);
        r1.setHot(true);
        r1.setNeedsHand(true);
        tester.addStep(r1);

        RecipeStep r2 = new RecipeStep("2");
        r2.setTime(5);
        r2.setHot(true);
        r2.setNeedsHand(true);
        tester.addStep(r2);

        RecipeStep r3 = new RecipeStep("3");
        r3.setTime(1);
        r3.setHot(false);
        r3.setNeedsHand(true);
        tester.addStep(r3);

        RecipeStep r4 = new RecipeStep("4");
        r4.setTime(7);
        r4.setHot(false);
        r4.setNeedsHand(true);
        tester.addStep(r4);

        RecipeStep r5 = new RecipeStep("5");
        r5.setTime(3);
        r5.setHot(true);
        r5.setNeedsHand(false);
        tester.addStep(r5);

        RecipeStep r6 = new RecipeStep("6");
        r6.setTime(7);
        r6.setHot(false);
        r6.setNeedsHand(false);
        tester.addStep(r6);

        RecipeStep r7 = new RecipeStep("7");
        r7.setTime(2);
        r7.setHot(true);
        r7.setNeedsHand(false);
        tester.addStep(r7);

        tester.sort();

        System.out.println(r1.toString());
        System.out.println(r2.toString());
        System.out.println(r3.toString());
        System.out.println(r4.toString());
        System.out.println(r5.toString());
        System.out.println(r6.toString());
        System.out.println(r7.toString());


    }

}
