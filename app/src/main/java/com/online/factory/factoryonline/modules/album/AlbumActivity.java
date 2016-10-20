package com.online.factory.factoryonline.modules.album;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.base.BaseActivity;
import com.online.factory.factoryonline.base.BasePresenter;
import com.online.factory.factoryonline.databinding.ActivityAlbumBinding;
import com.online.factory.factoryonline.modules.album.fragment.PhotoWall.PhotoWallFragment;

/**
 * Created by cwenhui on 2016/10/19.
 */
public class AlbumActivity extends BaseActivity {
    private ActivityAlbumBinding mBinding;

//    @Inject
    PhotoWallFragment photoWallFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_album);
        photoWallFragment = new PhotoWallFragment();
        loadRootFragment(R.id.rl_container, photoWallFragment);
    }

    @Override
    protected BasePresenter createPresent() {
        return null;
    }

}
