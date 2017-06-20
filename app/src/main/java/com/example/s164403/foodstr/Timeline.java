package com.example.s164403.foodstr;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.s164403.foodstr.database.DatabaseTask;
import com.example.s164403.foodstr.database.MainDatabaseHelper;
import com.example.s164403.foodstr.database.Model.Recipe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

/**
 * Created by Jonathan on 14-06-2017.
 */

public class Timeline {
    Recipe recipe;
    private ArrayList<RecipeStep> steps = new ArrayList<RecipeStep>();
    private HashMap<Integer, StepAlarm> alarms;
    private Context mContext;

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
    private static Comparator<RecipeStep> sortByFirstStart = new Comparator<RecipeStep>() {
        @Override
        public int compare(RecipeStep r1, RecipeStep r2) {
            int time1 = r1.getStartTime() + r1.getTime();
            int time2 = r2.getStartTime() + r2.getTime();
            return time1 < time2 ? 1 : (time1 > time2 ? -1 : 0);
        }
    };


    public Timeline(Recipe recipe, Context mContext) {
        this.recipe = recipe;
        this.mContext = mContext;
    }

    public Timeline(Recipe recipe){
        this.recipe = recipe;
    }
    public Timeline(){}

    public void addStep(RecipeStep step){
        steps.add(step);
    }

    public void removeStep(RecipeStep step){
        steps.remove(step);
    }

    private int numhands = 1;
    public int getAvailableHands(){
        return numhands;
    }
    public void setAvailableHands(int hands){
        numhands = hands;
        sort();
    }

    private void planStep(RecipeStep step, int start, int line){
        step.setStartTime(start);
        step.setLine(line);

        int end = start + step.getTime();
        if (end > finishtime) finishtime = end;

        Log.i("Test4", step.getName() + " Final Time: "+ ((finishtime-start)-step.getTime()) + " - " + (finishtime-start) + ", Internal Time: " + step.getStartTime() + " - " + step.getStartTime() + step.getTime());
    }

    public void initiateAlarms(){
        for (RecipeStep step : steps){
            int end = step.getStartTime();
            int start = end + step.getTime();

            Log.i("ALarmtid", start + ", " + end + ", Step: " +step.getName());

            if (alarms.get(start) == null){alarms.put(start, new StepAlarm(start, recipe));}
            alarms.get(start).addStartingStep(step);

            if (alarms.get(end) == null){alarms.put(end, new StepAlarm(end, recipe));}
            alarms.get(end).addEndingStep(step);
        }
    }

    public int getFinishTime(){return finishtime;}
    public ArrayList<RecipeStep> getSteps(){return steps;}

    /*public void sort(){
        //Reset our alarms - we will calculate them here
        alarms = new HashMap<Integer, StepAlarm>();
        finishtime = 0;

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
        //prioritysteps.sort(sortByLowestTime);

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

                planStep(step, mintime, minline);

                nexthand[minline] = nexthand[minline] + step.getTime();
            }
        }

        //Sort the rest with highest first for a nice step-like formation
        //normalsteps.sort(sortByHighestTime);

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

                    planStep(step, mintime, minline);

                    nexthand[minline] = nexthand[minline] + step.getTime();
                }else{
                    planStep(step, 0, coldline);
                    coldline++;
                }
            }
        }
    }*/

    public HashMap<Integer, StepAlarm> getAlarmTimes(){
        return alarms;
    }

    public void loadTimelineFromDatabase() {
        MainDatabaseHelper dbh = new MainDatabaseHelper(mContext);
        SQLiteDatabase db = dbh.getReadableDatabase();
        DatabaseTask dt = new DatabaseTask(db);
        if (recipe != null) {
            for (RecipeStep step : dt.getStepsForRecipe(recipe.id)) {
                addStep(step);
            }
        }
        db.close();
    }

