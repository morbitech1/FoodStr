package com.example.s164403.foodstr;

import android.app.Fragment;
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
    final Fridge fridgeFragment = new Fridge();
    final SearchResult searchResultFragment = new SearchResult();
    Fragment currentFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainView = (FrameLayout) findViewById(R.id.main_view);
        menuBar = (LinearLayout) findViewById(R.id.menu_bar);
        search = (Button) findViewById(R.id.search);
        if(savedInstanceState == null) {
            getFragmentManager().beginTransaction().replace(R.id.main_view, fridgeFragment).commit();
            currentFragment = fridgeFragment;
            search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getFragmentManager().beginTransaction()
                            .replace(R.id.main_view, searchResultFragment)
                            .addToBackStack(null)
                            .commit();
                    currentFragment = searchResultFragment;
                }
            });
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }
}
