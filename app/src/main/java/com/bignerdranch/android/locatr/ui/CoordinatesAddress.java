package com.bignerdranch.android.locatr.ui;

import android.location.Location;
import android.util.Pair;

import com.google.android.gms.maps.model.LatLng;

public class CoordinatesAddress {

    public String address;
    public LatLng location;

    public CoordinatesAddress(final String address, final Location location) {
        this.address = address;
        this.location = new LatLng(location.getLatitude(), location.getLongitude());
    }

    public CoordinatesAddress(final String address, final LatLng location) {
        this.address = address;
        this.location = location;
    }

    public CoordinatesAddress(Pair<String, LatLng> arg) {
        this.address = arg.first;
        this.location = arg.second;
    }

}