package com.bignerdranch.android.locatr.ui;

import android.support.v4.app.Fragment;

public class LocatrActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return LocatrFragment.newInstance("", "");
    }

}
