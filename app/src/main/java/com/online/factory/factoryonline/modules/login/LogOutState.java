package com.online.factory.factoryonline.modules.login;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.online.factory.factoryonline.modules.main.MainActivity;
import com.online.factory.factoryonline.modules.setting.SettingActivity;

/**
 * Created by louiszgm on 2016/9/30.
 */

public class LogOutState implements UserState {

    @Override
    public void openMsg(Context context) {
        toLoginActivity(context);
    }

    @Override
    public void openUserDetail(Context context) {
        toLoginActivity(context);
    }

    @Override
    public void openPublish(Context context) {
        toLoginActivity(context);
    }

    @Override
    public void openCollection(Context context) {
        toLoginActivity(context);
    }

    @Override
    public void openSetting(Context context) {
        context.startActivity(SettingActivity.getStartIntent(context));
    }

    public void toLoginActivity(Context context) {
        Toast.makeText(context, "尚未登录，请先登录", Toast.LENGTH_SHORT).show();
        context.startActivity(LoginActivity.getStartIntent(context));
    }


}
