package com.online.factory.factoryonline.modules.publishRental;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.base.BaseActivity;
import com.online.factory.factoryonline.databinding.ActivityPublishRentalBinding;
import com.online.factory.factoryonline.modules.album.AlbumActivity;
import com.online.factory.factoryonline.modules.album.fragment.PhotoSelected.PhotoSelectedFragment;
import com.squareup.picasso.Picasso;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by cwenhui on 2016/10/19.
 */
public class PublishRentalActivity extends BaseActivity<PublishRentalContract.View, PublishRentalPresenter> implements PublishRentalContract.View {
    public static final String REQUEST_CODE = "requestCode";
    public static final int TO_PHOTO_WALL = 68;
    public static final int TO_PHOTO_SELECTED = 69;
    private ActivityPublishRentalBinding mBinding;
    public static final int TO_PHOTO_WALL_PICK_IMAGE = 78;
    public static final int ALBUM_ACTIVITY_RESULT_OK = 79;

    @Inject
    PublishRentalPresenter mPresenter;
    private List<String> mSelectedImage = new ArrayList<>();

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
        startActivityForResult(intent, TO_PHOTO_WALL_PICK_IMAGE);
    }

    public void pickMore(){
        Intent intent = new Intent(this, AlbumActivity.class);
        intent.putStringArrayListExtra(PhotoSelectedFragment.SELECTED_PHOTO, (ArrayList<String>)
                mSelectedImage);
        intent.putExtra(REQUEST_CODE, TO_PHOTO_WALL);
        startActivityForResult(intent, TO_PHOTO_WALL_PICK_IMAGE);
    }

    public void viewSelectedImage(){
        Intent intent = new Intent(this, AlbumActivity.class);
        intent.putStringArrayListExtra(PhotoSelectedFragment.SELECTED_PHOTO, (ArrayList<String>)
                mSelectedImage);
        intent.putExtra(REQUEST_CODE, TO_PHOTO_SELECTED);
        startActivityForResult(intent, TO_PHOTO_WALL_PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TO_PHOTO_WALL_PICK_IMAGE && resultCode == ALBUM_ACTIVITY_RESULT_OK && data != null) {
            mSelectedImage.clear();
            mSelectedImage.addAll(data.getStringArrayListExtra(PhotoSelectedFragment.SELECTED_PHOTO));
            if (mSelectedImage.size() > 0) {
                File file = new File(mSelectedImage.get(0));
                Picasso.with(this).load(file)
                        .placeholder(R.drawable.demo)
                        .resize(mBinding.ivSelectedImg.getMeasuredWidth(), mBinding.ivSelectedImg.getMeasuredHeight())
                        .config(Bitmap.Config.RGB_565)
                        .into(mBinding.ivSelectedImg);
                mBinding.ivPickImg.setVisibility(View.GONE);
                mBinding.tvPickImg.setVisibility(View.GONE);
                mBinding.ivPicMore.setVisibility(View.VISIBLE);
            }
        }
    }
}
