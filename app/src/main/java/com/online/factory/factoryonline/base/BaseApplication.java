package com.online.factory.factoryonline.base;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
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
import com.taobao.hotfix.HotFixManager;
import com.taobao.hotfix.PatchLoadStatusListener;
import com.taobao.hotfix.util.PatchStatusCode;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import javax.inject.Inject;

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

        // hotfix
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            HotFixManager.getInstance().setContext(this)
                    .setAppVersion(packageInfo.versionName)
                    .setAppId("97717-1")
                    .setAesKey("FactoryOnline888")
                    .setSupportHotpatch(true)
                    .setEnableDebug(true)
                    .setPatchLoadStatusStub(new PatchLoadStatusListener() {
                        @Override
                        public void onload(final int mode, final int code, final String info, final int handlePatchVersion) {
                            // 补丁加载回调通知
                            if (code == PatchStatusCode.CODE_SUCCESS_LOAD) {
                                // TODO: 10/24/16 表明补丁加载成功
                            } else if (code == PatchStatusCode.CODE_ERROR_NEEDRESTART) {
                                // TODO: 10/24/16 表明新补丁生效需要重启. 业务方可自行实现逻辑, 提示用户或者强制重启, 建议: 用户可以监听进入后台事件, 然后应用自杀
                            } else if (code == PatchStatusCode.CODE_ERROR_INNERENGINEFAIL) {
                                // 内部引擎加载异常, 推荐此时清空本地补丁, 但是不清空本地版本号, 防止失败补丁重复加载
                                //HotFixManager.getInstance().cleanPatches(false);
                            } else {
                                // TODO: 10/25/16 其它错误信息, 查看PatchStatusCode类说明
                            }
                        }
                    }).initialize();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

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
