package com.online.factory.factoryonline.modules.login;

import android.content.Context;

import com.online.factory.factoryonline.modules.main.MainActivity;

/**
 * Created by louiszgm on 2016/9/30.
 */

public interface UserState {

//    void openMsg(Context context);

    void openUserDetail(Context context);

    void openPublish(Context context);

    void openCollection(Context context);

    void openSetting(Context context);

    void openHistory(Context context);

    void openTransparentPublish(MainActivity mainActivity);
}
