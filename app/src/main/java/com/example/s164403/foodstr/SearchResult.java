package com.example.s164403.foodstr;

import android.app.Fragment;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.s164403.foodstr.database.LocalDatabaseFridge;
import com.example.s164403.foodstr.database.MainDatabaseHelper;
import com.example.s164403.foodstr.database.Model.Recipe;
import com.example.s164403.foodstr.database.RecipeOverview;

import java.util.Map;

/**
 * Created by Morbi95 on 15-Jun-17.
 */

public class SearchResult extends Fragment implements OnSearchCompleted {
    EditText filter, numOfPeople;
    ListView recipes;
    SQLiteDatabase db;
    public final static String TAG = "Search-Result-Fragment";
    private SearchInDatabaseTask currentSearchInDatabaseTask;
    private static final int DEFAULT_MAX_RESULTS = 10;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.search_result, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        ImageView fridge = (ImageView) getActivity().findViewById(R.id.button_fridge);
        fridge.setVisibility(View.VISIBLE);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        filter = (EditText) getActivity().findViewById(R.id.filter);


        TextWatcher searchOnChangeWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                startSearchTask();
            }

            @Override
            public void afterTextChanged(Editable s) { }
        };

        filter.addTextChangedListener(searchOnChangeWatcher);

        numOfPeople = (EditText) getActivity().findViewById(R.id.person_count);
        numOfPeople.addTextChangedListener(searchOnChangeWatcher);
        recipes = (ListView) getActivity().findViewById(R.id.recipes);
        db = new MainDatabaseHelper(getActivity()).getWritableDatabase();
        startSearchTask();
        recipes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle  = new Bundle();
                //close keyboard
                View v = getActivity().getCurrentFocus();
                if (v != null) {
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                Recipe recipe = (Recipe) parent.getAdapter().getItem(position);
                Log.d(TAG, "Showing view for " + recipe.name );
                bundle.putLong("recipeId", recipe.id);
                int numberOfPeople = Integer.parseInt(getResources().getString(R.string.search_default_number_people));
                try{
                    numberOfPeople = Integer.parseInt(numOfPeople.getText().toString());
                }catch(Exception e){}

                bundle.putInt("peopleCount", numberOfPeople);
                Log.d(TAG, "Making recipe fragment");
                RecipeOverview recipeOverview = new RecipeOverview();
                recipeOverview.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.main_view,recipeOverview ).addToBackStack(null).commit();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (db != null) {
            db.close();
        }
    }

    @Override
    public void onSearchCompleted(Map<Recipe, Double> searchResult) {
        Log.d(TAG, "Results from search: " + searchResult.toString());
        recipes.setAdapter(new SearchResultAdapter(getActivity(), searchResult));
    }

    private void startSearchTask() {
        if (currentSearchInDatabaseTask != null) {
            if (currentSearchInDatabaseTask.cancel(true)) {
                return;
            }
        }
        currentSearchInDatabaseTask = new SearchInDatabaseTask(this);

        SearchQuery query = new SearchQuery();
        query.databaseFridge = new LocalDatabaseFridge(db);
        try {
            query.numOfPeople = Integer.parseInt(numOfPeople.getText().toString());
        } catch (NumberFormatException ex) {
            query.numOfPeople = Integer.parseInt(getResources().getString(R.string.search_default_number_people));
        }
        query.filter = filter.getText().toString();
        query.limit = DEFAULT_MAX_RESULTS;

        currentSearchInDatabaseTask.execute(query);
    }

    private class SearchQuery {
        String filter;
        int numOfPeople;
        int limit;
        LocalDatabaseFridge databaseFridge;
    }


    private class SearchInDatabaseTask extends AsyncTask<SearchQuery, Void, Map<Recipe, Double>> {
        private OnSearchCompleted mCallback;

        public SearchInDatabaseTask(OnSearchCompleted callback) {
            mCallback = callback;
        }

        @Override
        protected void onPostExecute(Map<Recipe, Double> recipeDoubleMap) {
            mCallback.onSearchCompleted(recipeDoubleMap);
        }

        @Override
        protected Map<Recipe, Double> doInBackground(SearchQuery... params) {
            Map<Recipe, Double> searchResult = null;
            if (params.length >= 1) {
                SearchQuery query = params[0];
                searchResult = query.databaseFridge.searchRecipesByScore(
                        query.numOfPeople, query.limit, query.filter);
            }
            return searchResult;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        ImageView fridge = (ImageView) getActivity().findViewById(R.id.button_fridge);
        fridge.setVisibility(View.GONE);
    }
}


interface OnSearchCompleted{
    void onSearchCompleted(Map<Recipe, Double> searchRes);
}
