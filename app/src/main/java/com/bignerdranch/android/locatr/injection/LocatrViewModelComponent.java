package com.bignerdranch.android.locatr.injection;

import com.bignerdranch.android.locatr.ui.LocatrViewModel;

import dagger.Component;

@Component( dependencies = {LocationServiceComponent.class, PhotoRepositoryComponent.class})
public  interface LocatrViewModelComponent {

    void inject(LocatrViewModel viewModel);
}
