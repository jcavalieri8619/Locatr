package com.bignerdranch.android.locatr.locationservice;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LifecycleObserver;
import android.content.Context;
import android.databinding.ObservableBoolean;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;

import com.bignerdranch.android.locatr.service.GeocoderService;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;

public class LocationService implements LifecycleObserver {
    private static final String TAG = "LocationService";

    //FIXME this is deprecated
    private GoogleApiClient mGoogleApiClient;

    private Context mActivityContext;
    private FusedLocationProviderClient mLocationProviderClient;

    private ObservableBoolean mIsGoogleAPIConnected;



    public LocationService(final GoogleApiClient ApiClient,
                           final FusedLocationProviderClient locationClient,
                           final Context context) {

        mIsGoogleAPIConnected = new ObservableBoolean(false);
        mGoogleApiClient = ApiClient;
        mLocationProviderClient = locationClient;
        mActivityContext = context;

        registerAPIConnectionCallback();


    }

    public LocationService(
                           final FusedLocationProviderClient locationClient,
                           final Context context) {

        mIsGoogleAPIConnected = new ObservableBoolean(false);
        mLocationProviderClient = locationClient;
        mActivityContext = context;

//        registerAPIConnectionCallback();


    }

    public ObservableBoolean IsGoogleAPIConnected() {
        return mIsGoogleAPIConnected;
    }

    private void unregisterAPIConnectionCallback() {
        mGoogleApiClient.unregisterConnectionCallbacks(connectionCallbacks);

    }

    private void registerAPIConnectionCallback() {
        mGoogleApiClient.registerConnectionCallbacks(connectionCallbacks);
    }


    @SuppressLint("MissingPermission")
    public Single<Location> requestAccurateLocationNow() {

        final LocationRequest request = LocationRequest.create();

        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setNumUpdates(1);
        request.setInterval(0);


//        mLocationProviderClient.getAccurateLocationNow().addOnSuccessListener(new OnSuccessListener<Location>() {
//            @Override
//            public void onSuccess(final Location location) {
//                if (location != null) {
//
//                    Log.d(TAG, "onSuccess: JPC starting GeoCoder and got a fix at:  " + location.toString());
//
//
//                } else {
//                    Log.d(TAG, "onSuccess: JPC got null fix");
//
//                }
//
//            }
//        });




        return Single.create(new SingleOnSubscribe<Location>() {


            @Override
            public void subscribe(final SingleEmitter<Location> emitter) throws Exception {

                LocationCallback locationCallback = new LocationCallback() {
                    @Override
                    public void onLocationResult(final LocationResult locationResult) {
                        super.onLocationResult(locationResult);


                        if (locationResult != null) {

                            GeocoderService.startActionReverseGeocode(mActivityContext, locationResult.getLastLocation());

                            emitter.onSuccess(locationResult.getLastLocation());
                        } else {
                            Throwable t = new Throwable("NULL LOCATION RESULT");
                            Log.e(TAG, "onLocationResult: locationRESULT null", t);
                            emitter.onError(t);

                        }

                    }
                };

                mLocationProviderClient.requestLocationUpdates(request, locationCallback, Looper.getMainLooper());

            }
        });




    }


    private GoogleApiClient.ConnectionCallbacks connectionCallbacks = new GoogleApiClient.ConnectionCallbacks() {
        @Override
        public void onConnected(@Nullable final Bundle bundle) {
            mIsGoogleAPIConnected.set(true);

        }

        @Override
        public void onConnectionSuspended(final int i) {

            mIsGoogleAPIConnected.set(false);
        }
    };


}
