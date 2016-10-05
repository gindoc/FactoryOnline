package com.online.factory.factoryonline.dagger.modules;

import android.content.res.Resources;

import com.github.aurae.retrofit2.LoganSquareConverterFactory;
import com.online.factory.factoryonline.BuildConfig;
import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.data.remote.FactoryApi;

import java.io.IOException;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


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
    @Named("httpLogger")
    public HttpLoggingInterceptor providesHttpLogger(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        HttpLoggingInterceptor.Level basic = BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.HEADERS:HttpLoggingInterceptor.Level.NONE;
        interceptor.setLevel(basic);
        return interceptor;
    }

    @Provides
    @Named("localdata")
    public Interceptor provideLocalDataInterceptor(){
        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                String responseString = "{\n" +
                        "    \"pic\": [\n" +
                        "        \"http://www.cwenhui.com/images/zhuimeng.jpg\",\n" +
                        "        \"http://www.cwenhui.com/images/mianshi.jpg\"\n" +
                        "    ]\n" +
                        "}";
                Request request = chain.request();
                Response intercepterResponse = new Response.Builder()
                        .code(200)
                        .message(responseString)
                        .request(request)
                        .protocol(Protocol.HTTP_1_0)
                        .body(ResponseBody.create(MediaType.parse("application/json"), responseString
                                .getBytes()))
                        .addHeader("content-type", "application/json")
                        .build();
                return intercepterResponse;
            }
        };
        return BuildConfig.DEBUG ? interceptor:null;
    }

    @Provides
    public OkHttpClient provideHttpClient(@Named("httpLogger")HttpLoggingInterceptor loggingInterceptor , @Named("localdata")Interceptor localDataInterceptor){

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(loggingInterceptor);
        if(localDataInterceptor != null){
            builder.addInterceptor(localDataInterceptor);
        }
        OkHttpClient okHttpClient = builder.build();
        return okHttpClient;
    }

    @Provides
    public Retrofit providesRetrofit(Converter.Factory converterFactory, @Named("api_url")HttpUrl url,OkHttpClient client){
        return new Retrofit.Builder()
                .baseUrl(url)
                .client(client)
                .addConverterFactory(converterFactory)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    @Provides
    public FactoryApi providesApi(Retrofit retrofit){
        return retrofit.create(FactoryApi.class);
    }


}
