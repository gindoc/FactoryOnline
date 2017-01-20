package com.online.factory.factoryonline.modules.order;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.base.BaseActivity;
import com.online.factory.factoryonline.databinding.ActivityOrderBinding;
import com.online.factory.factoryonline.modules.main.MainActivity;
import com.online.factory.factoryonline.utils.StatusBarUtils;
import com.trello.rxlifecycle.LifecycleTransformer;

import timber.log.Timber;

/**
 * 作者: GIndoc
 * 日期: 2017/1/18 16:57
 * 作用:
 */

public class OrderActivity extends BaseActivity<OrderContract.View, OrderPresenter> implements OrderContract.View, View.OnTouchListener {
    private ActivityOrderBinding mBinding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getComponent().inject(this);
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_order);
        mBinding.setView(this);

        StatusBarUtils.from(this)
                .setLightStatusBar(true)
                .setActionbarView(mBinding.rlTitle)
                .process();

        mBinding.radioGroup.getChildAt(0).setOnTouchListener(this);
        mBinding.radioGroup.getChildAt(1).setOnTouchListener(this);
        mBinding.radioGroup.getChildAt(2).setOnTouchListener(this);
        mBinding.etInputTime.setOnTouchListener(this);
    }

    @Override
    protected OrderPresenter createPresent() {
        return null;
    }

    @Override
    public <T> LifecycleTransformer<T> getBindToLifecycle() {
        return getBindToLifecycle();
    }

    @Override
    public void showError(String error) {

    }

    public static Intent getStartIntent(Context context) {
        return new Intent(context, OrderActivity.class);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v instanceof RadioButton) {
            mBinding.radioGroup.setFocusable(true);
            mBinding.radioGroup.setFocusableInTouchMode(true);
            mBinding.radioGroup.requestFocus();
            ((RadioButton)v).setChecked(true);
            mBinding.etInputTime.setBackgroundResource(R.drawable.gray_rectangle_outline_with_corner);
            mBinding.etInputTime.setTextColor(Color.parseColor("#909090"));
            if (TextUtils.isEmpty(mBinding.etInputTime.getText())){
                mBinding.etInputTime.setHint("其他");
            }
        } else if (v instanceof EditText) {
            v.setFocusable(true);
            v.setFocusableInTouchMode(true);
            v.requestFocus();
            ((EditText) v).setHint("");
            ((EditText) v).setTextColor(Color.WHITE);
            v.setBackgroundResource(R.drawable.green_rectangle_solid_with_corner);
            mBinding.radioGroup.clearCheck();
        }
        return false;
    }
}
