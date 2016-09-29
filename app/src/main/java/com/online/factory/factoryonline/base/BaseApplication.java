package com.online.factory.factoryonline.base;

import android.app.Application;

import com.online.factory.factoryonline.dagger.components.ApplicationComponent;
import com.online.factory.factoryonline.dagger.components.DaggerApplicationComponent;
import com.online.factory.factoryonline.dagger.modules.ApplicationModule;

/**
 * Created by louiszgm on 2016/9/29.
 */

public class BaseApplication extends Application {
    private ApplicationComponent mComponent;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public ApplicationComponent getApplicationComponent(){
        if(mComponent == null){
            mComponent = DaggerApplicationComponent.builder()
                    .applicationModule(new ApplicationModule(this))
                    .build();
        }
        return  mComponent;
    }

    public void setComponent(ApplicationComponent component){
        mComponent = component;
    }
}
