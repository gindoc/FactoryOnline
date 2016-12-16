package com.online.factory.factoryonline.modules.album;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.base.BaseActivity;
import com.online.factory.factoryonline.base.BasePresenter;
import com.online.factory.factoryonline.databinding.ActivityAlbumBinding;
import com.online.factory.factoryonline.modules.album.fragment.PhotoFolder.PhotoFolderFragment;
import com.online.factory.factoryonline.modules.album.fragment.PhotoSelected.PhotoSelectedFragment;
import com.online.factory.factoryonline.modules.album.fragment.PhotoWall.PhotoWallFragment;
import com.online.factory.factoryonline.modules.publishRental.PublishRentalActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by cwenhui on 2016/10/19.
 */
public class AlbumActivity extends BaseActivity {
    private ActivityAlbumBinding mBinding;

    @Inject
    PhotoWallFragment photoWallFragment;

    @Inject
    PhotoFolderFragment photoFolderFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getComponent().inject(this);
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_album);
        List<String> uploadedImage = getIntent().getStringArrayListExtra(PhotoSelectedFragment
                .UPLOADED_PHOTO);
        List<String> imageKey = getIntent().getStringArrayListExtra(PhotoSelectedFragment.IMAGE_KEYS);
        if (uploadedImage != null) {
            Bundle bundle = new Bundle();
            bundle.putStringArrayList(PhotoSelectedFragment.UPLOADED_PHOTO, (ArrayList<String>)
                    uploadedImage);
            bundle.putStringArrayList(PhotoSelectedFragment.IMAGE_KEYS, (ArrayList<String>) imageKey);
            int requestCode = getIntent().getIntExtra(PublishRentalActivity.REQUEST_CODE,
                    PublishRentalActivity.TO_PHOTO_WALL);
            bundle.putInt(PublishRentalActivity.REQUEST_CODE, requestCode);
            photoWallFragment.setArguments(bundle);
        }
        loadRootFragment(R.id.rl_container, photoWallFragment);

    }

    @Override
    protected BasePresenter createPresent() {
        return null;
    }


}
