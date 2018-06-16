package com.bignerdranch.android.locatr.injection;

import com.bignerdranch.android.locatr.webservice.FlickrAPI;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class FlickrWebServiceModule {


    @Singleton
    @Provides
    public FlickrAPI flickerAPI(Retrofit retrofit) {
        return retrofit.create(FlickrAPI.class);
    }


    @Provides
    public Retrofit retrofit(OkHttpClient okHttpClient,
                             GsonConverterFactory gsonConverterFactory, Gson gson) {
        return new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(FlickrAPI.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(gsonConverterFactory)
                .build();
    }

    @Provides
    public Gson gson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        return gsonBuilder.serializeNulls()
                .excludeFieldsWithoutExposeAnnotation()
                .setPrettyPrinting().create();
    }

    @Provides
    public GsonConverterFactory gsonConverterFactory(Gson gson) {
        return GsonConverterFactory.create(gson);
    }

    @Provides
    public OkHttpClient okHttpClient() {

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request originalRequest = chain.request();

                        HttpUrl originalHttpUrl = originalRequest.url();

                        HttpUrl url = originalHttpUrl
                                .newBuilder()
                                .addQueryParameter("api_key", FlickrAPI.API_KEY)
                                .addQueryParameter("format", "json")
                                .addQueryParameter("nojsoncallback", "1")
                                .addQueryParameter("extras", "url_s")
                                .build();


                        Request.Builder requestBuilder = originalRequest.newBuilder()
                                .url(url);

                        Request newRequest = requestBuilder.build();

                        return chain.proceed(newRequest);
                    }
                }).build();

        return okHttpClient;
    }


}
