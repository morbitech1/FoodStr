package com.example.s164403.foodstr;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * Created by Morbi95 on 15-Jun-17.
 */

public class MainActivity extends AppCompatActivity{
    FrameLayout mainView;
    LinearLayout menuBar;
    Button search;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainView = (FrameLayout) findViewById(R.id.main_view);
        menuBar = (LinearLayout) findViewById(R.id.menu_bar);
        search = (Button) findViewById(R.id.search);
        final Fridge fridgeFragment = new Fridge();
        getFragmentManager().beginTransaction().add(R.id.main_view, fridgeFragment).commit();
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchResult searchResult = new SearchResult();
                getFragmentManager().beginTransaction()
                        .remove(fridgeFragment)
                        .add(R.id.main_view, searchResult)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }
}
