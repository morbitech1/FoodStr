package com.example.s164403.foodstr;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.s164403.foodstr.database.DatabaseIngredient;
import com.example.s164403.foodstr.database.DummyData;
import com.example.s164403.foodstr.database.LocalDatabaseFridge;
import com.example.s164403.foodstr.database.Model.Ingredient;

import java.util.HashMap;
import java.util.List;

public class Fridge extends AppCompatActivity {

    Button add;
    AutoCompleteTextView ingredient;
    EditText amount;
    LinearLayout fridge;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fridge);
        initializeUiElements();
        final DatabaseIngredient databaseIngredient = new DatabaseIngredient(this);
        databaseIngredient.addIngredients(DummyData.dummyIngredients());

        final ArrayAdapter<Ingredient> adapter = new ArrayAdapter<Ingredient>(this,
                android.R.layout.simple_dropdown_item_1line, databaseIngredient.getAllIngredients());
        Log.d("Ingredient list", databaseIngredient.getAllIngredients().toString());
        ingredient.setAdapter(adapter);
        ingredient.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Ingredient ingredient = adapter.getItem(i);
                amount.setHint(ingredient.primaryUnit.toString());
                Log.d("Ingredient", "id: " + ingredient.id);
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Ingredient i = databaseIngredient.getIngredient(ingredient.getText().toString());
                if(i != null){
                    LocalDatabaseFridge fridgeDB = new LocalDatabaseFridge(getApplicationContext());
                    double a;
                    try{
                        a = Double.parseDouble(amount.getText().toString());
                    }catch(Exception e){
                        a = -1;
                    }
                    fridgeDB.addIngredient(i.id, a);
                }else{
                    Toast.makeText(getApplicationContext(), ingredient.getText().toString() + " does not exists", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void updateFridge(){
        LocalDatabaseFridge fridgeDB = new LocalDatabaseFridge(this);
        HashMap<Ingredient, Integer> ingredients = fridgeDB.getIngredientsInFridge();
        fridge.removeAllViews();
        for(final Ingredient ingredient : ingredients.keySet()){
            fridge.addView(generateFridgeFragment(ingredient, ingredients.get(ingredient), fridgeDB));
        }
    }

    private LinearLayout generateFridgeFragment(final Ingredient ingredient, int ammount, final LocalDatabaseFridge fridgeDB){
        final LinearLayout fragment = (LinearLayout) getLayoutInflater().inflate(R.layout.fridge_fragment, null);
        TextView fridgeIngredient = (TextView) fragment.findViewById(R.id.fridge_ingredient);
        EditText fridgeAmount = (EditText) fragment.findViewById(R.id.fridge_amount);
        ImageButton fridgeButton = (ImageButton) fragment.findViewById(R.id.fridge_remove);
        fridgeIngredient.setText(ingredient.name);
        fridgeAmount.setText(ammount);
        fridgeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fridge.removeView(fragment);
                fridgeDB.remove(ingredient.id);
            }
        });
        return fragment;
    }

    private void initializeUiElements(){
        add = (Button) findViewById(R.id.add);
        ingredient = (AutoCompleteTextView) findViewById(R.id.ingredient);
        amount = (EditText) findViewById(R.id.amount);
        fridge = (LinearLayout) getLayoutInflater().inflate(R.layout.fridge, null);
    }
}
