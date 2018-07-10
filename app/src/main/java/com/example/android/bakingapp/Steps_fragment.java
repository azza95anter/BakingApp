package com.example.android.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.bakingapp.Adapter.StepAdapter;
import com.example.android.bakingapp.Model.StepsModel;

import java.util.ArrayList;

import butterknife.ButterKnife;

import static com.example.android.bakingapp.IngredientsAndSteps.mTwoPane;

/**
 * Created by azza anter on 4/3/2018.
 */

public class Steps_fragment extends Fragment implements StepAdapter.StepClickListener {
    public Steps_fragment() {

    }

    RecyclerView rvStep;

    private StepAdapter stepAdapter;
    private ArrayList<StepsModel> steps = new ArrayList<>();
    private StepDetailsListener stepDetailsListener;

    public interface StepDetailsListener {
        void onStepClicked(StepsModel step);
    }

    @Override
    public void onStepItemClick(StepsModel step) {
        int index = steps.indexOf(step);
        if(mTwoPane)
        {
            stepDetailsListener.onStepClicked(step);

        }
else {


            Intent intent = new Intent(getActivity(), VidoesActivity.class);
            intent.putParcelableArrayListExtra("steps", steps);
            intent.putExtra("stepIndex", index);
            startActivity(intent);
        }
    }


    public static Steps_fragment newInstance(ArrayList<StepsModel> steps) {
        Steps_fragment stepFragment = new Steps_fragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("steps", steps);
        stepFragment.setArguments(bundle);
        return stepFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_steps, container, false);
        ButterKnife.bind(this, view);

        if (getArguments() != null) {
            steps = getArguments().getParcelableArrayList("steps");
        }
        rvStep = (RecyclerView) view.findViewById(R.id.rv_list_steps);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvStep.setLayoutManager(layoutManager);
        stepAdapter = new StepAdapter(steps, getContext(), this);
        rvStep.setAdapter(stepAdapter);


        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        stepDetailsListener = (StepDetailsListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        stepDetailsListener = null;
    }

}
