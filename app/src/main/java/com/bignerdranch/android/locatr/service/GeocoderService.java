package com.bignerdranch.android.locatr.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.text.TextUtils;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;


import com.bignerdranch.android.locatr.R;
import com.bignerdranch.android.locatr.event.AddressReadyEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class GeocoderService extends IntentService {
    private static final String TAG = "GeocoderService";

    private static final String ACTION_REVERSE_GEOCODE = "com.bignerdranch.android.locatr.service.action.REVERSE_GEOCODE";
    private static final String ACTION_GEOCODE = "com.bignerdranch.android.locatr.service.action.GEOCODE";


    private static final String EXTRA_PARAM_LOCATION = "com.bignerdranch.android.locatr.service.extra.PARAM1";






    public GeocoderService() {
        super("GeocoderService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionReverseGeocode(Context context, Location location) {
        Intent intent = new Intent(context, GeocoderService.class);
        intent.setAction(ACTION_REVERSE_GEOCODE);
        intent.putExtra(EXTRA_PARAM_LOCATION, location);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionGeocode(Context context, Location location) {

        Intent intent = new Intent(context, GeocoderService.class);
        intent.setAction(ACTION_GEOCODE);
        intent.putExtra(EXTRA_PARAM_LOCATION, location);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_REVERSE_GEOCODE.equals(action)) {
                final Location param1 = intent.getParcelableExtra(EXTRA_PARAM_LOCATION);
                handleActionReverseGeocode(param1);
            } else if (ACTION_GEOCODE.equals(action)) {
                final Location param1 = intent.getParcelableExtra(EXTRA_PARAM_LOCATION);
                handleActionGeocode(param1);
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionReverseGeocode(Location location) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());


        String errorMessage = "";
        List<Address> addresses = null;

        try {
            addresses = geocoder.getFromLocation(
                    location.getLatitude(),
                    location.getLongitude(),
                    // In this sample, get just a single address.
                    1);
        } catch (IOException ioException) {
            // Catch network or other I/O problems.
            errorMessage = getString(R.string.service_not_available);
            Log.e(TAG, errorMessage, ioException);
        } catch (IllegalArgumentException illegalArgumentException) {
            // Catch invalid latitude or longitude values.
            errorMessage = getString(R.string.invalid_lat_long_used);
            Log.e(TAG, errorMessage + ". " +
                    "Latitude = " + location.getLatitude() +
                    ", Longitude = " +
                    location.getLongitude(), illegalArgumentException);
        }

        // Handle case where no address was found.
        if (addresses == null || addresses.size()  == 0) {
            if (errorMessage.isEmpty()) {
                errorMessage = getString(R.string.no_address_found);
                Log.e(TAG, errorMessage);
            }
            broadcastAddressReadyEvent(AddressReadyEvent.FAILURE_RESULT, errorMessage,null);
        } else {
            Address address = addresses.get(0);
            ArrayList<String> addressFragments = new ArrayList<String>();

            // Fetch the address lines using getAddressLine,
            // join them, and send them to the thread.
            for(int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                addressFragments.add(address.getAddressLine(i));
            }
            Log.i(TAG, getString(R.string.address_found));
            broadcastAddressReadyEvent(AddressReadyEvent.SUCCESS_RESULT,
                    TextUtils.join(System.getProperty("line.separator"),
                            addressFragments), location);
        }



    }

    private void broadcastAddressReadyEvent(final int resultCode, final String result, final Location location) {

        EventBus.getDefault().post(new AddressReadyEvent(resultCode, result, location));
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionGeocode(Location location) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
