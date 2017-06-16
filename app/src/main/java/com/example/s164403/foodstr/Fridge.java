package com.example.s164403.foodstr;

import android.app.Fragment;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.s164403.foodstr.database.DatabaseIngredient;
import com.example.s164403.foodstr.database.DatabaseRecipeIngredient;
import com.example.s164403.foodstr.database.DummyData;
import com.example.s164403.foodstr.database.LocalDatabaseFridge;
import com.example.s164403.foodstr.database.MainDatabaseHelper;
import com.example.s164403.foodstr.database.Model.Ingredient;
import com.example.s164403.foodstr.database.Model.RecipeIngredientRelation;

public class Fridge extends Fragment {

    public final static String TAG = "Fridge-Fragment";
    public static MainDatabaseHelper databaseHelper;
    public static SQLiteDatabase db;

    Button add;
    AutoCompleteTextView ingredientTextview;
    EditText amount;
    ListView fridge;
    FridgeAdapter fridgeAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fridge, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initializeDatabase();
        initializeUiElements();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (db != null) {
            db.close();
        }
    }

    private void initializeDatabase() {
        databaseHelper = new MainDatabaseHelper(getActivity());
        db = databaseHelper.getWritableDatabase();
        addDummyData();
    }

    private void initializeUiElements(){
        //load layout and views
        add = (Button) getView().findViewById(R.id.add);
        ingredientTextview = (AutoCompleteTextView) getView().findViewById(R.id.ingredient);
        amount = (EditText) getView().findViewById(R.id.amount);
        fridge = (ListView) getView().findViewById(R.id.fridgeBox);

        //initialize adapter for AutoCompleteTextView
        final DatabaseIngredient databaseIngredient = new DatabaseIngredient(db);
        final ArrayAdapter<Ingredient> adapter = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_dropdown_item_1line,
                databaseIngredient.getAllIngredients());
        Log.d(TAG, "Found " +  adapter.getCount() + " ingredients");
        ingredientTextview.setAdapter(adapter);

        //Set selection lisner on AutoCompleteTextview
        ingredientTextview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Ingredient ingredient = adapter.getItem(i);
                amount.setHint(ingredient.primaryUnit.toString());
                Log.d(TAG, "id: " + ingredient.id);
            }
        });

        //Make adapter for ListView
        fridgeAdapter = new FridgeAdapter(getActivity(),
                new LocalDatabaseFridge(db).getIngredientsInFridge(), db);
        fridge.setAdapter(fridgeAdapter);


        //event handler for submit button
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Ingredient ingredient = databaseIngredient.getIngredient(ingredientTextview.getText().toString());
                //checks if the ingredient is in the database
                if(ingredient != null){
                    LocalDatabaseFridge fridgeDB = new LocalDatabaseFridge(db);
                    double amount;
                    //Try to get amount from text field, defaults to -1 for no amount given
                    try{
                        amount = Double.parseDouble(Fridge.this.amount.getText().toString());
                    }catch(Exception e){
                        amount = -1;
                    }
                    //tries to add the ingredient to the fridge
                    fridgeDB.addIngredient(ingredient.id, amount);
                    fridgeAdapter.add(ingredient,amount);
                    //Reset add ingredient text
                    ingredientTextview.setText("");
                    Fridge.this.amount.setText("");
                }else{
                    //Let user know the ingredient does not exist
                    Toast.makeText(getActivity(), ingredientTextview.getText().toString() + " does not exists", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void addDummyData(){
        DatabaseRecipeIngredient recipeIngredientDb = new DatabaseRecipeIngredient(db);
        for(RecipeIngredientRelation ri : DummyData.dummyRI()){
            recipeIngredientDb.addRelation(ri);
        }
    }
}
