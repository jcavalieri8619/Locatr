package com.bignerdranch.android.locatr.ui;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.android.locatr.databinding.FragmentGeoPhotoViewerBinding;
import com.bignerdranch.android.locatr.R;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GeoPhotoViewer#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GeoPhotoViewer extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM_PHOTO_URL = "paramPhotoURL";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParamPhotoUrl;
    private String mParam2;


    private FragmentGeoPhotoViewerBinding mBinding;


    public GeoPhotoViewer() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param photoURL Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GeoPhotoViewer.
     */
    // TODO: Rename and change types and number of parameters
    public static GeoPhotoViewer newInstance(String photoURL, String param2) {
        GeoPhotoViewer fragment = new GeoPhotoViewer();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM_PHOTO_URL, photoURL);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParamPhotoUrl = getArguments().getString(ARG_PARAM_PHOTO_URL);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_geo_photo_viewer, container, false);

        View root = mBinding.getRoot();

        Picasso.get().load(mParamPhotoUrl).fit().centerCrop().into(mBinding.image);



        return root;

    }

}
