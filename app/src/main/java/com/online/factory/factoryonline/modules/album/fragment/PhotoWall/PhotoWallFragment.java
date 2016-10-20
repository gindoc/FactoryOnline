package com.online.factory.factoryonline.modules.album.fragment.PhotoWall;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.base.BaseFragment;
import com.online.factory.factoryonline.customview.CustomDialog;
import com.online.factory.factoryonline.databinding.FragmentPhotoWallBinding;
import com.online.factory.factoryonline.modules.album.AlbumActivity;
import com.online.factory.factoryonline.modules.album.fragment.photowall.PhotoWallPresenter;
import com.trello.rxlifecycle.LifecycleTransformer;

import javax.inject.Inject;

/**
 * Created by cwenhui on 2016/10/19.
 */
public class PhotoWallFragment extends BaseFragment<PhotoWallContract.View, PhotoWallPresenter> implements PhotoWallContract.View {
    private FragmentPhotoWallBinding mBinding;

    @Inject
    PhotoWallPresenter mPresenter;

    @Inject
    public PhotoWallFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        getComponent().inject(this);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentPhotoWallBinding.inflate(inflater);

        initToolBar();
        mPresenter.getPhotos();

        return mBinding.getRoot();
    }

    private void initToolBar() {
        mBinding.toolbar.setTitle("");
        ((AlbumActivity) getActivity()).setSupportActionBar(mBinding.toolbar);
        mBinding.toolbar.setNavigationIcon(R.drawable.ic_close);
    }

    @Override
    protected com.online.factory.factoryonline.modules.album.fragment.photowall.PhotoWallPresenter createPresent() {
        return mPresenter;
    }

    @Override
    public <T> LifecycleTransformer<T> getBindToLifecycle() {
        return bindToLifecycle();
    }

    @Override
    public void showError(String error) {}

    @Override
    public void showLoadingDialog() {
        CustomDialog.createLoadingDialog(getContext()).show();
    }

    @Override
    public void hideLoadingDialog() {
        CustomDialog.dismissDialog();
    }
}
