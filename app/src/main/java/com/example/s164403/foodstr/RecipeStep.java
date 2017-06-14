package com.example.s164403.foodstr;

import java.util.ArrayList;

/**
 * Created by Jonathan on 14-06-2017.
 */

public class RecipeStep {
    private boolean hot;
    private int time = 10;
    private boolean hand;
    private ArrayList<RecipeStep> prerequisites;
    private String name = "";

    private int starttime;
    private int line;

    public RecipeStep(String name){
        setName(name);
    }

    public void setName(String name) {this.name = name;}
    public String getName() {return name;}

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
