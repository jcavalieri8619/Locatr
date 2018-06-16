package com.bignerdranch.android.locatr.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class GalleryItemWrapper {

    @Expose
    public PhotosResult photos;

    public static class PhotosResult {


        @Expose
        public int page;

        @Expose
        public int pages;

        @Expose
        public int perpage;

        @Expose
        public int total;

        @Expose
        @SerializedName("photo")
        public List<GalleryItemEntity> photo_array = new ArrayList<>();


    }
}
