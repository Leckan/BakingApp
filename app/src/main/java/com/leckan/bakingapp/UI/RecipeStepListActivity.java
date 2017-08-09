package com.leckan.bakingapp.UI;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.leckan.bakingapp.Adapter.RecipeStepsAdapter;
import com.leckan.bakingapp.Model.Ingredient;
import com.leckan.bakingapp.Model.Recipe;
import com.leckan.bakingapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * An activity representing a list of Recipes. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link RecipeDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class RecipeStepListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private Recipe theRecipe;
    private final String CURRENT_SCROLL = "currentScroll";
    private final String RECIPE_SAVED_STATE = "recipe";
    private int currentScrollPosition = 0;

    @BindView(R.id.recipe_ingredient)
    TextView ingredientView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step_list);


        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(RECIPE_SAVED_STATE)) {
                theRecipe = savedInstanceState.getParcelable(RECIPE_SAVED_STATE);
            }
            if (savedInstanceState.containsKey(CURRENT_SCROLL)) {
                currentScrollPosition = savedInstanceState.getInt(CURRENT_SCROLL,0);
            }

        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(theRecipe == null) {
            theRecipe = getIntent().getParcelableExtra("theRecipe");
        }
        setTitle(theRecipe.getName());
        toolbar.setTitle(theRecipe.getName());

        ButterKnife.bind(this);

        View recyclerView = findViewById(R.id.recipe_list);
        assert recyclerView != null;

        String ingredient = "Ingredients: ";
        if(theRecipe != null) {
            for (Ingredient i : theRecipe.getIngredients()) {
                ingredient = ingredient + i.getIngredient() + ", ";
            }
            ingredient = ingredient.substring(0, ingredient.length() - 3) + ".";
        }
        ingredientView.setText(ingredient);
        if (findViewById(R.id.recipe_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
        setupRecyclerView((RecyclerView) recyclerView);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpTo(this, new Intent(this, MainActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(RECIPE_SAVED_STATE)) {
                theRecipe = savedInstanceState.getParcelable(RECIPE_SAVED_STATE);
            }
            if (savedInstanceState.containsKey(CURRENT_SCROLL)) {
                currentScrollPosition = savedInstanceState.getInt(CURRENT_SCROLL,0);
            }

        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putParcelable(RECIPE_SAVED_STATE,theRecipe);
    }
    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        if(theRecipe != null) {
            recyclerView.setAdapter(new RecipeStepsAdapter(theRecipe.getSteps(),theRecipe, mTwoPane, this));
        }
    }

}
