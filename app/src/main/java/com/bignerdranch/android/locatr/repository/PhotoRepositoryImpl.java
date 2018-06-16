package com.bignerdranch.android.locatr.repository;

import com.bignerdranch.android.locatr.GalleryItemEntity;
import com.bignerdranch.android.locatr.GalleryItemWrapper;
import com.bignerdranch.android.locatr.webservice.FlickrAPI;

import java.util.List;

import io.reactivex.Single;

public class PhotoRepositoryImpl implements PhotoRepository {


    FlickrAPI mWebService;


    public PhotoRepositoryImpl(final FlickrAPI webService) {
        this.mWebService = webService;
    }

    @Override
    public Single<List<GalleryItemEntity>> searchPhotos(final String text) {
        return mWebService.loadSearchResults(text).map(this::extractFromWrapper);
    }

    @Override
    public Single<List<GalleryItemEntity>> getRecentPhotos() {
        return mWebService.loadRecents().map(this::extractFromWrapper);
    }


    private List<GalleryItemEntity> extractFromWrapper(GalleryItemWrapper wrapper) {
        return wrapper.photos.photo_array;
    }
}
