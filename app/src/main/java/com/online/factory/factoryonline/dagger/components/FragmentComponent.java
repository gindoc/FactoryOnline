package com.online.factory.factoryonline.dagger.components;


import com.online.factory.factoryonline.dagger.modules.FragmentModule;
import com.online.factory.factoryonline.modules.album.fragment.PhotoFolder.PhotoFolderFragment;
import com.online.factory.factoryonline.modules.album.fragment.PhotoWall.PhotoWallFragment;
import com.online.factory.factoryonline.modules.main.fragments.home.HomeFragment;
import com.online.factory.factoryonline.modules.main.fragments.recommend.RecommendFragment;
import com.online.factory.factoryonline.modules.main.fragments.user.UserFragment;

import dagger.Subcomponent;

/**
 * Created by louiszgm-pc on 2016/9/21.
 */
@Subcomponent(modules = FragmentModule.class)
public interface FragmentComponent {
    void inject(UserFragment userFragment);

    void inject(HomeFragment homeFragment);

    void inject(RecommendFragment recommendFragment);

    void inject(PhotoWallFragment photoWallFragment);

    void inject(PhotoFolderFragment photoFolderFragment);
}
