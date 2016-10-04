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

    public Interceptor provideLocalDataInterceptor(){
        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();

                return null;
            }
        };
        return interceptor;
    }
    @Provides
    public OkHttpClient providesHttpClient(HttpLoggingInterceptor loggingInterceptor){

        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                String responseString = "{\"indexPicUrls\": [\"http: //www.cwenhui.com/images/zhuimeng" +
                        ".jpg\",\"http://www.cwenhui.com/images/zhuimeng.jpg\"]}";
                Response response = new Response.Builder()
                        .code(200)
                        .message(responseString)
                        .request(chain.request())
                        .protocol(Protocol.HTTP_1_0)
                        .body(ResponseBody.create(MediaType.parse("application/json"), responseString
                                .getBytes()))
                        .addHeader("content-type", "application/json")
                        .build();
                return response;
            }
        };
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addNetworkInterceptor(interceptor)
                .build();
        return client;
    }

    @Provides
    public Retrofit providesRetrofit(Converter.Factory converterFactory, @Named("api_url")HttpUrl url,OkHttpClient client){
        return new Retrofit.Builder()
                .baseUrl(url)
                .client(client)
                .addConverterFactory(converterFactory)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    @Provides
    public FactoryApi providesApi(Retrofit retrofit){
        return retrofit.create(FactoryApi.class);
    }


}
