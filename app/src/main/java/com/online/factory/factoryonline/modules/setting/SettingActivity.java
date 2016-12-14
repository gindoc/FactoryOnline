package com.online.factory.factoryonline.modules.setting;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.base.BaseActivity;
import com.online.factory.factoryonline.base.BasePresenter;
import com.online.factory.factoryonline.databinding.ActivitySearchBinding;
import com.online.factory.factoryonline.databinding.ActivitySettingBinding;
import com.online.factory.factoryonline.modules.setting.about.AboutActivity;
import com.online.factory.factoryonline.modules.setting.copyright.CopyrightActivity;
import com.online.factory.factoryonline.modules.setting.qrcode.QRCodeActivity;
import com.online.factory.factoryonline.utils.DataCleanManager;
import com.online.factory.factoryonline.utils.FileUtils;
import com.online.factory.factoryonline.utils.StatusBarUtils;
import com.trello.rxlifecycle.LifecycleTransformer;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import rx.Scheduler;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * 作者: GIndoc
 * 日期: 2016/12/2 16:10
 * 作用:
 */

public class SettingActivity extends BaseActivity<SettingContract.View, SettingPresenter> implements SettingContract.View {

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
                //沉浸状态栏
                .setTransparentStatusbar(true)
                //白底黑字状态栏
                .setLightStatusBar(true)
                //设置toolbar,actionbar等view
                .setActionbarView(mBinding.rlTopBar)
                .process();
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
}
