package com.online.factory.factoryonline.modules.login;

import android.app.Activity;
import android.content.Context;

import com.online.factory.factoryonline.modules.main.MainActivity;

/**
 * Created by louiszgm on 2016/9/30.
 */

public interface UserState {

    void openUserDetail(Activity context);


    void openCollection(Context context);

    void openSetting(Context context);

    void openHistory(Context context);

    void openTransparentPublish(MainActivity mainActivity);

    void openRecord(Context context, int type);
}
