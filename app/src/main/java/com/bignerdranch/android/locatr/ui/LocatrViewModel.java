package com.bignerdranch.android.locatr.ui;

import android.app.Activity;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.databinding.ObservableBoolean;
import android.location.Location;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.Pair;

import com.bignerdranch.android.locatr.locationservice.LocationService;
import com.bignerdranch.android.locatr.event.AddressReadyEvent;
import com.bignerdranch.android.locatr.injection.ContextModule;
import com.bignerdranch.android.locatr.injection.DaggerLocationServiceComponent;
import com.bignerdranch.android.locatr.injection.DaggerLocatrViewModelComponent;
import com.bignerdranch.android.locatr.injection.DaggerPhotoRepositoryComponent;
import com.bignerdranch.android.locatr.injection.LocatrViewModelComponent;
import com.bignerdranch.android.locatr.model.GalleryItemEntity;
import com.bignerdranch.android.locatr.repository.PhotoRepository;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.LatLng;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

public class LocatrViewModel extends AndroidViewModel implements LifecycleObserver{
    private static final String TAG = "LocatrViewModel";



    @Inject
    PhotoRepository mPhotoRepository;


    @Inject
    LocationService mLocationService;




    private MutableLiveData<Pair<List<GalleryItemEntity>, CoordinatesAddress>> mAdapterLiveData = new MutableLiveData<>();

    public  LatLng lastKnownLocation;
    public  LatLng lastPickedLocation;

    private PublishSubject<CoordinatesAddress> mCoordinatesAndAddressSubject = PublishSubject.create();

    public ObservableBoolean isLoading = new ObservableBoolean(true);
    public ObservableBoolean isReloading = new ObservableBoolean(false);


    public LocatrViewModel(Activity activity) {
        super(activity.getApplication());


        Log.d(TAG, "LocatrViewModel: JPC4 VIEWMODEL CREATE");

        LocatrViewModelComponent component = DaggerLocatrViewModelComponent
                .builder()
                .locationServiceComponent(DaggerLocationServiceComponent

                .builder().contextModule(new ContextModule(activity)).build())
                .photoRepositoryComponent(DaggerPhotoRepositoryComponent.create())
                .build();

        component.inject(this);

        EventBus.getDefault().register(this);






    }

    public LiveData<Pair<List<GalleryItemEntity>, CoordinatesAddress>> getAdapterLiveData() {
        return mAdapterLiveData;
    }

    public void getPhotosForLatLng(LatLng location) {

        CoordinatesAddress coordinatesAddress = new CoordinatesAddress("", location);
        lastPickedLocation = location;
        isReloading.set(true);


        loadPhotosLatLng(location, coordinatesAddress);


    }

    private void loadPhotosLatLng(final LatLng location, final CoordinatesAddress coordinatesAddress) {

        CompositeDisposable disposable = new CompositeDisposable();
        geoSearchFlickr(location)
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleObserver<List<GalleryItemEntity>>() {
                    @Override
                    public void onSubscribe(final Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(final List<GalleryItemEntity> galleryItemEntities) {


                        mAdapterLiveData.postValue(Pair.create(galleryItemEntities, coordinatesAddress));

                        isReloading.set(false);

                    }

                    @Override
                    public void onError(final Throwable e) {

                        Log.e(TAG, "onError: JPC", e);

                    }
                });
    }

    public void getPhotosForPlace(Place place) {

        Log.d(TAG, "getPhotosForPlace: JPC3");


        CoordinatesAddress coordinatesAddress = new CoordinatesAddress(place.getAddress().toString(), place.getLatLng());

        lastPickedLocation = place.getLatLng();

        isReloading.set(true);


        LatLng location = place.getLatLng();

        loadPhotosLatLng(location, coordinatesAddress);


//        CompositeDisposable disposable = new CompositeDisposable();
//        geoSearchFlickr(location)
//                .subscribeOn(Schedulers.io())
//                .subscribe(new SingleObserver<List<GalleryItemEntity>>() {
//                    @Override
//                    public void onSubscribe(final Disposable d) {
//                        disposable.add(d);
//                    }
//
//                    @Override
//                    public void onSuccess(final List<GalleryItemEntity> galleryItemEntities) {
//
//
//                        mAdapterLiveData.postValue(Pair.create(galleryItemEntities, coordinatesAddress));
//
//                        isReloading.set(false);
//
//                    }
//
//                    @Override
//                    public void onError(final Throwable e) {
//
//                        Log.e(TAG, "onError: JPC", e);
//                    }
//                });
    }

