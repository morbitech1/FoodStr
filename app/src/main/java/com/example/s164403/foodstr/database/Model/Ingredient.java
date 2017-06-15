package com.example.s164403.foodstr.database.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by aMoe on 14-06-2017.
 */

public class Ingredient {
    public final int id;
    public final String name;
    public final String[] aliases;
    public final PrimaryUnit primaryUnit;

    public Ingredient(int id, String name, String[] aliases, PrimaryUnit primaryUnit){
        this.id = id;
        this.name = name;
        this.aliases = aliases;
        this.primaryUnit = primaryUnit;
    }

    public Ingredient(String name, String[] aliases, PrimaryUnit primaryUnit){
        this(-1, name, aliases, primaryUnit);
    }

    public Ingredient(String name, PrimaryUnit primaryUnit){
        this(name, new String[0], primaryUnit);
    }

    public String getAliasCsv(){
        String res = "";
        for(String alias : aliases){
            if(!res.isEmpty())
                res += ";";
            res += alias;
        }
        return res;
    }

    public static String[] parseCsvAlias(String alias){
        List<String> res = new ArrayList<>();
        StringTokenizer stringTokenizer = new StringTokenizer(alias, ";");
        while(stringTokenizer.hasMoreTokens()){
            res.add(stringTokenizer.nextToken());
        }
        return res.toArray(new String[res.size()]);
    }

    public String toString(){
        return name;
    }

    public List<Integer> relationIds;

    public List<RecipeIngredientRelation> getRecipeRelations() {
        throw new Error();
    }
}
