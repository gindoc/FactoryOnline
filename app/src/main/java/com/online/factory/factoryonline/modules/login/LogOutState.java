package com.online.factory.factoryonline.modules.login;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.modules.main.MainActivity;
import com.online.factory.factoryonline.modules.setting.SettingActivity;

/**
 * Created by louiszgm on 2016/9/30.
 */

public class LogOutState implements UserState {

//    @Override
//    public void openMsg(Context context) {
//        toLoginActivity(context);
//    }

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

    @Override
    public void openHistory(Context context) {
        Toast.makeText(context, "尚未登录", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void openTransparentPublish(MainActivity context) {
        Toast.makeText(context, "尚未登录，无法发布信息~~", Toast.LENGTH_SHORT).show();
    }

    public void toLoginActivity(Context context) {
        Activity activity = ((Activity) context);
//        Toast.makeText(context, "尚未登录，请先登录", Toast.LENGTH_SHORT).show();
        activity.startActivity(LoginActivity.getStartIntent(context));
        activity.overridePendingTransition(R.anim.translate_vetical_bottom_in, R.anim.no_animation);
    }


}
