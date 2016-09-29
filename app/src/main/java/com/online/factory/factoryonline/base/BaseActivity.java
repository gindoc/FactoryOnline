package com.online.factory.factoryonline.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.online.factory.factoryonline.dagger.components.ActivityComponent;
import com.online.factory.factoryonline.dagger.components.ApplicationComponent;
import com.online.factory.factoryonline.dagger.modules.ActivityModule;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

/**
 * Created by louiszgm on 2016/9/29.
 */

public abstract class BaseActivity<V , T extends BasePresenter<V>> extends RxAppCompatActivity {
    private ActivityComponent mComponent;
    protected T mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = createPresent();
        if(mPresenter != null){
            V view = (V) this;
            mPresenter.attachView(view);
        }

    }

    protected abstract T createPresent();

    public ActivityComponent getComponent(){
        if(mComponent == null){
            ApplicationComponent appComponent = ((BaseApplication)getApplication()).getApplicationComponent();
            mComponent = appComponent
                    .plus(new ActivityModule(this));
        }
        return mComponent;
    }

    public void setComponent(ActivityComponent mComponent) {
        this.mComponent = mComponent;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mPresenter != null){
            mPresenter.detachView();
        }

    }
}