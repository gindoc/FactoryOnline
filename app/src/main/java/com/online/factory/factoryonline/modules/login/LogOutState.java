package com.online.factory.factoryonline.modules.login;

import android.content.Context;
import android.content.Intent;

import com.online.factory.factoryonline.modules.main.MainActivity;

/**
 * Created by louiszgm on 2016/9/30.
 */

public class LogOutState implements UserState {

    @Override
    public void openMsg(Context context) {
        context.startActivity(LoginActivity.getStartIntent(context));
    }
}
