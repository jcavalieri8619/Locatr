package com.bignerdranch.android.locatr.injection;

import android.content.Context;

import com.bignerdranch.android.locatr.locationservice.LocationService;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import dagger.Module;
import dagger.Provides;

@Module(includes = ContextModule.class)
public class LocationServiceModule {



    @Provides
    public LocationService locationService(
                                           FusedLocationProviderClient locationProviderClient,
                                           Context context) {

        return new LocationService( locationProviderClient, context);

    }

//    @Provides
//    public GoogleApiClient googleApiClient(Context context) {
//
//
//        return new GoogleApiClient.Builder(context)
//                .addApi(LocationServices.API)
//
//                .build();
//    }

    @Provides
    public FusedLocationProviderClient locationProviderClient(Context context) {
        return LocationServices
                .getFusedLocationProviderClient(context);
    }


}
