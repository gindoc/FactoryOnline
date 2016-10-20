package com.online.factory.factoryonline.modules.album.fragment.PhotoWall;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.base.BaseFragment;
import com.online.factory.factoryonline.customview.CustomDialog;
import com.online.factory.factoryonline.customview.recyclerview.BaseRecyclerViewAdapter;
import com.online.factory.factoryonline.databinding.FragmentPhotoWallBinding;
import com.online.factory.factoryonline.modules.album.AlbumActivity;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.io.File;
import java.util.Arrays;

import javax.inject.Inject;

/**
 * Created by cwenhui on 2016/10/19.
 */
public class PhotoWallFragment extends BaseFragment<PhotoWallContract.View, PhotoWallPresenter> implements PhotoWallContract.View, BaseRecyclerViewAdapter.OnItemClickListener {
    private FragmentPhotoWallBinding mBinding;

    @Inject
    PhotoWallPresenter mPresenter;

    @Inject
    PhotoWallAdapter mAdapter;

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

        mBinding.recyclerView.setAdapter(mAdapter);
        mBinding.recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
        mBinding.recyclerView.addHeader(LayoutInflater.from(getContext()).inflate(R.layout.item_photowall_take_pic, null));
        mAdapter.setOnItemClickListener(this);

        mPresenter.getPhotos();

        return mBinding.getRoot();
    }

    private void initToolBar() {
        mBinding.toolbar.setTitle("");
        ((AlbumActivity) getActivity()).setSupportActionBar(mBinding.toolbar);
        mBinding.toolbar.setNavigationIcon(R.drawable.ic_close);
    }

    @Override
    protected PhotoWallPresenter createPresent() {
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
    public void initRecyclerview(File maxImgDir, int imgCount) {
        if (maxImgDir == null) {
            Toast.makeText(getContext(), "擦，一张图片没扫描到", Toast.LENGTH_SHORT).show();
            return;
        }
        mAdapter.setData(Arrays.asList(maxImgDir.list()));
        mAdapter.setmDirPath(maxImgDir.getAbsolutePath());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(View view, int position) {
        mAdapter.getSubject().onNext(position);
    }
}
