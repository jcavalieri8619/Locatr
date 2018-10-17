package com.bignerdranch.android.locatr.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.bignerdranch.android.locatr.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MapsActivity extends FragmentActivity
        implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private static final String TAG = "MapsActivity";

    private static final String EXTRA_LAST_KNOW_LATLNG = "last_known_location";
    private static final String EXTRA_PLACE_PICKER_LATLNG = "place_picker_location";


    private GoogleMap mMap;

    private LatLng lastKnownLocation;
    private LatLng pickedLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // logically should never be null

        lastKnownLocation = getIntent().getParcelableExtra(EXTRA_LAST_KNOW_LATLNG);


            if (getIntent().getParcelableExtra(EXTRA_PLACE_PICKER_LATLNG) !=null) {
                pickedLocation = getIntent().getParcelableExtra(EXTRA_PLACE_PICKER_LATLNG);

            }



    }



    public static Intent newInstance(Context context, LatLng myLocation) {

        Intent intent = new Intent(context, MapsActivity.class);
        intent.putExtra(EXTRA_LAST_KNOW_LATLNG, myLocation);


        return intent;
    }

    public static Intent newInstance(Context context, LatLng myLocation, LatLng pickedLocation) {

        Intent intent = new Intent(context, MapsActivity.class);
        intent.putExtra(EXTRA_LAST_KNOW_LATLNG, myLocation);
        intent.putExtra(EXTRA_PLACE_PICKER_LATLNG, pickedLocation);



        return intent;
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMapClickListener(this);

        LatLngBounds.Builder builder = new LatLngBounds.Builder()
                .include(lastKnownLocation);
        mMap.addMarker(new MarkerOptions().position(lastKnownLocation).title("Your Location"));

        if (pickedLocation != null) {

            builder.include(pickedLocation);

            mMap.addMarker(new MarkerOptions().position(pickedLocation).title("Picked Location"));

        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        int margin = getResources().getDimensionPixelSize(R.dimen.map_inset_margin);

        CameraUpdate update = CameraUpdateFactory.newLatLngBounds(builder.build(), margin);

        mMap.animateCamera(update);


//        mMap.moveCamera(CameraUpdateFactory.newLatLng(lastKnownLocation));

    }

    @Override
    public void onMapClick(final LatLng latLng) {
        Log.d(TAG, "onMapClick: JPC8 handing map Click");

//        ComponentName componentName;
//        if ((componentName = getCallingActivity()) != null) {
//            Log.d(TAG, "onMapClick: JPC8 calling activity name: " + componentName.getClassName());
//
//            try {
//                Method method = componentName.getClass().getMethod("resultIntent", LatLng.class);
//                setResult(Activity.RESULT_OK, (Intent) method.invoke(latLng));
//
//            } catch (NoSuchMethodException|IllegalAccessException | InvocationTargetException e) {
//
//                Log.e(TAG, "onMapClick: JPC8 cannot get resultIntent method on calling activity", e);
//                setResult(Activity.RESULT_CANCELED);
//            }
//
//        }
        setResult(Activity.RESULT_OK,  LocatrFragment.resultIntent(latLng));
        finish();


    }
}
