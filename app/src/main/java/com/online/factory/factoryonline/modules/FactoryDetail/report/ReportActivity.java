package com.online.factory.factoryonline.modules.FactoryDetail.report;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.base.BaseActivity;
import com.online.factory.factoryonline.databinding.ActivityReportBinding;
import com.trello.rxlifecycle.LifecycleTransformer;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * 作者: GIndoc
 * 日期: 2016/11/14 14:21
 * 作用: 举报页面
 */
public class ReportActivity extends BaseActivity<ReportContract.View, ReportPresenter> implements ReportContract.View{
    private ActivityReportBinding mBinding;

    @Inject
    ReportPresenter mPresenter;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, ReportActivity.class);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getComponent().inject(this);
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_report);

    }

    @Override
    protected ReportPresenter createPresent() {
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
