package com.example.android.bakingapp.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.bakingapp.Model.RecipeModel;
import com.example.android.bakingapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.ButterKnife;

/**
 * Created by azza anter on 3/25/2018.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolderRecipes> {
    private ArrayList<RecipeModel> recipeModels = new ArrayList<>();
    private Context context;
    private ListItemClickListener mOnClickListener;


    public RecipeAdapter(ArrayList<RecipeModel> recipeModels, Context context, ListItemClickListener listener) {
        this.context = context;
        this.recipeModels = recipeModels;
        mOnClickListener = listener;
    }


    @Override
    public ViewHolderRecipes onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipes_item, null);
        ViewHolderRecipes viewHolder = new ViewHolderRecipes(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolderRecipes holder, int position) {
        RecipeModel recipeModel = recipeModels.get(position);
     //   final String Base_URL = "https://image.tmdb.org/t/p/w185";
        holder.title.setText(recipeModel.getName());



        if(recipeModel.getImage().isEmpty()) {
            switch (position) {
                case 0:
                    holder.imageView.setImageResource(R.drawable.nutella);
                    break;
                case 1:
                    holder.imageView.setImageResource(R.drawable.brownies);
                    break;
                case 2:
                    holder.imageView.setImageResource(R.drawable.yellow);
                    break;
                case 3:
                    holder.imageView.setImageResource(R.drawable.cheesecake);
                    break;

            }
        }
        else
        {
            Picasso.with(context).load(/*Base_URL + */recipeModel.getImage()).into(holder.imageView);
        }
        holder.bind(recipeModels.get(position), mOnClickListener);

    }

    @Override
    public int getItemCount() {
        return recipeModels.size();
    }

    public class ViewHolderRecipes extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView title;

        public ViewHolderRecipes(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageID);
            title = (TextView) itemView.findViewById(R.id.TV_title);
//            itemView.setOnClickListener(mOnClickListener);
            ButterKnife.bind(this, itemView);

        }

        public void bind(final RecipeModel recipeModel, final ListItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onListItemClick(recipeModel);

                }
            });
        }
    }

    public interface ListItemClickListener {
        void onListItemClick(RecipeModel clicked);
    }

}