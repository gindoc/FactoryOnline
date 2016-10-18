package com.online.factory.factoryonline.dagger.modules;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;

import com.online.factory.factoryonline.modules.login.LoginContext;

import javax.inject.Named;
import javax.inject.Singleton;

import com.online.factory.factoryonline.modules.login.LoginContext;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import rx.subjects.BehaviorSubject;

/**
 * Created by louiszgm-pc on 2016/9/21.
 */
@Module
public class ApplicationModule {
    private Application mApp;
    public ApplicationModule(Application app){
        mApp = app;
    }

    @Provides
    public SharedPreferences providesPreferences(){
        return PreferenceManager.getDefaultSharedPreferences(mApp);
    }
    @Provides
    public Resources providesResources(){
        return mApp.getResources();
    }

    @Provides
    public Context provideContext(){
        return mApp;
    }

    @Provides
    @Singleton
    public LoginContext provideLoginContext(){
        return new LoginContext();
    }

    @Provides
    public BehaviorSubject provideBehaviorSubject(){
        return BehaviorSubject.create();
    }
}
