package com.bignerdranch.android.locatr.model;

public interface GalleryItem {

    String getHeight();

    void setHeight(String height);

    void setWidth(String width);

    String getWidth();

    String getID();

    void setID(String ID);

    String getOwner();

    void setOwner(String owner);

    String getCaption();

    void setCaption(String caption);

    double getLatitude();

    void setLatitude(double latitude);

    double getLongitude();

    void setLongitude(double longitude);


    String getUrl();

    void setUrl(String url);


}
