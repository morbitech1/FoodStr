package com.example.s164403.foodstr.database;

import com.example.s164403.foodstr.database.Model.Ingredient;
import com.example.s164403.foodstr.database.Model.PrimaryUnit;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Morbi95 on 14-Jun-17.
 */

public class DummyData {
    public static List<Ingredient> dummyIngredients(){
        ArrayList<Ingredient> res = new ArrayList<>();
        res.add(new Ingredient("Onion", PrimaryUnit.amt));
        res.add(new Ingredient("Garlic Bulb", PrimaryUnit.amt));
        res.add(new Ingredient("Chicken Leg", PrimaryUnit.amt));
        res.add(new Ingredient("Olive Oil", PrimaryUnit.tbsp));
        res.add(new Ingredient("Parsnip", PrimaryUnit.amt));
        res.add(new Ingredient("Carrot", PrimaryUnit.amt));
        res.add(new Ingredient("Sweet Potato", PrimaryUnit.amt));
        res.add(new Ingredient("Chicken Stock", PrimaryUnit.ml));
        res.add(new Ingredient("Pasta", PrimaryUnit.gram));
        res.add(new Ingredient("parmasan Cheese", PrimaryUnit.gram));
        res.add(new Ingredient("spaghetti", PrimaryUnit.gram));
        res.add(new Ingredient("Sugar", PrimaryUnit.tsp));
        return res;
    }
}
