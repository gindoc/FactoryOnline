package com.online.factory.factoryonline.dagger.modules;

import android.support.v4.app.FragmentManager;

import com.online.factory.factoryonline.base.BaseActivity;
import com.online.factory.factoryonline.base.BaseFragment;

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

}
