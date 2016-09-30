package com.online.factory.factoryonline.modules.login;

import android.content.Context;

/**
 * Created by louiszgm on 2016/9/30.
 */

public class LoginContext {
    private UserState mState = new LogOutState();

    public void setmState(UserState mState) {
        this.mState = mState;
    }

    public void openMsg(Context context){
        mState.openMsg(context);
    }
}
