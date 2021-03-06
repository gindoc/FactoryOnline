package com.online.factory.factoryonline.modules.setting.qrcode;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.base.BaseActivity;
import com.online.factory.factoryonline.customview.TitleBar;
import com.online.factory.factoryonline.databinding.ActivityQrcodeBinding;
import com.online.factory.factoryonline.utils.StatusBarUtils;
import com.online.factory.factoryonline.utils.ToastUtil;
import com.online.factory.factoryonline.utils.WindowUtil;
import com.squareup.picasso.Picasso;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.io.File;

import javax.inject.Inject;

/**
 * 作者: GIndoc
 * 日期: 2016/12/3 10:18
 * 作用:
 */

public class QRCodeActivity extends BaseActivity<QRCodeContract.View, QRCodePresenter> implements QRCodeContract.View, TitleBar.OnTitleBarClickListener {
    private ActivityQrcodeBinding mBinding;

    @Inject
    QRCodePresenter mPresenter;

    @Inject
    Resources resources;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getComponent().inject(this);
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_qrcode);
        mBinding.setView(this);
        StatusBarUtils.from(this)
                //白底黑字状态栏
                .setLightStatusBar(true)
                //设置toolbar,actionbar等view
                .setTransparentStatusbar(true)
                .setActionbarView(mBinding.rlTitle)
                .process();
        mBinding.rlTitle.setOnTitleBarClickListener(this);
        int qrcodeDimen = resources.getDimensionPixelSize(R.dimen.x150);
        PackageInfo packageInfo = WindowUtil.getPackageInfo(this);
        if (packageInfo != null) {
            mPresenter.createQRCode(packageInfo.versionName, qrcodeDimen,qrcodeDimen);
        }else {
            ToastUtil.show(this, "无法获取包信息，无法获得下载地址");
        }
    }

    @Override
    protected QRCodePresenter createPresent() {
        return mPresenter;
    }

    @Override
    public <T> LifecycleTransformer<T> getBindToLifecycle() {
        return bindToLifecycle();
    }

    @Override
    public void showError(String error) {
        ToastUtil.show(this, error);
    }

    @Override
    public void loadQRCode() {
        Picasso.with(this).load(new File(QRCodeContract.View.QRCODE_PATH))
                .into(mBinding.ivQrcode);
    }

    public static Intent getStartIntent(Context context) {
        return new Intent(context, QRCodeActivity.class);
    }

    @Override
    public void onLeftButtonClickListener(View view) {
        finish();
    }

    @Override
    public void onRightButtonClickListener(View view) {

    }
}
