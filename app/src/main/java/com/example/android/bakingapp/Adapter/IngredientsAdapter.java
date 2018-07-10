package com.example.android.bakingapp.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.Model.ingredientsModel;
import com.example.android.bakingapp.R;

import java.util.ArrayList;

/**
 * Created by azza anter on 3/30/2018.
 */

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.ViewHolderRecipes> {
    private ArrayList<ingredientsModel> ingredientsModels;

    private Context context;

    public IngredientsAdapter(ArrayList<ingredientsModel> ingredientsModels, Context context) {
        this.context = context;
        this.ingredientsModels = ingredientsModels;
    }

    @Override
    public IngredientsAdapter.ViewHolderRecipes onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredients, null);
        IngredientsAdapter.ViewHolderRecipes viewHolder = new IngredientsAdapter.ViewHolderRecipes(itemLayoutView);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(ViewHolderRecipes holder, int position) {
        ingredientsModel ingredientsModel = ingredientsModels.get(position);


        holder.ingredient.setText(ingredientsModel.getIngredient());

        holder.quantity.setText(ingredientsModel.getQuantity());
        holder.measure.setText(ingredientsModel.getMeasure());
    }

    @Override
    public int getItemCount() {
        return ingredientsModels.size();
    }

    public class ViewHolderRecipes extends RecyclerView.ViewHolder {

        public TextView ingredient, quantity, measure;

        public ViewHolderRecipes(View itemView) {
            super(itemView);

            ingredient = (TextView) itemView.findViewById(R.id.TV_ingredient);
            quantity = (TextView) itemView.findViewById(R.id.tv_quantity);
            measure = (TextView) itemView.findViewById(R.id.tv_measure);

        }
    }
}
