package com.example.s164403.foodstr;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.s164403.foodstr.database.DatabaseRecipe;
import com.example.s164403.foodstr.database.MainDatabaseHelper;
import com.example.s164403.foodstr.database.Model.Recipe;

/**
 * Created by s164403 on 6/16/2017.
 */

public class RecipeOverview extends Fragment {
    public final static String TAG = "Recipe-Overview";
    WebView webView;
    ListView recipeIngredient;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.recipe_overview, null);
    }

    @Override
    public void onStart() {
        super.onStart();
        ImageView fridge = (ImageView) getActivity().findViewById(R.id.button_fridge);
        ImageView cook = (ImageView) getActivity().findViewById(R.id.button_cook);
        fridge.setVisibility(View.VISIBLE);
        cook.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStop() {
        super.onStop();
        ImageView fridge = (ImageView) getActivity().findViewById(R.id.button_fridge);
        ImageView cook = (ImageView) getActivity().findViewById(R.id.button_cook);
        fridge.setVisibility(View.GONE);
        cook.setVisibility(View.GONE);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        webView = (WebView) getActivity().findViewById(R.id.recipe_view);
        recipeIngredient = (ListView) getActivity().findViewById(R.id.recipe_ingredient);
        final MainDatabaseHelper mainDB = new MainDatabaseHelper(getActivity());
        DatabaseRecipe reipceDB = new DatabaseRecipe(mainDB.getWritableDatabase());
        final Recipe recipe = reipceDB.getRecipe(getArguments().getLong("recipeId"));
        String html =
                "<html>" +
                        "<head>" +
                        "<style>" +
                        "html, body { " +
                            "margin: 0; " +
                            "padding-left: 5px; " +
                            "padding-right: 5px;" +
                        "}" +
                        ".textWrap{" +
                        "   float:left;" +
                        "   margin:7px;" +
                        "   width:30%;" +
                        "}" +
                        "</style>" +
                        "</head>" +
                        "<body>" +
                        "<img class=\"textWrap\" src=\"" + recipe.pictureUrl + "\" />" +
                        " <h1>" + recipe.name + "</h1>" +
                        "            <p>" +
                        recipe.description +
                        "            </p>" +
                        "</body>" +
                        "</html>";
        webView.loadDataWithBaseURL("", html, "text/html", "utf-8", "");
        int count = getArguments().getInt("peopleCount");
        final RecipeIngredientAdapter ria = new RecipeIngredientAdapter(getActivity(), recipe, mainDB.getWritableDatabase(), count);
        recipeIngredient.setAdapter(ria);
        final EditText peopleCount = (EditText) getActivity().findViewById(R.id.people_count);
        peopleCount.setText("" + count);
        peopleCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    ria.changeNumberOfPeople(Integer.parseInt(peopleCount.getText().toString()));
                }catch (Exception e){
                    Log.d(TAG, "Unable to start adapter with given person count with error " + e.getMessage());
                }
            }
        });
    }

}
