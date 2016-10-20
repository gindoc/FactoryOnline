package com.online.factory.factoryonline.modules.album.fragment.PhotoFolder;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.online.factory.factoryonline.base.BaseFragment;
import com.online.factory.factoryonline.customview.CustomDialog;
import com.online.factory.factoryonline.databinding.FragmentPhotoFolderBinding;
import com.online.factory.factoryonline.models.ImageFolderBean;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by cwenhui on 2016/10/20.
 */
public class PhotoFolderFragment extends BaseFragment<PhotoFolderContract.View, PhotoFolderPresenter> implements PhotoFolderContract.View {
    private FragmentPhotoFolderBinding mBinding;
    public static final int FROM_PHOTOWALL_FRAGMENT = 100;

    @Inject
    PhotoFolderPresenter mPresenter;

    @Inject
    PhotoFolderAdapter mAdapter;

    @Inject
    public PhotoFolderFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        getComponent().inject(this);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentPhotoFolderBinding.inflate(inflater);

        mBinding.recyclerView.setAdapter(mAdapter);

        mPresenter.getPhotoFolders();

        return mBinding.getRoot();
    }

    @Override
    protected PhotoFolderPresenter createPresent() {
        return mPresenter;
    }

    @Override
    public <T> LifecycleTransformer<T> getBindToLifecycle() {
        return bindToLifecycle();
    }

    @Override
    public void showError(String error) {

    }

    @Override
    public void showLoadingDialog() {
        CustomDialog.createLoadingDialog(getContext()).show();
    }

    @Override
    public void hideLoadingDialog() {
        CustomDialog.dismissDialog();
    }

    @Override
    public void initRecyclerview(List<ImageFolderBean> beanList) {
        mAdapter.setData(beanList);
        mBinding.recyclerView.notifyDataSetChanged();
    }
}
