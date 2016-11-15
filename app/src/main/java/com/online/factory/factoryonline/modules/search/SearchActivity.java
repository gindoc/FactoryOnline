package com.online.factory.factoryonline.modules.search;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.base.BaseActivity;
import com.online.factory.factoryonline.databinding.ActivitySearchBinding;
import com.trello.rxlifecycle.LifecycleTransformer;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Author: GIndoc on 2016/11/15 21:28
 * email : 735506583@qq.com
 * FOR   :
 */
public class SearchActivity extends BaseActivity<SearchContract.View, SearchPresenter> implements SearchContract.View {

    @Inject
    SearchPresenter mPresenter;

    private ActivitySearchBinding mBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_search);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_search);
    }

    @Override
    protected SearchPresenter createPresent() {
        return mPresenter;
    }

    @Override
    public <T> LifecycleTransformer<T> getBindToLifecycle() {
        return bindToLifecycle();
    }

    @Override
    public void showError(String error) {
        Timber.e(error);
    }
}