    public void loadNearbyFlickrItems() {

        final CompositeDisposable disposable = new CompositeDisposable();

        Log.d(TAG, "loadNearbyFlickrItems: JPC2");
        mLocationService.requestAccurateLocationNow()
                .observeOn(Schedulers.io())
                .flatMap(this::geoSearchFlickr)
                .toObservable()
                .zipWith(mCoordinatesAndAddressSubject, Pair::new)
                .subscribe(new Observer<Pair<List<GalleryItemEntity>, CoordinatesAddress>>() {
                    @Override
                    public void onSubscribe(final Disposable d) {

                        Log.d(TAG, "onSubscribe: loadNearbyFlickrItems SUBSCRIBE");

                        disposable.add(d);
                    }

                    @Override
                    public void onNext(final Pair<List<GalleryItemEntity>, CoordinatesAddress> pairResut) {

                        Log.d(TAG, "onNext: loadNearbyFlickrItems NEXT");

                        mAdapterLiveData.postValue(pairResut);

                        isLoading.set(false);

                    }

                    @Override
                    public void onError(final Throwable e) {

                        Log.e(TAG, "onError: loadNearbyFlickrItems FAILED", e);
                    }

                    @Override
                    public void onComplete() {

                        Log.d(TAG, "onComplete: loadNearbyFlickrItems COMPLETED");

                        disposable.clear();

                    }
                });


    }


    public Single<List<GalleryItemEntity>> geoSearchFlickr(Location location) {
        Log.d(TAG, "geoSearchFlickr: JPC2 "+location);
        return mPhotoRepository.searchPhotosByLocation(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));

    }

    public Single<List<GalleryItemEntity>> geoSearchFlickr(LatLng location) {
        Log.d(TAG, "geoSearchFlickr: JPC3 "+location);
        return mPhotoRepository.searchPhotosByLocation(String.valueOf(location.latitude), String.valueOf(location.longitude));

    }

    public Single<List<GalleryItemEntity>> textSearchFlickr(String text) {
        return mPhotoRepository.searchPhotosByText(text);

    }

    public Single<List<GalleryItemEntity>> recentSearchFlickr() {
        return mPhotoRepository.getRecentPhotos();

    }

    @Override
    protected void onCleared() {
        super.onCleared();
        EventBus.getDefault().unregister(this);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAddressReady(AddressReadyEvent event) {

        if (event.isValid()) {

            Log.d(TAG, "onAddressReady: JPC2 received AddressReady Event");

            mCoordinatesAndAddressSubject.onNext(new CoordinatesAddress(event.getAddressResult()));

        } else {
            Log.e(TAG, "onAddressReady: AddressReady Event FAILED",
                    new Throwable("AddressReady Event FAILED"));

            mCoordinatesAndAddressSubject.onNext(new CoordinatesAddress(event.getErrorResult()));

        }

        lastKnownLocation = event.getAddressResult().second;


    }


    public static class Factory extends ViewModelProvider.AndroidViewModelFactory {
        Activity mActivity;

        /**
         * Creates a {code AndroidViewModelFactory}
         *
         * param application an application to pass in {@link AndroidViewModel}
         */

        public Factory(@NonNull final Activity activity) {
            super(activity.getApplication());

            mActivity = activity;

        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull final Class<T> modelClass) {
            if (modelClass.isAssignableFrom(LocatrViewModel.class)) {
                return (T) new LocatrViewModel(mActivity);
            }

            throw new ClassCastException("invalid viewmodel class");
        }
    }




}
