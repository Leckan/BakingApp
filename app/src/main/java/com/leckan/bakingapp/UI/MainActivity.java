package com.leckan.bakingapp.UI;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.leckan.bakingapp.Adapter.RecipeAdapter;
import com.leckan.bakingapp.Model.Recipe;
import com.leckan.bakingapp.R;
import com.leckan.bakingapp.Utilities.DownloadRecipeTask;
import com.leckan.bakingapp.Utilities.OfflineRecipeTask;
import com.leckan.bakingapp.Utilities.Utility;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.recycler_view)
     RecyclerView recipeList;
    private GridLayoutManager layoutManager;
    private final String CURRENT_SCROLL_POSITION = "currentScrollPosition";
    private int currentScrollPosition = 0;
    private List<Recipe> recipes;
    private RecipeAdapter adapter;
    ConnectivityManager mConMgr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(CURRENT_SCROLL_POSITION)) {
                currentScrollPosition = savedInstanceState.getInt(CURRENT_SCROLL_POSITION, 0);
            }
        }
        mConMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        recipes = new ArrayList<>();
        recipes.add(new Recipe());
        int mNoOfColumns = Utility.calculateNoOfColumns(getApplicationContext());
        layoutManager = new GridLayoutManager(this, mNoOfColumns);
        recipeList.setLayoutManager(layoutManager);
        adapter = new RecipeAdapter(recipes,this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);



        if(recipes.get(0).getId()== 0) {
            NetworkInfo networkInfo = mConMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                LoadOnlineRecipes();
            } else {
                LoadOfflineRecipes();
            }
        }

    }
    private void LoadOfflineRecipes() {
       new OfflineRecipeTask(MainActivity.this, recipeList, currentScrollPosition).execute();
    }

    public void LoadOnlineRecipes() {
        new DownloadRecipeTask(MainActivity.this, recipeList, currentScrollPosition).execute();

    }
    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }
}
