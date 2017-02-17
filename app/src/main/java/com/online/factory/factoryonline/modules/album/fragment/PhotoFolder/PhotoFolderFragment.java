package com.online.factory.factoryonline.modules.album.fragment.PhotoFolder;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.base.BaseFragment;
import com.online.factory.factoryonline.customview.CustomDialog;
import com.online.factory.factoryonline.customview.recyclerview.BaseRecyclerViewAdapter;
import com.online.factory.factoryonline.databinding.FragmentPhotoFolderBinding;
import com.online.factory.factoryonline.models.ImageFolderBean;
import com.online.factory.factoryonline.modules.album.AlbumActivity;
import com.online.factory.factoryonline.modules.album.fragment.PhotoWall.PhotoWallFragment;
import com.online.factory.factoryonline.utils.StatusBarUtils;
import com.online.factory.factoryonline.utils.ToastUtil;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by cwenhui on 2016/10/20.
 */
public class PhotoFolderFragment extends BaseFragment<PhotoFolderContract.View, PhotoFolderPresenter> implements PhotoFolderContract.View, BaseRecyclerViewAdapter.OnItemClickListener {
    private FragmentPhotoFolderBinding mBinding;
    public static final int FROM_PHOTOWALL_FRAGMENT = 100;

    private List<ImageFolderBean> mFolderBeens = new ArrayList<>();

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
        mAdapter.setOnItemClickListener(this);

        initToolbar();

        loadImageFolders();

        return mBinding.getRoot();
    }

    private void initToolbar() {
        StatusBarUtils.from((Activity) getContext())
                //沉浸状态栏
                .setTransparentStatusbar(true)
                //白底黑字状态栏
                .setLightStatusBar(true)
                //设置toolbar,actionbar等view
                .setActionbarView(mBinding.toolbar)
                .process();
        mBinding.toolbar.setTitle("");
        ((AlbumActivity) getActivity()).setSupportActionBar(mBinding.toolbar);
        mBinding.toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
        mBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop();
            }
        });
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

    public void setmFolderBeens(List<ImageFolderBean> mFolderBeens) {
        this.mFolderBeens.clear();
        this.mFolderBeens.addAll(mFolderBeens);
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
    public void loadImageFolders() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            ToastUtil.show(getContext(), "暂无外部存储");
            return;
        }
        mAdapter.setData(mFolderBeens);
        mBinding.recyclerView.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(View view, int position) {
        Bundle bundle = new Bundle();
        bundle.putInt(PhotoWallFragment.SELECTED_FOLDER_INDEX, position);
        setFramgentResult(PhotoWallFragment.TO_PHOTOFOLDER_FRAGMENT, bundle);
        pop();
    }
}
