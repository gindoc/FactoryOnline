package com.online.factory.factoryonline.modules.order;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.base.BaseActivity;
import com.online.factory.factoryonline.customview.TitleBar;
import com.online.factory.factoryonline.databinding.ActivityOrderBinding;
import com.online.factory.factoryonline.modules.login.LoginContext;
import com.online.factory.factoryonline.utils.StatusBarUtils;
import com.online.factory.factoryonline.utils.ToastUtil;
import com.trello.rxlifecycle.LifecycleTransformer;

import javax.inject.Inject;

/**
 * 作者: GIndoc
 * 日期: 2017/1/18 16:57
 * 作用:
 */

public class OrderActivity extends BaseActivity<OrderContract.View, OrderPresenter> implements OrderContract.View, View.OnTouchListener, TitleBar.OnTitleBarClickListener {
    private ActivityOrderBinding mBinding;

    @Inject
    OrderPresenter mPresenter;

    @Inject
    LoginContext loginContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getComponent().inject(this);
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_order);
        mBinding.setView(this);

        StatusBarUtils.from(this)
                .setLightStatusBar(true)
                .setTransparentStatusbar(true)
                .setActionbarView(mBinding.rlTitle)
                .process();
        mBinding.rlTitle.setOnTitleBarClickListener(this);

        mBinding.radioGroup.getChildAt(0).setOnTouchListener(this);
        mBinding.radioGroup.getChildAt(1).setOnTouchListener(this);
        mBinding.radioGroup.getChildAt(2).setOnTouchListener(this);
        mBinding.etInputTime.setOnTouchListener(this);
    }

    @Override
    protected OrderPresenter createPresent() {
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

    @Override
    public void submitSuccessful() {
        finish();
    }

    @Override
    public void unLogin() {
        new AlertDialog.Builder(this)
                .setMessage(R.string.unLogin)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        loginContext.openUserDetail(OrderActivity.this);
                    }
                }).create().show();
    }

    @Override
    public void onLeftButtonClickListener(View view) {
        finish();
    }

    @Override
    public void onRightButtonClickListener(View view) {
        String description = mBinding.etDescription.getText().toString();
        int checkedId = mBinding.radioGroup.getCheckedRadioButtonId();
        if (TextUtils.isEmpty(description.trim())) {
            ToastUtil.show(this, "请描述您需要的厂房内容/详情..(2~400字)");
            return;
        }else if (description.length() < 2 || description.length() > 400){
            ToastUtil.show(this, "字数不符合要求(2~400字)");
            return;
        } else if (checkedId == -1 && TextUtils.isEmpty(mBinding.etInputTime.getText().toString().trim())) {
            ToastUtil.show(this, "请选择匹配时间");
            return;
        }
        if (checkedId != -1) {
            RadioButton radioButton = (RadioButton) mBinding.radioGroup.findViewById(checkedId);
            String c = radioButton.getText().toString();
            c = c.substring(0, c.length() - 1);
            mPresenter.publishNeededMessages(description, c);
        }else {
            mPresenter.publishNeededMessages(description, mBinding.etInputTime.getText().toString());
        }
    }
}
