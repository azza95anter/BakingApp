package com.example.android.bakingapp.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.Model.StepsModel;
import com.example.android.bakingapp.R;

import java.util.ArrayList;

import butterknife.ButterKnife;

/**
 * Created by azza anter on 4/3/2018.
 */

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.ViewHolderSteps> {

    public StepClickListener clickListener;

    public interface StepClickListener {
        void onStepItemClick(StepsModel step);
    }

    public Context context;
    public ArrayList<StepsModel> steps;

    public StepAdapter(ArrayList<StepsModel> steps, Context context, StepClickListener clickListener) {
        this.clickListener = clickListener;
        this.steps = steps;
        this.context = context;

    }


    @Override
    public ViewHolderSteps onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.steps_item, null);
        StepAdapter.ViewHolderSteps viewHolder = new StepAdapter.ViewHolderSteps(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolderSteps holder, int position) {
        StepsModel stepsModel = steps.get(position);


        holder.shortDes.setText(stepsModel.getShortDescription());
        holder.bind(steps.get(position), clickListener);

    }

    @Override
    public int getItemCount() {
        return steps.size();
    }

    public class ViewHolderSteps extends RecyclerView.ViewHolder {
        public TextView shortDes;

        public ViewHolderSteps(View itemView) {
            super(itemView);
            shortDes = (TextView) itemView.findViewById(R.id.shortDesc);
            ButterKnife.bind(this, itemView);

        }

        public void bind(final StepsModel stepModel, final StepClickListener clickListener) {
            shortDes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onStepItemClick(stepModel);

                }
            });
        }
    }
}
