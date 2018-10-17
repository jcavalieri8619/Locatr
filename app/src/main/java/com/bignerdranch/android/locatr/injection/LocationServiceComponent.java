package com.bignerdranch.android.locatr.injection;

import com.bignerdranch.android.locatr.locationservice.LocationService;

import dagger.Component;

@Component(modules = LocationServiceModule.class)
public interface LocationServiceComponent {


    LocationService injectLocationService();
}
