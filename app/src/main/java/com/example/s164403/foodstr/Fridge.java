package com.example.s164403.foodstr;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

public class Fridge extends AppCompatActivity {

    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, new String[1]);
    Button add;
    AutoCompleteTextView ingredient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fridge);
        add = (Button) findViewById(R.id.add);
        add.setOnClickListener(addIngredientListener());
        ingredient = (AutoCompleteTextView) findViewById(R.id.ingredient);


    }

    public View.OnClickListener addIngredientListener(){
        return new View.OnClickListener(){
            public void onClick(View view){

            }
        };
    }




}
