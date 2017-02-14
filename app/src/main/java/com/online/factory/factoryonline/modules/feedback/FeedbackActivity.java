package com.online.factory.factoryonline.modules.feedback;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.widget.Toast;

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.base.BaseActivity;
import com.online.factory.factoryonline.databinding.ActivityFeedbackBinding;
import com.trello.rxlifecycle.LifecycleTransformer;

import javax.inject.Inject;

/**
 * 作者: GIndoc
 * 日期: 2017/2/14 10:24
 * 作用:
 */

public class FeedbackActivity extends BaseActivity<FeedbackContract.View, FeedbackPresenter> implements FeedbackContract.View {

    @Inject
    FeedbackPresenter mPresenter;

    private ActivityFeedbackBinding mBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getComponent().inject(this);
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_feedback);
        mBinding.setView(this);
    }

    @Override
    protected FeedbackPresenter createPresent() {
        return mPresenter;
    }

    @Override
    public <T> LifecycleTransformer<T> getBindToLifecycle() {
        return bindToLifecycle();
    }

    @Override
    public void showError(String error) {

    }

    public void submit() {
        String description = mBinding.etDescription.getText().toString().trim();
        if (TextUtils.isEmpty(description)) {
            Toast.makeText(this, "请描述您反馈的问题或者建议..(2~400字)", Toast.LENGTH_SHORT).show();
            return;
        }else if (description.length() < 2 || description.length() > 400){
            Toast.makeText(this, "字数不符合要求(2~400字)",Toast.LENGTH_SHORT).show();
            return;
        }
        String phoneNum = mBinding.etPhonenum.getText().toString().trim();
        if (TextUtils.isEmpty(phoneNum)) {
            Toast.makeText(this, "请留下您的留下方式(QQ号码/手机号/邮箱)", Toast.LENGTH_SHORT).show();
            return;
        }
        mPresenter.submitFeedback(description, phoneNum);
    }

    @Override
    public void submitSuccessful() {
        finish();
    }

    public static Intent getStartIntent(Context context) {
        return new Intent(context, FeedbackActivity.class);
    }

}
