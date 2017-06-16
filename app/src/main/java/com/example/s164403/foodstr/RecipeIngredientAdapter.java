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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
        keys = new ArrayList<>(recipeIngredientRelation.ingredients.keySet());
        this.people = people;
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view == null)
            view = activity.getLayoutInflater().inflate(R.layout.recipe_ingredient_fragment, null);
        LocalDatabaseFridge fridgeDb = new LocalDatabaseFridge(db);
        Ingredient ingredient = keys.get(position);

        Double factor =  recipeIngredientRelation.ingredients.get(ingredient) * people;
        Double inFridge = fridgeDb.getAmount(ingredient.id);
        Double percentage = inFridge >= 0 ? (inFridge/factor)*100 : 0;

        TextView ingredientName = (TextView) view.findViewById(R.id.recipe_ingredient_name);
        ingredientName.setText(ingredient.name);

        TextView ingredientFridgeAmount = (TextView) view.findViewById(R.id.recipe_fridge_amount);
        ingredientFridgeAmount.setText(inFridge.toString() + ingredient.primaryUnit.toString());

        TextView ingredientRequiredAmount = (TextView) view.findViewById(R.id.recipe_required_amount);
        ingredientRequiredAmount.setText(factor.toString() + ingredient.primaryUnit.toString());

        ProgressBar recipeIngredientScore = (ProgressBar) view.findViewById(R.id.recipe_ingredient_score);
        recipeIngredientScore.setProgress(percentage.intValue());
        return view;
    }
}
