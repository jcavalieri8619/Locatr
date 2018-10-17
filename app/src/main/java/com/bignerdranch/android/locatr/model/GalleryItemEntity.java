package com.bignerdranch.android.locatr.model;

import android.net.Uri;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GalleryItemEntity implements GalleryItem {


    @Expose
    @SerializedName("id")
    private String mID;

    @Expose
    @SerializedName("title")
    private String mCaption;

    @Expose
    @SerializedName("url_s")
    private String mUrl;

    @Expose
    @SerializedName("height_s")
    private String mHeight;

    @Expose
    @SerializedName("width_s")
    private String mWidth;

    @Expose
    @SerializedName("owner")
    private String mOwner;


    @Expose
    @SerializedName("latitude")
    private double mLatitude;

    @Expose
    @SerializedName("longitude")
    private double mLongitude;


    public Uri constructPhotoPageUri() {
        final String FLICKR_URI = "https://www.flickr.com/photos/";

        return Uri.parse(FLICKR_URI)
                .buildUpon()
                .appendPath(getOwner())
                .appendPath(getID())
                .build();
    }


    @Override
    public String getHeight() {
        return mHeight;
    }

    @Override
    public void setHeight(final String height) {

        mHeight = height;
    }

    @Override
    public void setWidth(String width) {

        mWidth = width;
    }

    @Override
    public String getWidth() {
        return mWidth;
    }

    @Override
    public String getID() {
        return mID;
    }

    @Override
    public void setID(final String ID) {

        mID = ID;
    }

    @Override
    public String getOwner() {
        return mOwner;
    }

    @Override
    public void setOwner(final String owner) {
        mOwner = owner;
    }

    @Override
    public String getCaption() {
        return mCaption;
    }

    @Override
    public void setCaption(final String caption) {
        mCaption = caption;
    }

    @Override
    public double getLatitude() {
        return mLatitude;
    }

    @Override
    public void setLatitude(final double latitude) {

        mLatitude = latitude;
    }

    @Override
    public double getLongitude() {
        return mLongitude;
    }

    @Override
    public void setLongitude(final double longitude) {
        mLongitude = longitude;
    }

    @Override
    public String getUrl() {
        return mUrl;
    }

    @Override
    public void setUrl(final String url) {

        mUrl = url;
    }

    @Override
    public String toString() {
        return mCaption;

    }
}
