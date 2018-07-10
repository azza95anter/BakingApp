package com.example.android.bakingapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by azza anter on 3/25/2018.
 */

public class RecipeModel implements Parcelable {
    private Integer id;
    private String name;
    private ArrayList<ingredientsModel> IngredientModels;
    private ArrayList<StepsModel> StepModels;
    private Integer servings;
    private String image;


    public RecipeModel() {

    }

    // setter
    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIngredientModels(ArrayList<ingredientsModel> IngredientModels) {
        this.IngredientModels = IngredientModels;
    }

    public void setStepModels(ArrayList<StepsModel> StepModels) {
        this.StepModels = StepModels;
    }

    public void setServings(Integer servings) {
        this.servings = servings;
    }

    public void setImage(String image) {
        this.image = image;
    }
// getter
    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ArrayList<ingredientsModel> getIngredientModels() {
        return IngredientModels;
    }

    public ArrayList<StepsModel> getStepModels() {
        return StepModels;
    }

    public Integer getServings() {
        return servings;
    }

    public String getImage() {
        return image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeTypedList(IngredientModels);
        dest.writeTypedList(StepModels);
        dest.writeInt(servings);
        dest.writeString(image);


    }

    private RecipeModel(Parcel in) {
        id = in.readInt();
        name = in.readString();
        IngredientModels = in.createTypedArrayList(ingredientsModel.CREATOR);
        StepModels = in.createTypedArrayList(StepsModel.CREATOR);
        servings=in.readInt();
        image=in.readString();

    }


    public static final Creator<RecipeModel> CREATOR = new Creator<RecipeModel>() {
        @Override
        public RecipeModel createFromParcel(Parcel in) {
            return new RecipeModel(in);
        }

        @Override
        public RecipeModel[] newArray(int size) {
            return new RecipeModel[size];
        }
    };
}
