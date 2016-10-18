package com.online.factory.factoryonline.dagger.modules;

import android.content.Context;
import android.support.v4.app.FragmentManager;

import com.online.factory.factoryonline.base.BaseActivity;
import com.online.factory.factoryonline.base.BaseFragment;
import com.online.factory.factoryonline.modules.login.fragments.PwdLoginFragment;
import com.online.factory.factoryonline.modules.login.fragments.SmsLoginFragment;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * Created by louiszgm-pc on 2016/9/21.
 */
@Module
public class ActivityModule {
    private WeakReference<BaseActivity> mActivity;
    public ActivityModule(BaseActivity activity){
        mActivity = new WeakReference<BaseActivity>(activity);
    }

    @Provides
    public BaseActivity  providesActivity(){
        return mActivity.get();
    }

    @Provides
    public FragmentManager providesFragmentManager(){
        return mActivity.get().getSupportFragmentManager();
    }

    @Provides
    @Named("LoginFragments")
    public ArrayList<BaseFragment> provideLoginFragments(PwdLoginFragment pwdLoginFragment , SmsLoginFragment smsLoginFragment){
        ArrayList<BaseFragment> list = new ArrayList<>();
        list.add(pwdLoginFragment);
        list.add(smsLoginFragment);
        return list;
    }
}
