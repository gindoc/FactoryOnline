package com.online.factory.factoryonline.modules.publishRental;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.base.BaseActivity;
import com.online.factory.factoryonline.databinding.ActivityPublishRentalBinding;
import com.online.factory.factoryonline.modules.album.AlbumActivity;
import com.trello.rxlifecycle.LifecycleTransformer;

import javax.inject.Inject;

/**
 * Created by cwenhui on 2016/10/19.
 */
public class PublishRentalActivity extends BaseActivity<PublishRentalContract.View, PublishRentalPresenter> implements PublishRentalContract.View {
    private ActivityPublishRentalBinding mBinding;

    @Inject
    PublishRentalPresenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_publish_rental);

        mBinding.setView(this);

        initToolBar();
    }

    private void initToolBar() {
        mBinding.toolbar.setTitle("");
        setSupportActionBar(mBinding.toolbar);
        mBinding.toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
    }

    @Override
    protected PublishRentalPresenter createPresent() {
        return mPresenter;
    }

    @Override
    public <T> LifecycleTransformer<T> getBindToLifecycle() {
        return bindToLifecycle();
    }

    @Override
    public void showError(String error) {
    }

    public void toAlbumActivity() {
        Intent intent = new Intent(this, AlbumActivity.class);
        startActivity(intent);
    }
}
