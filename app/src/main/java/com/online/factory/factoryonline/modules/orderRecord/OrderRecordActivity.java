package com.online.factory.factoryonline.modules.orderRecord;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.base.BaseActivity;
import com.online.factory.factoryonline.customview.TitleBar;
import com.online.factory.factoryonline.databinding.ActivityOrderRecordBinding;
import com.online.factory.factoryonline.models.NeededMessage;
import com.online.factory.factoryonline.utils.StatusBarUtils;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.util.List;

import javax.inject.Inject;

/**
 * 作者: GIndoc
 * 日期: 2017/2/17 10:14
 * 作用:
 */

public class OrderRecordActivity extends BaseActivity<OrderRecordContract.View, OrderRecordPresenter> implements OrderRecordContract.View, TitleBar.OnTitleBarClickListener {

    @Inject
    OrderRecordPresenter mPresenter;

    @Inject
    OrderRecordAdapter mAdapter;

    private ActivityOrderRecordBinding mBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getComponent().inject(this);
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_order_record);

        StatusBarUtils.from(this)
                .setLightStatusBar(true)
                .setTransparentStatusbar(true)
                .setActionbarView(mBinding.rlTitle)
                .process();
        mBinding.rlTitle.setOnTitleBarClickListener(this);

        mBinding.recyclerView.setAdapter(mAdapter);
        mPresenter.requestRecord();
    }

    @Override
    protected OrderRecordPresenter createPresent() {
        return mPresenter;
    }

    @Override
    public <T> LifecycleTransformer<T> getBindToLifecycle() {
        return bindToLifecycle();
    }

    @Override
    public void showError(String error) {

    }

    @Override
    public void loadOrderRecords(List<NeededMessage> neededMessages) {
        mAdapter.addData(neededMessages);
        mBinding.recyclerView.notifyDataSetChanged();
    }

    public static Intent getStartIntent(Context context) {
        return new Intent(context, OrderRecordActivity.class);
    }

    @Override
    public void onLeftButtonClickListener(View view) {
        finish();
    }

    @Override
    public void onRightButtonClickListener(View view) {
    }
}
