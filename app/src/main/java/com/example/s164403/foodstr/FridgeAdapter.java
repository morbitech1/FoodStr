package com.example.s164403.foodstr;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.s164403.foodstr.database.LocalDatabaseFridge;
import com.example.s164403.foodstr.database.Model.Ingredient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Morbi95 on 15-Jun-17.
 */

public class FridgeAdapter extends BaseAdapter {
    final Map<Ingredient, Double> data;
    List<Ingredient> keys;
    final Activity activity;
    final SQLiteDatabase db;

    public FridgeAdapter(Activity activity, Map<Ingredient, Double> data, SQLiteDatabase db){
        this.activity = activity;
        this.data = data;
        keys = new ArrayList<>(data.keySet());
        this.db = db;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return keys.get(i);
    }

    @Override
    public long getItemId(int i) {
        return keys.get(i).id;
    }

    public void add(Ingredient ingredient, double amount){
        Double currentAmount = data.get(ingredient);
        if(currentAmount != null )
            amount += currentAmount >= 0 ? currentAmount : 0;
        data.put(ingredient,amount);
        keys = new ArrayList<>(data.keySet());
        notifyDataSetChanged();
    }

    public void remove(Ingredient ingredient){
        data.remove(ingredient);
        keys = new ArrayList<>(data.keySet());
        notifyDataSetChanged();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final Ingredient ingredient = keys.get(i);
        double amount = data.get(ingredient);
        final LinearLayout fragment = (LinearLayout) activity.getLayoutInflater().inflate(R.layout.fridge_fragment, null);
        TextView fridgeIngredient = (TextView) fragment.findViewById(R.id.fridge_ingredient);
        final EditText fridgeAmount = (EditText) fragment.findViewById(R.id.fridge_amount);
        TextView fridgeUnit = (TextView)  fragment.findViewById(R.id.fridge_unit);
        fridgeAmount.setId((int)ingredient.id);
        ImageView fridgeButton = (ImageView) fragment.findViewById(R.id.fridge_remove);
        fridgeIngredient.setText(ingredient.name);
        fridgeAmount.setText(amount >= 0 ? ""+amount : "");
        fridgeUnit.setText(ingredient.primaryUnit.toString());
        fridgeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new LocalDatabaseFridge(db).remove(ingredient.id);
                remove(ingredient);
            }
        });
        fridgeAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                double newAmount;
                try{
                    newAmount = Double.parseDouble(fridgeAmount.getText().toString());
                }catch (Exception e){
                    newAmount = -1;
                }
                new LocalDatabaseFridge(db).changeAmount(ingredient.id, newAmount);
            }
        });
        return fragment;
    }
}