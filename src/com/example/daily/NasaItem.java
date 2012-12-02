package com.example.daily;

import android.graphics.Bitmap;

/**
 * User: Yves-T
 * Date: 25/11/12
 * Time: 19:35
 */
public class NasaItem {
    private String mTitle;
    private String mDescription;
    private String mDate;
    private Bitmap mImage;

    public NasaItem(String mTitle, String mDescription, String mDate, Bitmap image) {
        this.mTitle = mTitle;
        this.mDescription = mDescription;
        this.mDate = mDate;
        this.mImage = image;
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmDescription() {
        return mDescription;
    }

    public String getmDate() {
        return mDate;
    }

    public Bitmap getmImageUrl() {
        return mImage;
    }
}