    public static Timeline getTestTimeline(){
        Timeline tester = new Timeline();

        RecipeStep r1 = new RecipeStep("1");
        r1.setTime(20);
        r1.setHot(true);
        r1.setNeedsHand(true);
        tester.addStep(r1);

        RecipeStep r2 = new RecipeStep("2");
        r2.setTime(50);
        r2.setHot(true);
        r2.setNeedsHand(true);
        tester.addStep(r2);

        RecipeStep r3 = new RecipeStep("3");
        r3.setTime(10);
        r3.setHot(false);
        r3.setNeedsHand(true);
        tester.addStep(r3);

        RecipeStep r4 = new RecipeStep("4");
        r4.setTime(7);
        r4.setHot(false);
        r4.setNeedsHand(true);
        tester.addStep(r4);

        RecipeStep r5 = new RecipeStep("5");
        r5.setTime(30);
        r5.setHot(true);
        r5.setNeedsHand(false);
        tester.addStep(r5);

        RecipeStep r6 = new RecipeStep("6");
        r6.setTime(70);
        r6.setHot(false);
        r6.setNeedsHand(false);
        r6.addPrerequisite(r1);
        tester.addStep(r6);

        RecipeStep r7 = new RecipeStep("7");
        r7.setTime(20);
        r7.setHot(true);
        r7.setNeedsHand(false);
        tester.addStep(r7);

        tester.sort();

        return tester;
    }

    public static void main(String args[]){
        Timeline tester = getTestTimeline();

        for (RecipeStep step : tester.getSteps()){
            System.out.println(step.toString());
        }

        for (StepAlarm a : tester.getAlarmTimes().values()){
            System.out.println(a.toString());
        }
    }

    /*public void sort2(){

        ArrayList<ArrayList<RecipeStep>> steps2 = new ArrayList<ArrayList<RecipeStep>>();
        //TODO: tag alle predecessors først
        for(RecipeStep step : steps){
            int[] position = new int[2];
            int heatloss = Integer.MAX_VALUE;
            int linenumber = 0;
            int starttid = 0;
            ArrayList<Integer> indexes = new ArrayList<Integer>();

            for(ArrayList<RecipeStep> line : steps2){
                for (int i = 0; i<line.size();i++){
                    if(line.get(i).isPredecessor(step)){
                        for (int k = i; i >= 0; i--) {
                            if( starttid<line.get(i).getTime()) {
                                starttid=line.get(i).getTime();
                            }
                        }
                    }
                }
            }

            int time=0;
            for(ArrayList<RecipeStep> line : steps2) {
                int i;
                for (i=0; i<line.size();i++) {
                    time+=line.get(i).getTime();
                    if (time>=starttid) break;
                }
                indexes.add(i);
            }

            for(ArrayList<RecipeStep> line : steps2){
                int index;
                for(index = indexes.get(linenumber); index<line.size(); index++){
                    int warm=0;
                    int timebefore=0;
                    if(step.getHot()){
                        for(int i = index; i>=0;i--) {
                            timebefore += line.get(i).getTime();
                        }
                    }
                    warm=warm*step.getTime()+timebefore;
                    if(heatloss>warm) {
                        heatloss = warm;
                        position[0] = linenumber;
                        position[1] = index;
                    }
                }
                linenumber++;
            }

            steps2.get(position[0]).add(position[1], step);
        }

    }*/

    private ArrayList<ArrayList<RecipeStep>> sorted;
    private ArrayList<RecipeStep> handled;
    public void sort(){
        sorted = new ArrayList<ArrayList<RecipeStep>>();
        handled = new ArrayList<RecipeStep>();
        alarms = new HashMap<Integer, StepAlarm>();

        finishtime = 0;
        recursivePrerequisiteFilter(steps);
    }

