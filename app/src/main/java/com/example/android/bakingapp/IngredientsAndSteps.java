package com.example.android.bakingapp;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.android.bakingapp.Model.RecipeModel;
import com.example.android.bakingapp.Model.StepsModel;
import com.example.android.bakingapp.Model.ingredientsModel;

public class IngredientsAndSteps extends AppCompatActivity implements Steps_fragment.StepDetailsListener {

    public static RecipeModel recipeModel;

    FragmentManager fragmentManager = getSupportFragmentManager();
    int flag = 0;
    Toolbar toolbarview;
    ImageView steeeps;
    public static boolean mTwoPane;
    public StepsModel step;
    private Steps_fragment steps_fragment;
    private ingredients_fragment ingredients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients_and_steps);

        toolbarview = (Toolbar) findViewById(R.id.toolbar);
        recipeModel = (RecipeModel) getIntent().getParcelableExtra("recipeModel");
        toolbarview.setTitle(recipeModel.getName());
        steeeps = (ImageView) findViewById(R.id.imageView);

        if (findViewById(R.id.container) != null) {
            mTwoPane = true;
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, FragmentStepDetails.newInstance(step))
                    .commit();
            steps_fragment = Steps_fragment.newInstance(recipeModel.getStepModels());
            fragmentManager.beginTransaction().replace(R.id.Steps_container, steps_fragment).commit();
            // to show the ingredients
            ingredients = ingredients_fragment.newObject(recipeModel.getIngredientModels());
            fragmentManager.beginTransaction().replace(R.id.ingredient_container, ingredients).commit();
        } else {
            mTwoPane = false;
            // to show the steps
            // based on the reviewer comment i add "   if (savedInstanceState == null) "
            if (savedInstanceState == null) {
                steps_fragment = Steps_fragment.newInstance(recipeModel.getStepModels());
                fragmentManager.beginTransaction().replace(R.id.Steps_container, steps_fragment).commit();
                // to show the ingredients
             ingredients = ingredients_fragment.newObject(recipeModel.getIngredientModels());
                fragmentManager.beginTransaction().replace(R.id.ingredient_container, ingredients).commit();
            } else {
                ingredients = (ingredients_fragment) fragmentManager.getFragment(savedInstanceState, "ingredientFragment");
                steps_fragment = (Steps_fragment) fragmentManager.getFragment(savedInstanceState, "stepFragment");
            }
        }
        createWidget();

    }

    private void showDetailStepFragment(StepsModel step) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, FragmentStepDetails.newInstance(step))
                .commit();
    }


    @Override
    public void onStepClicked(StepsModel step) {
        if (mTwoPane) {
            showDetailStepFragment(step);
        } else {
            showDetailStepFragment(step);
        }

    }

    private void createWidget() {

        String ingredientsList = "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < recipeModel.getIngredientModels().size(); i++) {
            ingredientsModel ingredient = recipeModel.getIngredientModels().get(i);
            String name = ingredient.getIngredient();
            String measure = ingredient.getMeasure();
            String quantity = ingredient.getQuantity();

            String ing = String.format("%s %s %s \n", quantity, measure, name);
            sb.append(ing);
            ingredientsList = sb.toString();
        }
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        RemoteViews remoteViews = new RemoteViews(this.getPackageName(), R.layout.baking_widget);
        ComponentName thisWidget = new ComponentName(this, BakingWidget.class);
        remoteViews.setTextViewText(R.id.appwidget_head_text, recipeModel.getName());
        remoteViews.setTextViewText(R.id.appwidget_text, ingredientsList);
        appWidgetManager.updateAppWidget(thisWidget, remoteViews);
        Toast.makeText(this, "added to widget",
                Toast.LENGTH_LONG).show();

    }

    public void clickBack(View view) {
        finish();
    }
}
