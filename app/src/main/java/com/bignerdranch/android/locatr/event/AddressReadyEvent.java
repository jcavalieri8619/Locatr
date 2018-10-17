package com.bignerdranch.android.locatr.event;

import android.location.Location;
import android.util.Pair;

import com.google.android.gms.maps.model.LatLng;

public class AddressReadyEvent {

    public static final int SUCCESS_RESULT = 0;
    public static final int FAILURE_RESULT = 1;

    private int resultCode;
    private String result;
    private LatLng location;


    public AddressReadyEvent(final int resultCode, final String result, final Location location) {

        this.resultCode = resultCode;
        this.result = result;
        this.location = new LatLng(location.getLatitude(), location.getLongitude());

    }

    public AddressReadyEvent(final int resultCode, final String result, final LatLng location) {

        this.resultCode = resultCode;
        this.result = result;
        this.location = location;

    }




    public Pair<String,LatLng> getErrorResult() {
        return resultCode == FAILURE_RESULT ? null : Pair.create(result,location);
    }


    public Pair<String,LatLng> getAddressResult() {
        return resultCode == SUCCESS_RESULT ?
                Pair.create(result,location) : null;

    }

    public boolean isValid() {
        return resultCode == SUCCESS_RESULT;
    }
}
