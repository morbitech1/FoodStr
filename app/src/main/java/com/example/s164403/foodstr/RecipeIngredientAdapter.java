package com.example.s164403.foodstr;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.s164403.foodstr.database.DatabaseRecipeIngredient;
import com.example.s164403.foodstr.database.LocalDatabaseFridge;
import com.example.s164403.foodstr.database.Model.Ingredient;
import com.example.s164403.foodstr.database.Model.Recipe;
import com.example.s164403.foodstr.database.Model.RecipeIngredientRelation;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by s164403 on 6/16/2017.
 */

public class RecipeIngredientAdapter extends BaseAdapter{

    Activity activity;
    RecipeIngredientRelation recipeIngredientRelation;
    List<Ingredient> keys;
    SQLiteDatabase db;
    int people;

    public RecipeIngredientAdapter(Activity activity, Recipe recipe, SQLiteDatabase db, int people){
        this.activity = activity;
        this.db = db;
        recipeIngredientRelation = new DatabaseRecipeIngredient(db).getRecipeIngredientRelation(recipe.id);
        this.people = people;
        LocalDatabaseFridge fridgeDb = new LocalDatabaseFridge(db);
        Map<Ingredient,Double> ingredientPercentageMap = new LinkedHashMap<>();
        for(Ingredient ingredient : recipeIngredientRelation.ingredients.keySet()){
            Double factor =  recipeIngredientRelation.ingredients.get(ingredient) * people;
            Double inFridge = fridgeDb.getAmount(ingredient.id);
            inFridge = inFridge >= 0 ? inFridge : Double.POSITIVE_INFINITY ;
            Double percentage = inFridge >= 0 ? (inFridge/factor)*100 : 0;
            ingredientPercentageMap.put(ingredient, percentage);
        }
        keys = new ArrayList<>(sortByValue(ingredientPercentageMap).keySet());


    }

    //Descending order
    public static <K, V extends Comparable<? super V>> Map<K, V>
    sortByValue( Map<K, V> map )
    {
        List<Map.Entry<K, V>> list =
                new LinkedList<Map.Entry<K, V>>( map.entrySet() );
        Collections.sort( list, new Comparator<Map.Entry<K, V>>()
        {
            public int compare( Map.Entry<K, V> o1, Map.Entry<K, V> o2 )
            {
                return (o2.getValue()).compareTo( o1.getValue() );
            }
        } );

        Map<K, V> result = new LinkedHashMap<K, V>();
        for (Map.Entry<K, V> entry : list)
        {
            result.put( entry.getKey(), entry.getValue() );
        }
        return result;
    }

    public void changePeopleCount(int count){
        this.people = count;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return recipeIngredientRelation.ingredients.size();
    }

    @Override
    public Object getItem(int position) {
        return keys.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void changeNumberOfPeople(int peopleCount){
        this.people = peopleCount;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view == null)
            view = activity.getLayoutInflater().inflate(R.layout.recipe_overview_list_element, null);
        LocalDatabaseFridge fridgeDb = new LocalDatabaseFridge(db);
        Ingredient ingredient = keys.get(position);
        Double factor =  recipeIngredientRelation.ingredients.get(ingredient) * people;
        Double inFridge = fridgeDb.getAmount(ingredient.id);
        inFridge = inFridge >= 0 ? inFridge : Double.POSITIVE_INFINITY ;
        Double percentage = inFridge >= 0 ? (inFridge/factor)*100 : 0;

        TextView ingredientName = (TextView) view.findViewById(R.id.recipe_ingredient_name);
        ingredientName.setText(ingredient.name);
        DecimalFormat df = new DecimalFormat("0.##");

        TextView ingredientFridgeAmount = (TextView) view.findViewById(R.id.recipe_fridge_amount);
        ingredientFridgeAmount.setText(inFridge.isInfinite() ? "∞" : df.format(inFridge) + ingredient.primaryUnit.toString());

        TextView ingredientRequiredAmount = (TextView) view.findViewById(R.id.recipe_required_amount);
        ingredientRequiredAmount.setText(df.format(factor) + ingredient.primaryUnit.toString());

        ProgressBar recipeIngredientScore = (ProgressBar) view.findViewById(R.id.recipe_ingredient_score);
        recipeIngredientScore.setProgress(percentage.intValue());

        TextView recipePersonCount = (TextView) view.findViewById(R.id.recipe_person_count);
        recipePersonCount.setText(""+people);
        return view;
    }
}
