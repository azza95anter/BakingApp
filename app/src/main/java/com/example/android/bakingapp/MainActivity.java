package com.example.android.bakingapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android.bakingapp.Adapter.RecipeAdapter;
import com.example.android.bakingapp.Model.RecipeModel;
import com.example.android.bakingapp.Model.StepsModel;
import com.example.android.bakingapp.Model.ingredientsModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements Steps_fragment.StepDetailsListener, RecipeAdapter.ListItemClickListener {
    private static final String TAG = "RecyclerViewForRecipes";
    public String urlRecipes = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
    public static ArrayList<RecipeModel> recipeModels = new ArrayList<>();

    Toolbar toolbarTitle;
    private RecipeAdapter recipeAdapter;
    private RecyclerView mRecyclerView;
    public static final String Recipe_ID = "id";
    public static final String Recipe_name = "name";
    public static final String recipe_Ingredients = "ingredients";
    public static final String Steps = "steps";
    public static final String Ingredient = "ingredient";
    public static final String shortDescription = "shortDescription";
    public static final String description = "description";
    public static final String measure = "measure";
    public static final String quantity = "quantity";
    public static final String videoURL = "videoURL";
    public static final String thumbnailURL = "thumbnailURL";
    public static final String servings = "servings";
    public static final String image = "image";
    private ProgressBar progressBar;
    static ArrayList<ingredientsModel> ingredientModels = new ArrayList<>();


    @Nullable
    private SimpleIdlingResource mIdlingResource;

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_Movies);

        progressBar = (ProgressBar) findViewById(R.id.pb_movies);
        toolbarTitle = (Toolbar) findViewById(R.id.toolbar);
        toolbarTitle.setTitle("Baking App");

        new RecipeTask().execute(urlRecipes);
        getIdlingResource();
        if (findViewById(R.id.activity_discovery_tablet) != null) {
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));


        } else {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
            mRecyclerView.setLayoutManager(gridLayoutManager);

        }

    }

    private void showDetailStepFragment(StepsModel step) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, FragmentStepDetails.newInstance(step))
                .commit();
    }


    @Override
    public void onStepClicked(StepsModel step) {
        showDetailStepFragment(step);
    }

    @Override
    public void onListItemClick(RecipeModel clicked) {

    }

    public class RecipeTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPreExecute() {

            progressBar.setVisibility(View.VISIBLE);
            recipeModels.clear();

        }

        @Override
        protected Integer doInBackground(String... params) {
            Integer result = 0;
            HttpURLConnection urlConnection;
            try {
                URL url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                int statusCode = urlConnection.getResponseCode();

                // 200 is mean everything is normal and HTTP Request is OK
                if (statusCode == 200) {
                    BufferedReader r = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder reader = new StringBuilder();
                    String line;
                    while ((line = r.readLine()) != null) {

                        reader.append(line);
                    }
                    // response is json result from API
                    parseResult(reader.toString());
                    result = 1; // this is mean fetching data is successful
                } else {
                    result = 0; // but here mean failed to fetch data
                }
            } catch (Exception e) {
                Log.d(TAG, e.getLocalizedMessage());
            }
            return result;
        }

        @Override
        protected void onPostExecute(Integer result) {
            progressBar.setVisibility(View.GONE);

            if (result == 1) {
                recipeAdapter = new RecipeAdapter(recipeModels, getApplicationContext(), new RecipeAdapter.ListItemClickListener() {


                    @Override
                    public void onListItemClick(RecipeModel clicked) {

                        Intent intent = new Intent(MainActivity.this, IngredientsAndSteps.class);
                        intent.putExtra("recipeModel", clicked);
                        startActivity(intent);


                    }

                });
                mRecyclerView.setAdapter(recipeAdapter);


            } else {
                Toast.makeText(MainActivity.this, "Failed to fetch data!", Toast.LENGTH_SHORT).show();


            }
        }
    }

    // this method to parse data of json  to the arrayList and calling it in doInBackground method
    public void parseResult(String result) {
        //int[] images = new int[]{R.drawable.recipe_one, R.drawable.recipe_two, R.drawable.recipe_three, R.drawable.recipe_four};
        try {
            JSONArray jsonArray = new JSONArray(result);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject recipeJson = jsonArray.getJSONObject(i);
                RecipeModel recipeModel = new RecipeModel();
                recipeModel.setId(recipeJson.getInt(Recipe_ID));
                recipeModel.setName(recipeJson.getString(Recipe_name));

                ingredientModels = new ArrayList<>();
                JSONArray ingredientJsonArray = recipeJson.getJSONArray(recipe_Ingredients);
                for (int j = 0; j < ingredientJsonArray.length(); j++) {
                    JSONObject ingredientJson = ingredientJsonArray.getJSONObject(j);
                    ingredientsModel ingredientModel = new ingredientsModel();
                    ingredientModel.setQuantity(ingredientJson.getString(quantity));
                    ingredientModel.setMeasure(ingredientJson.getString(measure));
                    ingredientModel.setIngredient(ingredientJson.getString(Ingredient));
                    ingredientModels.add(ingredientModel);
                    recipeModel.setIngredientModels(ingredientModels);

                }

                ArrayList<StepsModel> stepModels = new ArrayList<>();
                JSONArray stepJsonArray = recipeJson.getJSONArray(Steps);
                for (int b = 0; b < stepJsonArray.length(); b++) {
                    JSONObject stepJson = stepJsonArray.getJSONObject(b);
                    StepsModel stepModel = new StepsModel();
                    stepModel.setID(stepJson.getInt(Recipe_ID));
                    stepModel.setShortDescription(stepJson.getString(shortDescription));
                    stepModel.setDescription(stepJson.getString(description));
                    stepModel.setVideoURL(stepJson.getString(videoURL));
                    stepModel.setThumbnailURL(stepJson.getString(thumbnailURL));
                    stepModels.add(stepModel);
                    recipeModel.setStepModels(stepModels);


                }
                recipeModel.setServings(recipeJson.getInt(servings));
                recipeModel.setImage(recipeJson.getString(image));
                recipeModels.add(recipeModel);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();
        if (itemThatWasClickedId == R.id.goback) {
            Intent intent=new Intent(this,IngredientsAndSteps.class);
            startActivity(intent);

        }
        return super.onOptionsItemSelected(item);
    }
}

