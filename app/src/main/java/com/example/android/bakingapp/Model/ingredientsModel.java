package com.example.android.bakingapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by azza anter on 3/30/2018.
 */

public class ingredientsModel implements Parcelable {
    private String quantity;
    private String ingredient;
    private String measure;

    public ingredientsModel() {
    }

    protected ingredientsModel(Parcel in) {
        quantity = in.readString();
        ingredient = in.readString();
        measure = in.readString();
    }

    public static final Creator<ingredientsModel> CREATOR = new Creator<ingredientsModel>() {
        @Override
        public ingredientsModel createFromParcel(Parcel in) {
            return new ingredientsModel(in);
        }

        @Override
        public ingredientsModel[] newArray(int size) {
            return new ingredientsModel[size];
        }
    };

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public void setMeasure(String measure) {
        this.measure = measure;

    }

    public String getIngredient() {
        return ingredient;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getMeasure() {
        return measure;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(quantity);
        parcel.writeString(ingredient);
        parcel.writeString(measure);
    }
}
