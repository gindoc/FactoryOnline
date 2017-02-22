package com.online.factory.factoryonline.dagger.components;

import android.content.Context;

import com.online.factory.factoryonline.base.BaseApplication;
import com.online.factory.factoryonline.dagger.modules.ActivityModule;
import com.online.factory.factoryonline.dagger.modules.ApplicationModule;
import com.online.factory.factoryonline.dagger.modules.DataManagerModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by louiszgm on 2016/9/29.
 */
@Singleton
@Component(modules = {ApplicationModule.class,DataManagerModule.class})
public interface ApplicationComponent {

    void inject(BaseApplication application);

    ActivityComponent plus(ActivityModule module);

    ServiceComponent plus();

    Context getContext();
}
