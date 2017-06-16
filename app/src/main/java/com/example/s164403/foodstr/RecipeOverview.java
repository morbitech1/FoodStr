package com.example.s164403.foodstr;

import android.app.Fragment;
import android.content.Context;
import android.database.ContentObservable;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.ListView;

import com.example.s164403.foodstr.R;
import com.example.s164403.foodstr.database.DatabaseRecipe;
import com.example.s164403.foodstr.database.MainDatabaseHelper;
import com.example.s164403.foodstr.database.Model.Recipe;

/**
 * Created by s164403 on 6/16/2017.
 */

public class RecipeOverview extends Fragment {
    WebView webView;
    ListView listView;
    Recipe recipe;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.recipe_overview, null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        webView = (WebView) getActivity().findViewById(R.id.recipe_view);
        listView = (ListView) getActivity().findViewById(R.id.recipe_ingredient);
        MainDatabaseHelper mainDatabaseHelper = new MainDatabaseHelper(getActivity());
        SQLiteDatabase db = mainDatabaseHelper.getWritableDatabase();
        DatabaseRecipe databaseRecipe = new DatabaseRecipe(db);
        recipe = databaseRecipe.getRecipe(getArguments().getLong("recipeId"));
        String html =
                        "<html>" +
                                "<head>" +
                                "<style>" +
                                "html, body { margin: 0; padding: 0}" +
                                ".textWrap{" +
                                "            float:left;" +
                                "           margin-right:5px;" +
                                "           margin-bottom:5px;" +
                                "            width:30%;" +
                                "        }" +
                                "</style>" +
                                "</head>" +
                                "<body>" +
                                "<figure>        " +
                                "                <img class=\"textWrap\" src=\""+recipe.pictureUrl+"\" />" +
                                "            </figure>   " +
                                " <h1>" + recipe.name + "</h1>" +
                                "            <p>" +
                                recipe.description +
                                "            </p>" +
                                "</body>" +
                                "</html>";
        webView.loadDataWithBaseURL("", html, "text/html", "utf-8", "");
        listView.setAdapter(new RecipeIngredientAdapter(getActivity(), recipe, db ,getArguments().getInt("peopleCount")));

    }

}
