package com.example.android.bakingapp;

/**
 * Created by azza anter on 4/12/2018.
 */

public class TextUtils {
    public static String getExtension(String string) {
        if (string.length() == 3) {
            return string;
        } else if (string.length() > 3) {
            return string.substring(string.length() - 3);
        } else {
            throw new IllegalArgumentException(" the Word has less than 3 characters");
        }
    }

}
