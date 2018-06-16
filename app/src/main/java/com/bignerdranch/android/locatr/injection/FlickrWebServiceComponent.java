package com.bignerdranch.android.locatr.injection;

import com.bignerdranch.android.locatr.repository.PhotoRepository;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = FlickrWebServiceModule.class)
public interface FlickrWebServiceComponent {

    void inject(PhotoRepository repository);

}