    private void recursivePrerequisiteFilter(ArrayList<RecipeStep> steps){
        ArrayList<RecipeStep> prereqs = new ArrayList<RecipeStep>();
        for (RecipeStep step : steps){

            boolean skip = false;
            for (RecipeStep pre : step.getPredecessors()){
                if (!handled.contains(pre)){ prereqs.add(step); skip = true; break;}
            }
            if (skip) continue;

            int[] pos = getBestPositionForStep(step);
            handled.add(step);
            Log.i("Test0", step.getName() + ": ["+pos[0]+", "+pos[1]+", "+pos[2]+"], Sorted: " + sorted.size() + " To String: "+ step.toString());
            ArrayList<RecipeStep> line;
            if (pos[0] < sorted.size()){
                line = sorted.get(pos[0]);
            }else{
                line = new ArrayList<RecipeStep>();
                sorted.add(pos[0], line);
            }

            if (pos[1] < line.size()) {
                RecipeStep pushed = line.get(pos[1]);
                planStep(step, pos[2], pos[0]);
                Log.i("Test1", step.getName() + " Planned for: "+(pos[2]) + ", pushing " + pushed.getName());


                for (int i = pos[1]; i < line.size(); i++){
                    planStep(line.get(i),line.get(i).getStartTime() + step.getTime(), pos[0]);
                    Log.i("Test2", line.get(i).getName() + " Moved to: "+(pushed.getStartTime() + pushed.getTime() - step.getTime()));
                }
                line.add(pos[1], step);
            }else{
                planStep(step,Math.max((line.size() > 0 ? line.get(line.size()-1).getStartTime() + line.get(line.size()-1).getTime() : 0),pos[2]), pos[0]);
                Log.i("Test3", step.getName() + " Planned for: "+((line.size() > 0 ? line.get(line.size()-1).getStartTime() + line.get(line.size()-1).getTime() : 0)));
                line.add(line.size(), step);
            }
        }

        if (prereqs.size() > 0){
            recursivePrerequisiteFilter(prereqs);
        }else{
            initiateAlarms();
        }

        Collections.sort(steps, sortByFirstStart);
    }

