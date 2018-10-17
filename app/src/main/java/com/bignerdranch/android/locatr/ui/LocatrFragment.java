package com.bignerdranch.android.locatr.ui;


import android.Manifest;
import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bignerdranch.android.locatr.databinding.FragmentLocatrBinding;
import com.bignerdranch.android.locatr.R;
import com.bignerdranch.android.locatr.model.GalleryItem;
import com.bignerdranch.android.locatr.model.GalleryItemEntity;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LocatrFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LocatrFragment extends Fragment {
    private static final String TAG = "LocatrFragment";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final int REQUEST_LOCATION_PERM = 0;

    private static final int PLACE_PICKER_RESULT = 1;


    private static final String SAVED_PAGER_POSITION = "savedPagerPosition";
    private static final String EXTRA_LATLNG_RESULT = "latlngResultData";


    private static final String[] LOCATION_PERMS = {Manifest.permission.ACCESS_FINE_LOCATION};
    private static final int REQUEST_COORDINATES = 2;



    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private ImageView mImageView;
    private LocatrViewModel mViewModel;


    private CompositeDisposable mDisposable = new CompositeDisposable();
    private FragmentLocatrBinding mBinding;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setHasOptionsMenu(true);


        if (!hasLocationPermissions()) {
            Log.d(TAG, "getLocationFix: attempting to get permissions");
            requestPermissions(LOCATION_PERMS, REQUEST_LOCATION_PERM);

        }


    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

         mBinding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_locatr, container, false);

        View root = mBinding.getRoot();


        setupViewPager();


        return root;
    }

    private void setupViewPager() {
        mBinding.geophotoPager.setAdapter(new GeoPhotoPagerAdapter(getChildFragmentManager()));
    }

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        LocatrViewModel.Factory factory = new LocatrViewModel.Factory(getActivity());
        mViewModel = ViewModelProviders.of(getActivity(), factory).get(LocatrViewModel.class);

        if (hasLocationPermissions() && savedInstanceState == null) {

            mViewModel.loadNearbyFlickrItems();

        }

        mBinding.setIsLoading(mViewModel.isLoading);
        mBinding.setIsReloading(mViewModel.isReloading);

        mViewModel.getAdapterLiveData().observe(this, listCoordinatesAddressPair -> {
            if (listCoordinatesAddressPair != null && !listCoordinatesAddressPair.first.isEmpty()) {

                ((GeoPhotoPagerAdapter) mBinding.geophotoPager.getAdapter()).submitItems(listCoordinatesAddressPair);

                int pager_position = 0;
                if (savedInstanceState!=null  && savedInstanceState.containsKey(SAVED_PAGER_POSITION)) {
                    pager_position = savedInstanceState.getInt(SAVED_PAGER_POSITION);
                    savedInstanceState.remove(SAVED_PAGER_POSITION);

                }
                mBinding.geophotoPager.setCurrentItem(pager_position, true);


            } else {

                Toast.makeText(getActivity(), "No Photos found for this location! ", Toast.LENGTH_SHORT).show();
            }



        });






    }

    @Override
    public void onSaveInstanceState(@NonNull final Bundle outState) {

        int currentPosition = mBinding.geophotoPager.getCurrentItem();

        outState.putInt(SAVED_PAGER_POSITION, currentPosition);


        super.onSaveInstanceState(outState);

    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();



    }

    public LocatrFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LocatrFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LocatrFragment newInstance(String param1, String param2) {
        LocatrFragment fragment = new LocatrFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    public static Intent resultIntent(LatLng resultData) {

        Intent resultIntent = new Intent();

        resultIntent.putExtra(EXTRA_LATLNG_RESULT, resultData);

        return resultIntent;

    }

    private LatLng getLatLngFromResultIntent(Intent resultIntent) {
        return resultIntent.getParcelableExtra(EXTRA_LATLNG_RESULT);
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_locatr, menu);


        MenuItem searchItem = menu.findItem(R.id.menu_item_locate);

    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_locate:

                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(getActivity()), PLACE_PICKER_RESULT);
                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                    Log.e(TAG, "onOptionsItemSelected: ", e);
                    Toast.makeText(getContext(), "ERROR: Cannot Launch Place Picker", Toast.LENGTH_LONG).show();
                }

                return true;


            case R.id.menu_item_map:


                Intent intent = MapsActivity.newInstance(getActivity(), mViewModel.lastKnownLocation, mViewModel.lastPickedLocation);
                startActivityForResult(intent, REQUEST_COORDINATES);






                return true;
            default:

                return super.onOptionsItemSelected(item);


        }

    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {

        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case PLACE_PICKER_RESULT:


                        Place place = PlacePicker.getPlace(getActivity(), data);


                    Log.d(TAG, "onActivityResult: JPC13 attributions: "+ place.getAttributions());

                    Log.d(TAG, "onActivityResult: JPC13 name: "+ place.getName());


                    Log.d(TAG, "onActivityResult: JPC13 websiteUri: "+ place.getWebsiteUri());


                    Log.d(TAG, "onActivityResult: JPC13 address: "+ place.getAddress());

                    Log.d(TAG, "onActivityResult: JPC13 getting RESULTS from place picker: " + place.toString());

                    mViewModel.getPhotosForPlace(place);

                    break;

                case REQUEST_COORDINATES:
                    Log.d(TAG, "onActivityResult: JPC8 receiving REQUEST_COORDINATES result intent");


                    

                    mViewModel.getPhotosForLatLng(getLatLngFromResultIntent(data));


                    break;
                default:

                    super.onActivityResult(requestCode, resultCode, data);

            }
        } else {
            Log.d(TAG, "onActivityResult: JPC8 failure to receive result back from activity");

        }


    }

    private boolean hasLocationPermissions() {
        int result = ContextCompat.checkSelfPermission(getContext().getApplicationContext(), LOCATION_PERMS[0]);

        return result == PackageManager.PERMISSION_GRANTED;
    }



    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {

        Log.d(TAG, "onRequestPermissionsResult: JPC request code: " + requestCode);
        switch (requestCode) {
            case REQUEST_LOCATION_PERM:

                if (hasLocationPermissions()) {

                    Log.d(TAG, "onRequestPermissionsResult: JPC permission granted");

                    //TODO re-call locationFix here after getting permissions
                    assert mViewModel != null;
                    mViewModel.loadNearbyFlickrItems();

                }



                break;
            default:
                Log.d(TAG, "onRequestPermissionsResult: JPC got wrong requestCode");

                super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        }

    }
}
