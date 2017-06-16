package com.example.s164403.foodstr;

import android.app.Fragment;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import com.example.s164403.foodstr.database.LocalDatabaseFridge;
import com.example.s164403.foodstr.database.MainDatabaseHelper;
import com.example.s164403.foodstr.database.Model.Recipe;

import java.util.Map;

/**
 * Created by Morbi95 on 15-Jun-17.
 */

public class SearchResult extends Fragment {

    EditText filter, numOfPeople;
    ListView recipes;
    SQLiteDatabase db;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.search_result, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        filter = (EditText) getActivity().findViewById(R.id.filter);
        numOfPeople = (EditText) getActivity().findViewById(R.id.person_count);
        recipes = (ListView) getActivity().findViewById(R.id.recipes);
        db = new MainDatabaseHelper(getActivity()).getWritableDatabase();
        LocalDatabaseFridge fridgeDB = new LocalDatabaseFridge(db);
        Map<Recipe, Double> searchResult = fridgeDB.searchRecipesByScore(
                Integer.parseInt(numOfPeople.getText().toString()), 10);
        recipes.setAdapter(new SearchResultAdapter(getActivity(), searchResult));
    }

}
