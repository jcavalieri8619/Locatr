package com.bignerdranch.android.locatr.injection;


import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class ContextModule {

    Context mContext;

    public ContextModule(final Context context) {
        mContext = context;
    }

    @Provides
    Context context() {
        return mContext;
    }
}
