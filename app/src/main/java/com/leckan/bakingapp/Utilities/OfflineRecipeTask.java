package com.leckan.bakingapp.Utilities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.leckan.bakingapp.Adapter.RecipeAdapter;
import com.leckan.bakingapp.Model.Ingredient;
import com.leckan.bakingapp.Model.Recipe;
import com.leckan.bakingapp.Model.Step;
import com.leckan.bakingapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Simpa on 8/7/2017.
 */

public class OfflineRecipeTask extends AsyncTask<Object, Object, Void> {
    ArrayList<Recipe> dRecipes;
    ProgressDialog pDialog;
    RecyclerView recipesList;
    RecipeAdapter adapter;
    int currentScrollPosition;
    private Resources mResources;
    private Context mContext;

    public OfflineRecipeTask(Context context, RecyclerView moviesRecyclerView, int scrollPosition) {
        mContext = context;
        recipesList = moviesRecyclerView;
        currentScrollPosition = scrollPosition;

        mResources = context.getResources();
    }

    /* private String readRecipesFromResources() throws IOException, JSONException {
         StringBuilder builder = new StringBuilder();
         InputStream in = mResources.openRawResource(R.raw.baking);
         BufferedReader reader = new BufferedReader(new InputStreamReader(in));

         String line;
         while ((line = reader.readLine()) != null) {
             builder.append(line);
         }

         //Parse resource into key/values
         final String rawJson = builder.toString();

         return  rawJson;
     }*/
    @Override
    protected Void doInBackground(Object... voids) {

        StringBuilder builder = new StringBuilder();
        try {
            InputStream in = mResources.openRawResource(R.raw.baking);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            String line;

            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Parse resource into key/values
        final String jsonStr = builder.toString();

        if (jsonStr != null) {
            try {
                JSONArray jsonArray = new JSONArray(jsonStr);

                dRecipes = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject c = jsonArray.getJSONObject(i);
                    Recipe aRecipe = new Recipe();
                    aRecipe.setId(c.getInt("id"));
                    aRecipe.setName(c.getString("name"));
                    aRecipe.setImage(c.getString("image"));
                    aRecipe.setServings(c.getInt("servings"));
                    JSONArray jsonSteps = c.getJSONArray("steps");
                    JSONArray jsonIngredients = c.getJSONArray("ingredients");
                    ArrayList<Step> steps = new ArrayList<>();
                    ArrayList<Ingredient> ingredients = new ArrayList<>();

                    for (int j = 0; j < jsonSteps.length(); j++) {
                        JSONObject d = jsonSteps.getJSONObject(j);
                        Step aStep = new Step();
                        aStep.setId(d.getInt("id"));
                        aStep.setDescription(d.getString("description"));
                        aStep.setShortDescription(d.getString("shortDescription"));
                        aStep.setThumbnailURL(d.getString("thumbnailURL"));
                        aStep.setVideoURL(d.getString("videoURL"));
                        steps.add(aStep);
                    }

                    for (int k = 0; k < jsonIngredients.length(); k++) {
                        JSONObject e = jsonIngredients.getJSONObject(k);
                        Ingredient anIngredient = new Ingredient();
                        anIngredient.setIngredient(e.getString("ingredient"));
                        anIngredient.setMeasure(e.getString("measure"));
                        anIngredient.setQuantity(e.getInt("quantity"));
                        ingredients.add(anIngredient);
                    }
                    aRecipe.setSteps(steps);
                    aRecipe.setIngredients(ingredients);
                    dRecipes.add(aRecipe);
                }
            } catch (final JSONException e) {
                Log.e("Main", "Json parsing error: " + e.getMessage());
                ((Activity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext,
                                "Json parsing error: " + e.getMessage(),
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }
        } else {
            Log.e("Main", "Couldn't get json from server.");
            ((Activity) mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mContext,
                            "Couldn't get json from file. Check LogCat for possible errors!",
                            Toast.LENGTH_LONG)
                            .show();
                }
            });

        }

        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // Showing progress dialog
        pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();

    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        // Dismiss the progress dialog
        if (pDialog.isShowing())
            pDialog.dismiss();
        /**
         * Updating parsed JSON data into ListView
         * */

        adapter = new RecipeAdapter(dRecipes, mContext);

        recipesList.setAdapter(adapter);

        if (currentScrollPosition > 0)
            recipesList.scrollToPosition(currentScrollPosition);
    }
}
