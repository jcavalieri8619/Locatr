package com.bignerdranch.android.locatr.ui.bindings;

import android.databinding.BindingAdapter;
import android.view.View;

public class BindingAdapters {

    @BindingAdapter("visibleGone")
    public static void VisibleGone(View v, boolean flag) {
        v.setVisibility(flag ? View.VISIBLE : View.GONE);
    }
}
