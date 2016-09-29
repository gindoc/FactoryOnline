package com.online.factory.factoryonline.dagger.components;

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

    ActivityComponent plus(ActivityModule module);
}
