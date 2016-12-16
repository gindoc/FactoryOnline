package com.online.factory.factoryonline.dagger.components;


import com.online.factory.factoryonline.dagger.modules.FragmentModule;
import com.online.factory.factoryonline.modules.album.fragment.PhotoFolder.PhotoFolderFragment;
import com.online.factory.factoryonline.modules.album.fragment.PhotoSelected.PhotoSelectedFragment;
import com.online.factory.factoryonline.modules.album.fragment.PhotoWall.PhotoWallFragment;
import com.online.factory.factoryonline.modules.collection.agent.AgentCollectionFragment;
import com.online.factory.factoryonline.modules.collection.owner.OwnerCollectionFragment;
import com.online.factory.factoryonline.modules.main.fragments.home.HomeFragment;
import com.online.factory.factoryonline.modules.main.fragments.recommend.RecommendFragment;
import com.online.factory.factoryonline.modules.main.fragments.user.UserFragment;
import com.online.factory.factoryonline.modules.personalInfo.fragments.modifyName.ModifyNameFragment;
import com.online.factory.factoryonline.modules.personalInfo.fragments.modifyPwd.ModifyPwdFragment;
import com.online.factory.factoryonline.modules.personalInfo.fragments.personalInfo.PersonalInfoFragment;

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

    void inject(PhotoSelectedFragment photoSelectedFragment);

    void inject(ModifyNameFragment modifyNameFragment);

    void inject(PersonalInfoFragment personalInfoFragment);

    void inject(ModifyPwdFragment modifyPwdFragment);

    void inject(AgentCollectionFragment agentCollectionFragment);

    void inject(OwnerCollectionFragment ownerCollectionFragment);
}
