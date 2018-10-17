package com.bignerdranch.android.locatr.ui;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Pair;
import android.view.ViewGroup;

import com.bignerdranch.android.locatr.model.GalleryItem;
import com.bignerdranch.android.locatr.model.GalleryItemEntity;

import java.util.List;

public class GeoPhotoPagerAdapter extends FragmentStatePagerAdapter {



    List<? extends GalleryItem> mItems;
    CoordinatesAddress mCoordinatesAndAddress;


    private FragmentManager mFragmentManager;

    public GeoPhotoPagerAdapter(final FragmentManager fm) {
        super(fm);
        mFragmentManager = fm;

    }

    /**
     * Called when the host view is attempting to determine if an item's position
     * has changed. Returns {@link #POSITION_UNCHANGED} if the position of the given
     * item has not changed or {@link #POSITION_NONE} if the item is no longer present
     * in the adapter.
     * <p>
     * <p>The default implementation assumes that items will never
     * change position and always returns {@link #POSITION_UNCHANGED}.
     *
     * @param object Object representing an item, previously returned by a call to
     *               {link #instantiateItem(View, int)}.
     * @return object's new position index from [0, {@link #getCount()}),
     * {@link #POSITION_UNCHANGED} if the object's position has not changed,
     * or {@link #POSITION_NONE} if the item is no longer present.
     */
    @Override
    public int getItemPosition(@NonNull final Object object) {

        /**
         * workaround to handle reloading data set otherwise,
         * the Fragment backstack will still contain fragments
         * displaying data associated with stale data set
         */
        return POSITION_NONE;
    }

    /**
     * Return the Fragment associated with a specified position.
     *
     * @param position
     */
    @Override
    public Fragment getItem(final int position) {
        String placeHolderParam = null;
        return GeoPhotoViewer.newInstance(mItems.get(position).getUrl(), placeHolderParam);
    }

    /**
     * Return the number of views available.
     */
    @Override
    public int getCount() {
        return mItems == null ? 0 : mItems.size();
    }



    /**
     * This method may be called by the ViewPager to obtain a title string
     * to describe the specified page. This method may return null
     * indicating no title for this page. The default implementation returns
     * null.
     *
     * @param position The position of the title requested
     * @return A title for the requested page
     */
    @Nullable
    @Override
    public CharSequence getPageTitle(final int position) {
        StringBuilder builder = new StringBuilder();
        builder.append(mCoordinatesAndAddress.address).append("\n").append(mCoordinatesAndAddress.location.toString());
        return builder.toString();
    }

    public void submitItems(Pair<List<GalleryItemEntity>, CoordinatesAddress> items) {
        mItems = items.first;
        mCoordinatesAndAddress = items.second;
        notifyDataSetChanged();


    }
}
