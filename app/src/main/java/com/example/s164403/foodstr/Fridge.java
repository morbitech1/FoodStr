package com.example.s164403.foodstr;

import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Fridge extends AppCompatActivity {

    
    Button add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fridge);
        add = (Button) findViewById(R.id.add);
        add.setOnClickListener(addIngredientListener());
    }

    public View.OnClickListener addIngredientListener(){
        return new View.OnClickListener(){
            public void onClick(View view){

            }
        };
    }


}
