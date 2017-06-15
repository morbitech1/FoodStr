package com.example.s164403.foodstr;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
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

import com.example.s164403.foodstr.database.*;
import com.example.s164403.foodstr.database.Model.*;

import java.util.HashMap;

import com.example.s164403.foodstr.database.DatabaseRecipeIngredient;

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

        final MainDatabaseHelper databaseHelper = new MainDatabaseHelper(this);
        final SQLiteDatabase db = databaseHelper.getWritableDatabase();

        DatabaseRecipeIngredient recipeIngredientDb = new DatabaseRecipeIngredient();
        for(RecipeIngredientRelation ri : DummyData.dummyRI()){
            recipeIngredientDb.addRelation(db, ri);
        }

        final DatabaseIngredient databaseIngredient = new DatabaseIngredient();
        final ArrayAdapter<Ingredient> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, databaseIngredient.getAllIngredients(db));
        Log.d("Ingredient list", databaseIngredient.getAllIngredients(db).toString());
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
                Ingredient i = databaseIngredient.getIngredient(db, ingredient.getText().toString());
                if(i != null){
                    LocalDatabaseFridge fridgeDB = new LocalDatabaseFridge();
                    double a;
                    try{
                        a = Double.parseDouble(amount.getText().toString());
                    }catch(Exception e){
                        a = -1;
                    }
                    fridgeDB.addIngredient(db, i.id, a);
                    fridge.addView(generateFridgeFragment(i, a, db));
                    ingredient.setText("");
                    amount.setText("");
                }else{
                    Toast.makeText(getApplicationContext(), ingredient.getText().toString() + " does not exists", Toast.LENGTH_LONG).show();
                }
            }
        });
        updateFridge();
    }

    public void updateFridge(){
        SQLiteDatabase db = new MainDatabaseHelper(this).getWritableDatabase();
        LocalDatabaseFridge fridgeDB = new LocalDatabaseFridge();
        HashMap<Ingredient, Integer> ingredients = fridgeDB.getIngredientsInFridge(db);
        fridge.removeAllViews();
        for(final Ingredient ingredient : ingredients.keySet()){
            fridge.addView(generateFridgeFragment(ingredient, ingredients.get(ingredient), db));
            Log.d("Fridge", ""+fridge.getChildCount());
        }
    }

    private LinearLayout generateFridgeFragment(final Ingredient ingredient, double amount, final SQLiteDatabase db){
        final LinearLayout fragment = (LinearLayout) getLayoutInflater().inflate(R.layout.fridge_fragment, null);
        TextView fridgeIngredient = (TextView) fragment.findViewById(R.id.fridge_ingredient);
        EditText fridgeAmount = (EditText) fragment.findViewById(R.id.fridge_amount);
        ImageButton fridgeButton = (ImageButton) fragment.findViewById(R.id.fridge_remove);
        fridgeIngredient.setText(ingredient.name);
        Log.d("Test", ""+amount);
        fridgeAmount.setText(amount >= 0 ? ""+amount : "");
        fridgeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fridge.removeView(fragment);
                new LocalDatabaseFridge().remove(db, ingredient.id);
            }
        });
        return fragment;
    }

    private void initializeUiElements(){
        add = (Button) findViewById(R.id.add);
        ingredient = (AutoCompleteTextView) findViewById(R.id.ingredient);
        amount = (EditText) findViewById(R.id.amount);
        fridge = (LinearLayout) findViewById(R.id.fridgeBox);
    }
}
