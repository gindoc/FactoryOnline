package com.online.factory.factoryonline.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.online.factory.factoryonline.dagger.components.FragmentComponent;
import com.online.factory.factoryonline.dagger.modules.FragmentModule;
import com.trello.rxlifecycle.components.support.RxFragment;

/**
 * Created by louiszgm on 2016/9/29.
 */

public abstract class BaseFragment<V , T extends BasePresenter<V>>  extends RxFragment {
    private FragmentComponent mComponent;
    private CharSequence mTitle;
    private T mPresent;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresent = createPresent();
        if(mPresent != null){
            mPresent.attachView((V)this);
        }
    }



    protected FragmentComponent getComponent(){
        if(mComponent == null){
            mComponent = ((BaseActivity)getActivity()).getComponent()
                    .plus(new FragmentModule());
        }
        return mComponent;
    }

    public CharSequence getmTitle() {
        return mTitle;
    }

    public void setmTitle(CharSequence mTitle) {
        this.mTitle = mTitle;
    }

    public void setComponent(FragmentComponent mComponent) {
        this.mComponent = mComponent;
    }

    protected abstract T createPresent();
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mPresent != null){
            mPresent.detachView();
        }
    }
}
