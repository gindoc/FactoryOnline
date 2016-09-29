package com.online.factory.factoryonline.dagger.modules;

import android.content.res.Resources;

import com.github.aurae.retrofit2.LoganSquareConverterFactory;
import com.online.factory.factoryonline.BuildConfig;
import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.data.remote.FactoryApi;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;


/**
 * Created by louiszgm-pc on 2016/9/22.
 */
@Module
public class DataManagerModule {

     @Provides
     @Named("api_url")
    HttpUrl providesApiUrl(Resources resources){
         return HttpUrl.parse(resources.getString(R.string.api));
     }

    @Provides
    Converter.Factory providesLoganSquareConverter(){
        return LoganSquareConverterFactory.create();
    }

    @Provides
    public HttpLoggingInterceptor providesHttpLogger(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        HttpLoggingInterceptor.Level basic = BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.HEADERS:HttpLoggingInterceptor.Level.NONE;
        interceptor.setLevel(basic);
        return interceptor;
    }

    @Provides
    public OkHttpClient providesHttpClient(HttpLoggingInterceptor loggingInterceptor){
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();
        return client;
    }

    @Provides
    public Retrofit providesRetrofit(Converter.Factory converterFactory, @Named("api_url")HttpUrl url,OkHttpClient client){
        return new Retrofit.Builder()
                .baseUrl(url)
                .client(client)
                .addConverterFactory(converterFactory)
//                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    @Provides
    public FactoryApi providesApi(Retrofit retrofit){
        return retrofit.create(FactoryApi.class);
    }


}
