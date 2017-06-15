package com.example.s164403.foodstr;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.s164403.foodstr.database.Model.Recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Morbi95 on 15-Jun-17.
 */

public class SearchResultAdapter extends BaseAdapter{
    final Map<Recipe, Double> data;
    final List<Recipe> keys;
    final Activity activity;

    public SearchResultAdapter(Activity activity, Map<Recipe, Double> data){
        this.activity = activity;
        this.data = data;
        keys = new ArrayList<>(data.keySet());
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LinearLayout linearLayout = (LinearLayout) activity.getLayoutInflater().inflate(R.layout.recipe_fragment, null);
        Recipe recipe = keys.get(i);
        double score = data.get(recipe);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Move to show recipe view
            }
        });
        TextView recipeName = (TextView) linearLayout.findViewById(R.id.recipe_name);
        recipeName.setText(recipe.name);
        ProgressBar recipeScore = (ProgressBar) linearLayout.findViewById(R.id.recipe_score);
        recipeScore.setProgress((int) score);
        return linearLayout;
    }
}
