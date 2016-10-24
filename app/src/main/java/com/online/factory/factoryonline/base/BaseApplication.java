package com.online.factory.factoryonline.base;

import android.app.Application;
import android.content.Intent;
import android.os.StrictMode;

import com.baidu.mapapi.SDKInitializer;
import com.online.factory.factoryonline.BuildConfig;
import com.online.factory.factoryonline.dagger.components.ApplicationComponent;
import com.online.factory.factoryonline.dagger.components.DaggerApplicationComponent;
import com.online.factory.factoryonline.dagger.modules.ApplicationModule;
import com.online.factory.factoryonline.data.DataManager;
import com.online.factory.factoryonline.modules.main.MainActivity;
import com.online.factory.factoryonline.utils.ComponentHolder;
import com.squareup.leakcanary.LeakCanary;

import javax.inject.Inject;

import rx.schedulers.Schedulers;
import timber.log.Timber;

import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.GINGERBREAD;

/**
 * Created by louiszgm on 2016/9/29.
 */

public class BaseApplication extends Application {
    private ApplicationComponent mComponent;
    @Inject
    DataManager dataManager;
    @Override
    public void onCreate() {
        super.onCreate();
        //内存泄漏分析
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
//        enabledStrictMode();
        LeakCanary.install(this);

        getApplicationComponent().inject(this);

        // 初始化baiduMap
        SDKInitializer.initialize(this);

        //初始化Timber
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        //替换系统默认异常处理Handler
        Thread.setDefaultUncaughtExceptionHandler(new MyUnCaughtExceptionHandler());
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

    public ApplicationComponent getApplicationComponent(){
        if(mComponent == null){
            mComponent = DaggerApplicationComponent.builder()
                    .applicationModule(new ApplicationModule(this))
//                    .dataManagerModule()
                    .build();
        }
        ComponentHolder.setAppComponent(mComponent);
        return  mComponent;
    }

    public void setComponent(ApplicationComponent component){
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
