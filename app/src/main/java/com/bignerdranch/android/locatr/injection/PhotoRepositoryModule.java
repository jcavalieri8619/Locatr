package com.bignerdranch.android.locatr.injection;

import com.bignerdranch.android.locatr.repository.PhotoRepository;
import com.bignerdranch.android.locatr.repository.PhotoRepositoryImpl;
import com.bignerdranch.android.locatr.webservice.FlickrAPI;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(includes = FlickrWebServiceModule.class)
public class PhotoRepositoryModule {

    @Provides
    public PhotoRepository photoRepository(FlickrAPI webService) {
        return new PhotoRepositoryImpl(webService);
    }
}
