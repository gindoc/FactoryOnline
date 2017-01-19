package com.online.factory.factoryonline.modules.main.fragments.home.agent.area;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.base.BaseActivity;
import com.online.factory.factoryonline.databinding.ActivityAreaAgentBinding;
import com.trello.rxlifecycle.LifecycleTransformer;

import javax.inject.Inject;

/**
 * 作者: GIndoc
 * 日期: 2017/1/19 16:03
 * 作用:
 */

public class AreaActivity extends BaseActivity<AreaContract.View, AreaPresenter> implements AreaContract.View {

    private ActivityAreaAgentBinding mBinding;

    @Inject
    AreaPresenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getComponent().inject(this);
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_area_agent);
        mBinding.setView(this);
    }

    @Override
    protected AreaPresenter createPresent() {
        return mPresenter;
    }

    @Override
    public <T> LifecycleTransformer<T> getBindToLifecycle() {
        return bindToLifecycle();
    }

    @Override
    public void showError(String error) {

    }

    public static Intent getStartIntent(Context context) {
        return new Intent(context, AreaActivity.class);
    }
}
