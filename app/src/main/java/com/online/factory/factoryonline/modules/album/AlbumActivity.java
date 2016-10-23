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
        List<String> selectedImage = getIntent().getStringArrayListExtra(PhotoSelectedFragment
                .SELECTED_PHOTO);
        if (selectedImage != null) {
            Bundle bundle = new Bundle();
            bundle.putStringArrayList(PhotoSelectedFragment.SELECTED_PHOTO, (ArrayList<String>)
                    selectedImage);
            photoWallFragment.setArguments(bundle);
        }
        loadRootFragment(R.id.rl_container, photoWallFragment);

    }

    @Override
    protected BasePresenter createPresent() {
        return null;
    }

    public void switchPhotoFolder() {
//        startWithPop(photoFolderFragment);
//        startForResult(photoFolderFragment, PhotoFolderFragment.TO_PHOTOFOLDER_FRAGMENT);
    }

    public void switchPhotoWall(){

    }

}
