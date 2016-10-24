package com.online.factory.factoryonline.modules.login;

import android.content.Context;

/**
 * Created by louiszgm on 2016/9/30.
 */

public interface UserState {

    void openMsg(Context context);

    /**
     * 注册成功后自动登录跳转
     * @param context
     */
    void regist(Context context);
}
