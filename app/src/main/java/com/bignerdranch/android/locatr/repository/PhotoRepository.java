package com.bignerdranch.android.locatr.repository;

import com.bignerdranch.android.locatr.model.GalleryItemEntity;
import com.bignerdranch.android.locatr.model.GalleryItemWrapper;
import com.bignerdranch.android.locatr.webservice.FlickrAPI;

import java.util.List;

import io.reactivex.Single;

public interface PhotoRepository{

    Single<List<GalleryItemEntity>> searchPhotosByText(String text);

    Single<List<GalleryItemEntity>> getRecentPhotos();


    Single<List<GalleryItemEntity>> searchPhotosByLocation(String latitute, String longitute);
}
