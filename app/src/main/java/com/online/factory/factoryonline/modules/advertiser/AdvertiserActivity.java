package com.online.factory.factoryonline.modules.advertiser;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.base.BaseActivity;
import com.online.factory.factoryonline.databinding.ActivityAdvertiserBinding;
import com.trello.rxlifecycle.LifecycleTransformer;

import javax.inject.Inject;

/**
 * Author: GIndoc on 2016/11/16 21:22
 * email : 735506583@qq.com
 * FOR   :
 */
public class AdvertiserActivity extends BaseActivity<AdvertiserContract.View, AdevertiserPresenter>
        implements AdvertiserContract.View {

    @Inject
    AdevertiserPresenter mPresenter;

    private ActivityAdvertiserBinding mBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_advertiser);
    }

    @Override
    protected AdevertiserPresenter createPresent() {
        return mPresenter;
    }

    @Override
    public <T> LifecycleTransformer<T> getBindToLifecycle() {
        return null;
    }

    @Override
    public void showError(String error) {

    }
}
