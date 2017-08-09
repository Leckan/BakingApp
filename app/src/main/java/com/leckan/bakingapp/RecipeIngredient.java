package com.leckan.bakingapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.leckan.bakingapp.Model.Ingredient;
import com.leckan.bakingapp.Model.Recipe;
import com.leckan.bakingapp.UI.MainActivity;
import com.leckan.bakingapp.Utilities.WidgetUtil;

import java.util.ArrayList;
import java.util.Random;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeIngredient extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, String recipeName, String ingredients) {

        CharSequence widgetText = recipeName;
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_ingredient);
        views.setTextViewText(R.id.appwidget_text, widgetText);

        CharSequence widgetIngredients = ingredients;
        views.setTextViewText(R.id.appwidget_details, widgetIngredients);

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,0);

        views.setOnClickPendingIntent(R.id.widget_container, pendingIntent);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them

      ArrayList<Recipe> recipeArrayList= new WidgetUtil(context).FetchRecipesForWidget();

        for (int appWidgetId : appWidgetIds) {
            Random random = new Random();
            int n = random.nextInt(recipeArrayList.size());
            ArrayList<Ingredient> ingredients = recipeArrayList.get(n).getIngredients();

            String ingredient = "Ingredients: ";
            for (Ingredient i : ingredients) {
                ingredient = ingredient  + i.getIngredient() + ", "; }
            ingredient = ingredient.substring(0,ingredient.length() - 3) + ".";

            String recipeName = recipeArrayList.get(n).getName();

            updateAppWidget(context, appWidgetManager, appWidgetId, recipeName, ingredient);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

