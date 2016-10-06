package com.online.factory.factoryonline.base;

import android.app.Application;

import com.online.factory.factoryonline.BuildConfig;
import com.online.factory.factoryonline.dagger.components.ApplicationComponent;
import com.online.factory.factoryonline.dagger.components.DaggerApplicationComponent;
import com.online.factory.factoryonline.dagger.modules.ApplicationModule;
import com.online.factory.factoryonline.utils.ComponentHolder;

import timber.log.Timber;

/**
 * Created by louiszgm on 2016/9/29.
 */

public class BaseApplication extends Application {
    private ApplicationComponent mComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
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
}
