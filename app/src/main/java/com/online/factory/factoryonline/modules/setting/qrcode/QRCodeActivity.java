package com.online.factory.factoryonline.modules.setting.qrcode;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.base.BaseActivity;
import com.online.factory.factoryonline.base.BasePresenter;
import com.online.factory.factoryonline.databinding.ActivityQrcodeBinding;
import com.online.factory.factoryonline.modules.setting.SettingActivity;
import com.squareup.picasso.Picasso;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.io.File;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * 作者: GIndoc
 * 日期: 2016/12/3 10:18
 * 作用:
 */

public class QRCodeActivity extends BaseActivity<QRCodeContract.View, QRCodePresenter> implements QRCodeContract.View {
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

        int qrcodeDimen = resources.getDimensionPixelSize(R.dimen.x150);
        mPresenter.createQRCode(qrcodeDimen,qrcodeDimen);
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
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loadQRCode() {
//        int size = mBinding.ivQrcode.getWidth();
        Picasso.with(this).load(new File(QRCodeContract.View.QRCODE_PATH))
//                .resize(size, size)
                .into(mBinding.ivQrcode);
    }

    public static Intent getStartIntent(Context context) {
        return new Intent(context, QRCodeActivity.class);
    }
}
