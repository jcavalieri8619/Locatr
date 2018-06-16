package com.bignerdranch.android.locatr.injection;


import com.bignerdranch.android.locatr.ui.LocatrViewModel;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = PhotoRepositoryModule.class)
public interface PhotoRepositoryComponent {

    void inject(LocatrViewModel viewModel);

}
