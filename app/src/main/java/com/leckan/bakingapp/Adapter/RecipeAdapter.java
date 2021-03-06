package com.leckan.bakingapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.leckan.bakingapp.Model.Recipe;
import com.leckan.bakingapp.R;
import com.leckan.bakingapp.UI.RecipeStepListActivity;
import com.leckan.bakingapp.Utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Thinkpad on 8/7/2017.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>{

    private static final String TAG = RecipeAdapter.class.getSimpleName();
    // final private ListItemClickListener mOnClickListener;
    private static int viewHolderCount;
    public List<Recipe> recipeList;
    LayoutInflater inflater;

    public RecipeAdapter(List<Recipe> recipes, Context c) {
        this.inflater = LayoutInflater.from(c);
        this.recipeList = recipes;
    }


    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recipe_item,parent,false);

        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position) {
        Recipe recipe = recipeList.get(position);
        holder.viewRecipeName.setText(recipe.getName());
        if(! recipe.getImage().isEmpty())
        {
            URL url = NetworkUtils.buildImageUrl(recipe.getImage());
            Picasso.with(this.inflater.getContext()).load(url.toString()).placeholder(R.drawable.baking2)
                    .error(R.drawable.baking1)
                    .into( holder.listRecipeImageView);
        }
        else {
             Picasso.with(this.inflater.getContext()).load(R.drawable.baking2).placeholder(R.drawable.baking2)
                    .error(R.drawable.baking1)
                        .into( holder.listRecipeImageView);
        }
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }


    public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.recipe_image)        ImageView listRecipeImageView;
        @BindView(R.id.recipe_name)        TextView viewRecipeName;
        @BindView(R.id.recipe_item_root) View container;


        public RecipeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            int clickedPosition = getAdapterPosition();
            Recipe selectedRecipe = recipeList.get(clickedPosition);
            Intent intent = new Intent(inflater.getContext(), RecipeStepListActivity.class);
            intent.putExtra("theRecipe",selectedRecipe);
            inflater.getContext().startActivity(intent);
        }
    }
}
