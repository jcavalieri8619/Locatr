package com.bignerdranch.android.locatr.injection;


import com.bignerdranch.android.locatr.repository.PhotoRepository;
import com.bignerdranch.android.locatr.ui.LocatrViewModel;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = PhotoRepositoryModule.class)
public interface PhotoRepositoryComponent {

    PhotoRepository injectPhotoRepository();
}
