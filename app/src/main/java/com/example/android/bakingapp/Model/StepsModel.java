package com.example.android.bakingapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by azza anter on 3/31/2018.
 */

public class StepsModel implements Parcelable {

    private int ID;
    private String shortDescription;
    private String description;
    private String videoURL;
    private String thumbnailURL;

    public StepsModel() {

    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }

    public String getDescription() {
        return description;
    }

    public int getID() {
        return ID;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    protected StepsModel(Parcel in) {
        ID = in.readInt();
        shortDescription = in.readString();
        description = in.readString();
        videoURL = in.readString();
        thumbnailURL = in.readString();

    }

    public static final Creator<StepsModel> CREATOR = new Creator<StepsModel>() {
        @Override
        public StepsModel createFromParcel(Parcel in) {
            return new StepsModel(in);
        }

        @Override
        public StepsModel[] newArray(int size) {
            return new StepsModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(ID);
        parcel.writeString(shortDescription);
        parcel.writeString(description);
        parcel.writeString(videoURL);
        parcel.writeString(thumbnailURL);
    }
}
