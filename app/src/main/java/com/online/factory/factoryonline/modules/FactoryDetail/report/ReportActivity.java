package com.online.factory.factoryonline.modules.FactoryDetail.report;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.base.BaseActivity;
import com.online.factory.factoryonline.customview.TitleBar;
import com.online.factory.factoryonline.databinding.ActivityReportBinding;
import com.online.factory.factoryonline.utils.StatusBarUtils;
import com.trello.rxlifecycle.LifecycleTransformer;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * 作者: GIndoc
 * 日期: 2016/11/14 14:21
 * 作用: 举报页面
 */
public class ReportActivity extends BaseActivity<ReportContract.View, ReportPresenter> implements ReportContract.View, TitleBar.OnTitleBarClickListener{
    private static final String MESSAGE_ID = "MESSAGE_ID";
    private ActivityReportBinding mBinding;

    @Inject
    ReportPresenter mPresenter;

    public static Intent getStartIntent(Context context, int messageId) {
        Intent intent = new Intent();
        intent.putExtra(MESSAGE_ID, messageId);
        intent.setClass(context, ReportActivity.class);
        return intent;
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getComponent().inject(this);
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_report);
        mBinding.setView(this);
        StatusBarUtils.from(this)
                //白底黑字状态栏
                .setLightStatusBar(true)
                .setTransparentStatusbar(true)
                //设置toolbar,actionbar等view
                .setActionbarView(mBinding.rlTitle)
                .process();
        mBinding.rlTitle.setOnTitleBarClickListener(this);

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

    @Override
    public void feedbackSuccessful() {
        Toast.makeText(this, "举报成功，静候佳音", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onLeftButtonClickListener(View view) {
        finish();
    }

    @Override
    public void onRightButtonClickListener(View view) {
        int checkedId = mBinding.radioGroup.getCheckedRadioButtonId();
        if (checkedId == -1) {
            Toast.makeText(this, "请选择举报内容", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(mBinding.etRemark.getText())) {
            Toast.makeText(this, "既然不满，总得吐槽点什么吧~~~", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mBinding.etRemark.getText().toString().length() > 200) {
            Toast.makeText(this,"最多吐槽200字哦~~", Toast.LENGTH_SHORT).show();
            return;
        }
        RadioButton radioButton = (RadioButton)findViewById(checkedId);
        mPresenter.feedback(getIntent().getIntExtra(MESSAGE_ID, -1), radioButton.getText().toString(), mBinding.etRemark.getText().toString());
    }
}
