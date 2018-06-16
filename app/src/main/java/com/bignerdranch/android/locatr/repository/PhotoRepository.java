package com.bignerdranch.android.locatr.repository;

import com.bignerdranch.android.locatr.model.GalleryItemEntity;

import java.util.List;

import io.reactivex.Single;

public interface PhotoRepository {

    Single<List<GalleryItemEntity>> searchPhotos(String text);

    Single<List<GalleryItemEntity>> getRecentPhotos();


}
