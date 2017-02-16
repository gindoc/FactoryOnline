package com.online.factory.factoryonline.modules.setting;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.base.BaseActivity;
import com.online.factory.factoryonline.customview.TitleBar;
import com.online.factory.factoryonline.databinding.ActivitySettingBinding;
import com.online.factory.factoryonline.modules.setting.about.AboutActivity;
import com.online.factory.factoryonline.modules.setting.copyright.CopyrightActivity;
import com.online.factory.factoryonline.modules.setting.qrcode.QRCodeActivity;
import com.online.factory.factoryonline.utils.StatusBarUtils;
import com.trello.rxlifecycle.LifecycleTransformer;

import javax.inject.Inject;

/**
 * 作者: GIndoc
 * 日期: 2016/12/2 16:10
 * 作用:
 */

public class SettingActivity extends BaseActivity<SettingContract.View, SettingPresenter> implements SettingContract.View, TitleBar.OnTitleBarClickListener {

    @Inject
    SettingPresenter mPresenter;

    private ActivitySettingBinding mBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getComponent().inject(this);
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_setting);
        mBinding.setView(this);
        mBinding.setPresenter(mPresenter);
        StatusBarUtils.from(this)
                //白底黑字状态栏
                .setLightStatusBar(true)
                .setTransparentStatusbar(true)
                //设置toolbar,actionbar等view
                .setActionbarView(mBinding.rlTitle)
                .process();
        mBinding.rlTitle.setOnTitleBarClickListener(this);
        mPresenter.caculateCacheSize();
    }

    @Override
    protected SettingPresenter createPresent() {
        return mPresenter;
    }

    public static Intent getStartIntent(Context context) {
        return new Intent(context, SettingActivity.class);
    }

    @Override
    public <T> LifecycleTransformer<T> getBindToLifecycle() {
        return bindToLifecycle();
    }

    @Override
    public void showError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loadCacheSize(String cacheSize) {
        mBinding.tvCacheSize.setText("当前缓存大小为" + cacheSize);
    }

    public void toAboutPage() {
        startActivity(AboutActivity.getStartIntent(this));
    }

    public void toCopyrightPage() {
        startActivity(CopyrightActivity.getStartIntent(this));
    }

    public void toQRCodePage() {
        startActivity(QRCodeActivity.getStartIntent(this));
    }

    @Override
    public void onLeftButtonClickListener(View view) {
        finish();
    }

    @Override
    public void onRightButtonClickListener(View view) {

    }
}
