package com.leckan.bakingapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.leckan.bakingapp.Model.Recipe;
import com.leckan.bakingapp.Model.Step;
import com.leckan.bakingapp.R;
import com.leckan.bakingapp.UI.RecipeDetailActivity;
import com.leckan.bakingapp.UI.RecipeDetailFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Simpa on 8/7/2017.
 */

public class RecipeStepsAdapter extends RecyclerView.Adapter<RecipeStepsAdapter.RecipeStepViewHolder>{

    private static final String TAG = RecipeAdapter.class.getSimpleName();
    // final private ListItemClickListener mOnClickListener;
    private static int viewHolderCount;
    public List<Step> stepList;
    LayoutInflater inflater;
    public Recipe theRecipe;
    private boolean mTwoPane;
    private FragmentActivity myContext;


    public RecipeStepsAdapter(List<Step> steps,Recipe recipe, boolean twoPane, Context c) {
        this.inflater = LayoutInflater.from(c);
        this.stepList = steps;
        mTwoPane = twoPane;
        theRecipe = recipe;
        myContext = (FragmentActivity) c;
    }
    @Override
    public RecipeStepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(parent.getContext())
        .inflate(R.layout.recipe_step_list_content,parent,false);

        return new RecipeStepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeStepViewHolder holder, int position) {

        Step step = stepList.get(position);
        holder.viewRecipeDescription.setText(step.getDescription());

    }

    @Override
    public int getItemCount() {
        return stepList.size();
    }

    public class RecipeStepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        @BindView(R.id.recipe_decription)
        TextView viewRecipeDescription;
        @BindView(R.id.Recipe_steps_container) View container;


        public RecipeStepViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            int clickedPosition = getAdapterPosition();
            Step selectedStep = stepList.get(clickedPosition);

            if (mTwoPane) {
                Bundle arguments = new Bundle();
                RecipeDetailFragment fragment = new RecipeDetailFragment();
                arguments.putParcelable("theStep",selectedStep);
                fragment.setArguments(arguments);
                myContext.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.recipe_detail_container, fragment)
                        .commit();
            } else {
                Context context = view.getContext();
                Intent intent = new Intent(context, RecipeDetailActivity.class);
               // intent.putExtra(RecipeDetailFragment.ARG_ITEM_ID, holder.mItem.id);

                intent.putExtra("theStep",selectedStep);
                intent.putExtra("theRecipe",theRecipe);
                context.startActivity(intent);
            }
        }
    }
}
