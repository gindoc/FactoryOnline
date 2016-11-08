package com.online.factory.factoryonline.modules.album.fragment.PhotoSelected;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.base.BaseFragment;
import com.online.factory.factoryonline.databinding.FragmentPhotoSelectedBinding;
import com.online.factory.factoryonline.databinding.ItemPhotoSelectedFooterBinding;
import com.online.factory.factoryonline.modules.album.AlbumActivity;
import com.online.factory.factoryonline.modules.album.fragment.PhotoWall.PhotoWallFragment;
import com.online.factory.factoryonline.modules.publishRental.PublishRentalActivity;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by cwenhui on 2016.02.23
 */
public class PhotoSelectedFragment extends BaseFragment<PhotoSelectedContract.View, PhotoSelectedPresenter> implements PhotoSelectedContract.View {
    public static final int FROM_PHOTOWALL_FRAGMENT = 101;
    public static final String SELECTED_PHOTO = "selectedPhoto";

    @Inject
    PhotoSelectedPresenter mPresenter;

    private FragmentPhotoSelectedBinding mBinding;
    private ItemPhotoSelectedFooterBinding mFooterBinding;

    @Inject
    PhotoSelectedAdapter mAdapter;

    private List<String> mSelectedPhotoPath = new ArrayList<>();

    @Inject
    public PhotoSelectedFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        getComponent().inject(this);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        mBinding = FragmentPhotoSelectedBinding.inflate(inflater);
        mFooterBinding = ItemPhotoSelectedFooterBinding.inflate(inflater);
        mBinding.setView(this);
        mFooterBinding.setView(this);

        mSelectedPhotoPath.clear();
        mSelectedPhotoPath.addAll(getArguments().getStringArrayList(SELECTED_PHOTO));

        mBinding.tvTitle.setText("已选"+mSelectedPhotoPath.size()+"张图片");
        initRecyclerView();
        initToolbar();

        return mBinding.getRoot();
    }

    private void initToolbar() {
        mBinding.toolbar.setTitle("");
        ((AlbumActivity) getActivity()).setSupportActionBar(mBinding.toolbar);
        mBinding.toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
        mBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popFragmentWithResult();
            }
        });
    }

    private void initRecyclerView() {
        mAdapter.setData(mSelectedPhotoPath);
        mBinding.recyclerView.setAdapter(mAdapter);
        mBinding.recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        mBinding.recyclerView.addFooter(mFooterBinding.getRoot());
    }

    public void addPhoto() {
        popFragmentWithResult();
    }

    private void popFragmentWithResult() {
        Bundle bundle = new Bundle();
        mSelectedPhotoPath.clear();
        mSelectedPhotoPath.addAll(mAdapter.getData());
        bundle.putStringArrayList(SELECTED_PHOTO, (ArrayList<String>) mSelectedPhotoPath);
        setFramgentResult(PhotoWallFragment.TO_PHOTOSELECTED_FRAGMENT, bundle);
        pop();
    }

    public void finish() {
        Intent intent = new Intent();
        mSelectedPhotoPath.clear();
        mSelectedPhotoPath.addAll(mAdapter.getData());
        intent.putStringArrayListExtra(SELECTED_PHOTO, (ArrayList<String>) mSelectedPhotoPath);
        getActivity().setResult(PublishRentalActivity.ALBUM_ACTIVITY_RESULT_OK, intent);
        getActivity().finish();
    }

    @Override
    protected PhotoSelectedPresenter createPresent() {
        return mPresenter;
    }

    @Override
    public <T> LifecycleTransformer<T> getBindToLifecycle() {
        return bindToLifecycle();
    }

    @Override
    public void showError(String error) {
    }

}
