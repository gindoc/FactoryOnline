package com.online.factory.factoryonline.modules.album.fragment.PhotoSelected;

import android.app.Activity;
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
import com.online.factory.factoryonline.utils.StatusBarUtils;
import com.online.factory.factoryonline.utils.rx.RxSubscriber;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Created by cwenhui on 2016.02.23
 */
public class PhotoSelectedFragment extends BaseFragment<PhotoSelectedContract.View, PhotoSelectedPresenter> implements PhotoSelectedContract.View {
    public static final int FROM_PHOTOWALL_FRAGMENT = 101;
    public static final String UPLOADED_PHOTO = "uploadedPhoto";
    public static final String IMAGE_KEYS = "imageKeys";

    @Inject
    PhotoSelectedPresenter mPresenter;

    private FragmentPhotoSelectedBinding mBinding;
    private ItemPhotoSelectedFooterBinding mFooterBinding;

    @Inject
    PhotoSelectedAdapter mAdapter;

    private List<String> mSelectedPhotoPath = new ArrayList<>();
    private List<String> imageKeys = new ArrayList<>();

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

        Bundle bundle = getArguments();
        mSelectedPhotoPath.clear();
        mSelectedPhotoPath.addAll(bundle.getStringArrayList(UPLOADED_PHOTO));
        imageKeys.clear();
        imageKeys.addAll(bundle.getStringArrayList(IMAGE_KEYS));

        mBinding.tvTitle.setText("已选"+mSelectedPhotoPath.size()+"张图片");
        initRecyclerView();
        initToolbar();

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
                finish();
            }
        });
    }

    private void initRecyclerView() {
        mAdapter.setData(mSelectedPhotoPath);
        mBinding.recyclerView.setAdapter(mAdapter);
        mBinding.recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        mBinding.recyclerView.addFooter(mFooterBinding.getRoot());

        mAdapter.getSubject().subscribe(new RxSubscriber() {
            @Override
            public void _onNext(Object o) {
                String imageKey = imageKeys.get((Integer) o);
                mPresenter.deleteImage(imageKey);
            }

            @Override
            public void _onError(Throwable throwable) {
                Timber.e(throwable.getMessage());
            }
        });
    }

    public void addPhoto() {
        popFragmentWithResult();
    }

    private void popFragmentWithResult() {
        Bundle bundle = new Bundle();
        mSelectedPhotoPath.clear();
        mSelectedPhotoPath.addAll(mAdapter.getData());
        bundle.putStringArrayList(UPLOADED_PHOTO, (ArrayList<String>) mSelectedPhotoPath);
        bundle.putStringArrayList(IMAGE_KEYS, new ArrayList<>(imageKeys));
        setFramgentResult(PhotoWallFragment.TO_PHOTOSELECTED_FRAGMENT, bundle);
        pop();
    }

    public void finish() {
        Intent intent = new Intent();
        mSelectedPhotoPath.clear();
        mSelectedPhotoPath.addAll(mAdapter.getData());
        intent.putStringArrayListExtra(UPLOADED_PHOTO, (ArrayList<String>) mSelectedPhotoPath);
        intent.putStringArrayListExtra(IMAGE_KEYS, new ArrayList<>(imageKeys));
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

    @Override
    public void removeUploadedImage(String imageKey) {
        mAdapter.getData().remove(imageKeys.indexOf(imageKey));
        imageKeys.remove(imageKey);
        mBinding.recyclerView.notifyDataSetChanged();
        mBinding.tvTitle.setText("已选" + mAdapter.getData().size() + "张图片");
    }
}
