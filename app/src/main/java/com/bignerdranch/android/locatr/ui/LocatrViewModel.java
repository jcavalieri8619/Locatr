package com.bignerdranch.android.locatr.ui;

import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.bignerdranch.android.locatr.GalleryItemEntity;
import com.bignerdranch.android.locatr.injection.DaggerPhotoRepositoryComponent;
import com.bignerdranch.android.locatr.injection.PhotoRepositoryComponent;
import com.bignerdranch.android.locatr.repository.PhotoRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LocatrViewModel extends ViewModel {
    private static final String TAG = "LocatrViewModel";

    @Inject
    PhotoRepository mPhotoRepository;


    public LocatrViewModel() {

        PhotoRepositoryComponent repositoryComponent =
                DaggerPhotoRepositoryComponent.create();

        repositoryComponent.inject(this);

        mPhotoRepository.getRecentPhotos().subscribeOn(Schedulers.io())
                .subscribe(new SingleObserver<List<GalleryItemEntity>>() {
                    @Override
                    public void onSubscribe(final Disposable d) {

                        Log.d(TAG, "onSubscribe: JPC subscribed");

                    }

                    @Override
                    public void onSuccess(final List<GalleryItemEntity> galleryItemEntities) {

                        Log.d(TAG, "onSuccess:JPC fetched list of recent photos of size: " + galleryItemEntities.size());

                    }

                    @Override
                    public void onError(final Throwable e) {

                        Log.e(TAG, "onError:JPC failed to get recents from Flickr", e);
                    }
                });

    }


}
