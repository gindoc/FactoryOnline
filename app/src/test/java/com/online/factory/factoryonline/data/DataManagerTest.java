package com.online.factory.factoryonline.data;

import com.bluelinelabs.logansquare.LoganSquare;
import com.google.gson.JsonObject;
import com.online.factory.factoryonline.BuildConfig;
import com.online.factory.factoryonline.dagger.components.ApplicationComponent;
import com.online.factory.factoryonline.dagger.components.DaggerApplicationComponent;
import com.online.factory.factoryonline.dagger.modules.ApplicationModule;
import com.online.factory.factoryonline.data.remote.FactoryApi;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.plugins.RxJavaPlugins;
import rx.plugins.RxJavaSchedulersHook;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by louiszgm on 2016/10/5.
 */

public class DataManagerTest {
    private String JSON_ROOT_PATH = "/json/";
    private String jsonFullPath;

    private FactoryApi api;
    @Before
    public void setUp()  {
        //输出日志
        ShadowLog.stream = System.out;

        setDataManager();
    }


    @Test
    public void getIndexPicUrlsTest(){
        // 让Schedulers.io()返回当前线程
        RxJavaPlugins.getInstance().registerSchedulersHook(new RxJavaSchedulersHook() {
            @Override
            public Scheduler getIOScheduler() {
                return Schedulers.immediate();
            }
        });
        final ArrayList<String> result = new ArrayList<>();
        Observable<JsonObject> observable = api.getIndexPicUrls();
        observable.subscribe(new Subscriber<JsonObject>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(JsonObject jsonObject) {
                try {
                    List<String> pics = LoganSquare.parseList(jsonObject.getAsJsonArray("pics").getAsString(),String.class);
                    result.addAll(pics);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        Assert.assertEquals(0,result.size());
    }
    private void setDataManager() {
        //获取测试json文件地址
        try {
            jsonFullPath = getClass().getResource(JSON_ROOT_PATH).toURI().getPath();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        //定义Http Client,并添加拦截器
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new MockInterceptor(jsonFullPath))
                .build();

        //设置Http Client
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.livecoding.tv:443")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpClient)
                .build();

         api = retrofit.create(FactoryApi.class);
    }

    @After
    public void tearDown() throws Exception {

    }

}