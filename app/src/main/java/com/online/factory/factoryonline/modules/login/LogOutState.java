package com.online.factory.factoryonline.modules.login;

import android.app.Activity;
import android.content.Context;

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.modules.main.MainActivity;
import com.online.factory.factoryonline.modules.setting.SettingActivity;
import com.online.factory.factoryonline.utils.ToastUtil;

/**
 * Created by louiszgm on 2016/9/30.
 */

public class LogOutState implements UserState {

    @Override
    public void openUserDetail(Activity context) {
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

    @Override
    public void openHistory(Context context) {
        ToastUtil.show(context, "尚未登录");
    }

    @Override
    public void openTransparentPublish(MainActivity context) {
        ToastUtil.show(context, "尚未登录，无法发布信息~~");
    }

    @Override
    public void openRecord(Context context, int type) {
        ToastUtil.show(context, "尚未登录");
    }

    public void toLoginActivity(Context context) {
        Activity activity = ((Activity) context);
        activity.startActivity(LoginActivity.getStartIntent(context));
        activity.overridePendingTransition(R.anim.translate_vetical_bottom_in, R.anim.no_animation);
    }


}
