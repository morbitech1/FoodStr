package com.example.s164403.foodstr.database;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.example.s164403.foodstr.R;

/**
 * Created by s164403 on 6/16/2017.
 */

public class RecipeOverview extends Fragment {
    WebView webView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.recipe_overview, null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        webView = (WebView) getActivity().findViewById(R.id.recipe_view);
        String name = getArguments().getString("recipeName");
        String description =  getArguments().getString("recipeDescription");
        String url = getArguments().getString("recipeURL");
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
                                "<title>" +name+"</title>"+
                                "</head>" +
                                "<body>" +
                                "<figure>        " +
                                "                <img class=\"textWrap\" src=\""+url+"\" />" +
                                "            </figure>   " +
                                "            <p>" +
                                description +
                                "            </p>" +
                                "</body>" +
                                "</html>";
        webView.loadDataWithBaseURL("", html, "text/html", "utf-8", "");
    }

}