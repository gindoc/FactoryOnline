package com.online.factory.factoryonline.base;

import android.app.Application;
import android.content.Intent;
import android.os.StrictMode;
import android.support.multidex.MultiDexApplication;

import com.baidu.mapapi.SDKInitializer;
import com.online.factory.factoryonline.BuildConfig;
import com.online.factory.factoryonline.dagger.components.ApplicationComponent;
import com.online.factory.factoryonline.dagger.components.DaggerApplicationComponent;
import com.online.factory.factoryonline.dagger.modules.ApplicationModule;
import com.online.factory.factoryonline.data.DataManager;
import com.online.factory.factoryonline.modules.main.MainActivity;
import com.online.factory.factoryonline.utils.ComponentHolder;
import com.online.factory.factoryonline.utils.Saver;
import com.squareup.leakcanary.LeakCanary;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import javax.inject.Inject;

import pub.devrel.easypermissions.EasyPermissions;
import rx.schedulers.Schedulers;
import timber.log.Timber;

import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.GINGERBREAD;

/**
 * Created by louiszgm on 2016/9/29.
 */

public class BaseApplication extends MultiDexApplication {
    private ApplicationComponent mComponent;
    @Inject
    DataManager dataManager;

    {
        PlatformConfig.setWeixin("wx9d0844d59f7344f3", "aa7a31ff1ee416e6c7d1cd33aa46b5a5");
        PlatformConfig.setSinaWeibo("769036763", "0f771400a235cfa1672e0186cd060655");
        PlatformConfig.setQQZone("1105941526", "Nct7t8yIEZqZmPcB");
        Config.REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";
    }

    @Override
    public void onCreate() {
        super.onCreate();
        UMShareAPI.get(this);
        //内存泄漏分析
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
//        enabledStrictMode();
        Saver.initSaver(this);

        getApplicationComponent().inject(this);

        // 初始化baiduMap
        SDKInitializer.initialize(this);

        //初始化Timber
        if (BuildConfig.DEBUG) {
            LeakCanary.install(this);
            Timber.plant(new Timber.DebugTree());
        }

        //替换系统默认异常处理Handler
//        Thread.setDefaultUncaughtExceptionHandler(new MyUnCaughtExceptionHandler());
    }

    private void enabledStrictMode() {
        if (SDK_INT >= GINGERBREAD) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder() //
                    .detectAll() //
                    .penaltyLog() //
                    .penaltyDeath() //
                    .build());
        }
    }

    public ApplicationComponent getApplicationComponent() {
        if (mComponent == null) {
            mComponent = DaggerApplicationComponent.builder()
                    .applicationModule(new ApplicationModule(this))
//                    .dataManagerModule()
                    .build();
        }
        ComponentHolder.setAppComponent(mComponent);
        return mComponent;
    }

    public void setComponent(ApplicationComponent component) {
        mComponent = component;
    }

    class MyUnCaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
        @Override
        public void uncaughtException(Thread thread, Throwable ex) {
            ex.printStackTrace();
            Timber.e(ex.getMessage());
            if (dataManager == null) {
                Timber.e("dataManager is null!!!!!!!!!!");
            }
            // 当APP闪退时，所做的一些处理
            Timber.e("send error msg to server!!!");
            dataManager.getRecommendAreaCats()
                    .subscribeOn(Schedulers.io())
                    .subscribe();//

            //重启app
//            restartApp();
        }

        private void restartApp() {
            Intent intent = new Intent(BaseApplication.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            BaseApplication.this.startActivity(intent);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
    }
}
