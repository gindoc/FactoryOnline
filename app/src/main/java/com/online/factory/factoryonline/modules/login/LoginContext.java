package com.online.factory.factoryonline.modules.login;

import android.content.Context;

import com.online.factory.factoryonline.utils.Saver;

/**
 * Created by louiszgm on 2016/9/30.
 */

public class LoginContext {

    public LoginContext() {
        if (Saver.getLoginState()) {
            mState = new LogInState();
        }else {
            mState = new LogOutState();
        }
    }

    private UserState mState = new LogOutState();

    public void setmState(UserState mState) {
        this.mState = mState;
    }

    public void openMsg(Context context) {
        mState.openMsg(context);
    }

    public void openUserDetail(Context context) {
        mState.openUserDetail(context);
    }
}
