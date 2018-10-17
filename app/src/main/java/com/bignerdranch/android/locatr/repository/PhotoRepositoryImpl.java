package com.bignerdranch.android.locatr.repository;

import com.bignerdranch.android.locatr.model.GalleryItemEntity;
import com.bignerdranch.android.locatr.model.GalleryItemWrapper;
import com.bignerdranch.android.locatr.webservice.FlickrAPI;

import java.util.List;

import io.reactivex.Single;

public class PhotoRepositoryImpl implements PhotoRepository {


    private FlickrAPI mWebService;


    public PhotoRepositoryImpl(final FlickrAPI webService) {
        this.mWebService = webService;
    }

    @Override
    public Single<List<GalleryItemEntity>> searchPhotosByText(final String text) {
        return mWebService.loadSearchByTextResults(text).map(this::extractFromWrapper);
    }

    @Override
    public Single<List<GalleryItemEntity>> getRecentPhotos() {
        return mWebService.loadRecents().map(this::extractFromWrapper);
    }

    @Override
    public Single<List<GalleryItemEntity>> searchPhotosByLocation(final String latitute, final String longitute) {
        return mWebService.loadSearchByLocationResults(latitute, longitute)
                .map(this::extractFromWrapper);
    }


    private List<GalleryItemEntity> extractFromWrapper(GalleryItemWrapper wrapper) {
        return wrapper.photos.photo_array;
    }
}
