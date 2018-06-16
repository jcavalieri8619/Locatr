package com.bignerdranch.android.locatr.webservice;

import com.bignerdranch.android.locatr.model.GalleryItemWrapper;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface FlickrAPI {

    String BASE_URL = "https://api.flickr.com/";
    String API_KEY = "156d82fd9e142d5482f873c8d85f71ce";


    @GET("services/rest/?method=flickr.photos.getRecent")
    Single<GalleryItemWrapper> loadRecents();


    @GET("services/rest/?method=flickr.photos.search")
    Single<GalleryItemWrapper> loadSearchResults(@Query("text") String text);


}
