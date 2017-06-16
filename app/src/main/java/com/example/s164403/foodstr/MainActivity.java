package com.example.s164403.foodstr;

import android.app.Fragment;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by Morbi95 on 15-Jun-17.
 */

public class MainActivity extends AppCompatActivity{
    public final String TAG = "Main-Activity";
    FrameLayout mainView;
    LinearLayout menuBar;
    ImageView search;
    Fridge fridgeFragment = new Fridge();
    SearchResult searchResultFragment = new SearchResult();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainView = (FrameLayout) findViewById(R.id.main_view);
        menuBar = (LinearLayout) findViewById(R.id.menu_bar);
        search = (ImageView) findViewById(R.id.button_search);
        if(savedInstanceState == null) {
            getFragmentManager().beginTransaction().replace(R.id.main_view, fridgeFragment).commit();
        }
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Changing to search result fragment");
                getFragmentManager().beginTransaction()
                        .replace(R.id.main_view, searchResultFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });



    }


}