    protected int[] getBestPositionForStep(RecipeStep step){
        if (sorted.size() <= 0){return new int[] {0,0,0};}

        int maxlines = step.getNeedsHand() ? getAvailableHands() : sorted.size() + 1;
        int start = 0;
        int end = Integer.MAX_VALUE;

        for (RecipeStep pre : step.getPrerequisites()){
            if (handled.contains(pre) && pre.getStartTime() < end){end = pre.getStartTime();}
        }
        for (RecipeStep pre : step.getPredecessors()){
            Log.i("Predecessor", pre.getName());
            if (handled.contains(pre) && pre.getStartTime() + pre.getTime() > start){start = pre.getStartTime() + pre.getTime();}
        }

        //Sort by least heat loss, least total finish time
        int heatloss = Integer.MAX_VALUE;
        int maxtime = Integer.MAX_VALUE;
        int finishtime = Integer.MAX_VALUE;
        int[] position = new int[3];

        Log.i("Lines", "Step: "+step.getName()+", Maxlines: "+maxlines + ", Start: " + start + ", End: "+end);

        for (int line = 0; line < maxlines; line++){
            if (line < sorted.size() && sorted.get(line) != null){
                ArrayList<RecipeStep> linesteps = sorted.get(line);
                Log.i("Lineswitch", "Step: "+step.getName()+ " now checking line: "+line);

                for (int si = 0; si <= linesteps.size(); si++){
                    int time;
                    if (si <= linesteps.size()){
                        if (si == linesteps.size()){
                            time = Math.max(start, linesteps.get(si-1).getStartTime() + linesteps.get(si-1).getTime());
                        }else {
                            time = linesteps.get(si).getStartTime();
                        }
                    }else{
                        time = linesteps.get(si-1).getStartTime()+linesteps.get(si-1).getTime();
                    }
                    if (time < start || time > end){ Log.i("Lineswitch", "Step: "+step.getName()+ " Out of bounds: "+time); continue;}

                    Log.i("Lineswitch", "Step: "+step.getName()+ " now checking index: "+si+", Time: " + time);
                    int numheat = 0;
                    for (int si2 = si; si2 < linesteps.size(); si2++){
                        if (linesteps.get(si2).getHot()){numheat++;}
                    }

                    int heat = step.getTime()*numheat;

                    if (si > 0 && linesteps.get(si-1) != null){
                        RecipeStep st = linesteps.get(si-1);
                        if (step.getHot()){heat += time;}
                    }
                    Log.i("Lineswitch", "Step: "+step.getName()+ " heatloss: "+heat);

                    if (heat < heatloss){
                        heatloss = heat;
                        position[0] = line;
                        position[1] = si;
                        position[2] = time;
                        maxtime = Math.max(linesteps.get(linesteps.size()-1).getStartTime()+linesteps.get(linesteps.size()-1).getTime(), time);
                        Log.i("Lineswitch", "Step: "+step.getName()+ " Heatloss lower! Line: "+line);

                    }else if (heat == heatloss){
                        Log.i("Lineswitch", "Step: "+step.getName()+ " Heatloss equal. Line: "+line);
                        ArrayList<RecipeStep> candidate = sorted.get(position[0]);
                        int length1 = Math.max(linesteps.get(linesteps.size()-1).getStartTime()+linesteps.get(linesteps.size()-1).getTime(), time);
                        int length2 = maxtime;

                        Log.i("Length", "Step: "+step.getName()+", Length1: "+length1+", Length2:"+length2+", Line: "+line+", Candidate: "+position[0]);

                        if (length1 < length2 ){
                            Log.i("Lineswitch", "Step: "+step.getName()+ " Length under! Line: "+line);
                            position[0] = line;
                            position[1] = si;
                            position[2] = time;
                            maxtime = length1;
                        }else if (length1 == length2){
                            Log.i("Lineswitch", "Step: "+step.getName()+ " Length equal. Line: "+line);
                            if (line <= position[0]){
                                Log.i("Lineswitch", "Step: "+step.getName()+ " Line under or equal. Line: "+line);
                                position[0] = line;
                                position[1] = si;
                                position[2] = time;
                            }
                        }
                    }
                }
            }else{
                Log.i("Lineswitch", "Step: "+step.getName()+ " now checking non-existant line: "+line);
                int heat = step.getHot() ? start : 0;
                Log.i("Lineswitch", "Step: "+step.getName()+ " heatloss: "+heat);

                if (heat < heatloss){
                    Log.i("Lineswitch", "Step: "+step.getName()+ " Heatloss lower! Line: "+line);
                    heatloss = heat;
                    position[0] = line;
                    position[1] = 0;
                    position[2] = start;
                }else if (heat == heatloss){
                    Log.i("Lineswitch", "Step: "+step.getName()+ " Heatloss equal. Line: "+line);
                    ArrayList<RecipeStep> candidate = sorted.get(position[0]);
                    int length1 = start;
                    int length2 = maxtime;
                    Log.i("Length", "Step: "+step.getName()+", Length1: "+length1+", Length2:"+length2+", Line: "+line+", Candidate: "+position[0]);

                    if (length1 < length2 ){
                        Log.i("Lineswitch", "Step: "+step.getName()+ " Length under! Line: "+line);
                        position[0] = line;
                        position[1] = 0;
                        position[2] = start;
                        maxtime = length1;
                    }else if (length1 == length2){
                        Log.i("Lineswitch", "Step: "+step.getName()+ " Length equal. Line: "+line);
                        if (line <= position[0]){
                            position[0] = line;
                            position[1] = 0;
                            position[2] = start;
                            Log.i("Lineswitch", "Step: "+step.getName()+ " Line under or equal. Line: "+line);
                        }
                    }
                }
                Log.i("Lineswitch", "Step: "+step.getName()+ " now breaking after checking line: "+line);
                break;
            }
        }
        return position;
    }

}
