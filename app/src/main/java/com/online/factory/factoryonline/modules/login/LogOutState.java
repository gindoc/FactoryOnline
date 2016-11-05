package com.online.factory.factoryonline.modules.login;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.online.factory.factoryonline.modules.main.MainActivity;

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

    public void toLoginActivity(Context context) {
        Toast.makeText(context, "尚未登录，请先登录", Toast.LENGTH_SHORT).show();
        context.startActivity(LoginActivity.getStartIntent(context));
    }


}
