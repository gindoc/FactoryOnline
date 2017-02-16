package com.online.factory.factoryonline.modules.FactoryDetail.advertiser.record;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.base.BaseActivity;
import com.online.factory.factoryonline.customview.TitleBar;
import com.online.factory.factoryonline.databinding.ActivityRecordBinding;
import com.online.factory.factoryonline.utils.StatusBarUtils;
import com.trello.rxlifecycle.LifecycleTransformer;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * 作者: GIndoc
 * 日期: 2016/11/17 10:33
 * 作用:
 */

public class RecordActivity extends BaseActivity<RecordContract.View, RecordPresenter> implements RecordContract.View, TitleBar.OnTitleBarClickListener {

    @Inject
    RecordPresenter mPresenter;
    private ActivityRecordBinding mBinding;

    public static Intent getIntent(Context context) {
        return new Intent(context, RecordActivity.class);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_record);
        mBinding.setView(this);
        StatusBarUtils.from(this)
                //白底黑字状态栏
                .setLightStatusBar(true)
                //设置toolbar,actionbar等view
                .setTransparentStatusbar(true)
                .setActionbarView(mBinding.rlTitle)
                .process();
        mBinding.rlTitle.setOnTitleBarClickListener(this);
    }

    @Override
    protected RecordPresenter createPresent() {
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

    @Override
    public void onLeftButtonClickListener(View view) {
        finish();
    }

    @Override
    public void onRightButtonClickListener(View view) {

    }
}
