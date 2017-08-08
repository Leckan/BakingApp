package com.leckan.bakingapp.Utilities;

import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

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
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Simpa on 8/8/2017.
 */

public class WidgetUtil extends AsyncTask<Object, Object, Void> {
    Context context;
    String[] result;

    public WidgetUtil(Context c) {
        context = c;
    }

    public String[] FetchRandomRecipeForWidget() {
        ConnectivityManager mConMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = mConMgr.getActiveNetworkInfo();
        Resources mResources = context.getResources();
        String result = "";
        String recipeName = "";
        String jsonStr = null;
        if (networkInfo != null && networkInfo.isConnected()) {


            URL recipeURL = NetworkUtils.buildRecipeUrl();
            jsonStr = NetworkUtils.makeServiceCall(recipeURL.toString());

        }
        if (jsonStr == null) {
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
            jsonStr = builder.toString();

        }
        if (jsonStr != null) {
            try {
                JSONArray jsonArray = new JSONArray(jsonStr);

                ArrayList<Recipe> dRecipes = new ArrayList<>();
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
                Random random = new Random();

                int n = random.nextInt(dRecipes.size());
                ArrayList<Ingredient> ingredients = dRecipes.get(n).getIngredients();
                recipeName = dRecipes.get(n).getName();
                for (Ingredient i : ingredients) {
                    result = result + "\n" + i.getIngredient();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            Log.e("Main", "Couldn't get json from server.");
        }

        return new String[]{recipeName, result};
    }

    @Override
    protected Void doInBackground(Object... objects) {
        result = FetchRandomRecipeForWidget();
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);

    }
}