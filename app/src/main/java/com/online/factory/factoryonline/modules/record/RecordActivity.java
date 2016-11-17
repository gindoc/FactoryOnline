package com.online.factory.factoryonline.modules.record;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.base.BaseActivity;
import com.online.factory.factoryonline.databinding.ActivityRecordBinding;
import com.online.factory.factoryonline.databinding.ActivityReportBinding;
import com.online.factory.factoryonline.modules.report.ReportPresenter;
import com.trello.rxlifecycle.LifecycleTransformer;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * 作者: GIndoc
 * 日期: 2016/11/17 10:33
 * 作用:
 */

public class RecordActivity extends BaseActivity<RecordContract.View, RecordPresenter> implements RecordContract.View {

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
}
