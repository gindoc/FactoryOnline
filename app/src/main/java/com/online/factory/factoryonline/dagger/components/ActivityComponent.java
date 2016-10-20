package com.online.factory.factoryonline.dagger.components;



import com.online.factory.factoryonline.modules.FactoryDetail.FactoryDetailActivity;
import com.online.factory.factoryonline.modules.album.AlbumActivity;
import com.online.factory.factoryonline.modules.login.LoginActivity;
import com.online.factory.factoryonline.modules.main.MainActivity;
import com.online.factory.factoryonline.dagger.modules.ActivityModule;
import com.online.factory.factoryonline.dagger.modules.FragmentModule;

import dagger.Subcomponent;

/**
 * Created by louiszgm-pc on 2016/9/21.
 */
@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(MainActivity activity);

    FragmentComponent plus(FragmentModule module);

    void inject(LoginActivity activity);

    void inject(FactoryDetailActivity factoryDetailActivity);

    void inject(AlbumActivity albumActivity);
}
