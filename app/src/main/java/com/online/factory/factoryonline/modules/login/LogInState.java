package com.online.factory.factoryonline.modules.login;

import android.content.Context;
import android.content.Intent;

import com.online.factory.factoryonline.base.BaseActivity;
import com.online.factory.factoryonline.modules.main.MainActivity;
import com.online.factory.factoryonline.modules.main.fragments.msg.MsgFragment;
import com.online.factory.factoryonline.modules.publication.PublicationActivity;

/**
 * Created by louiszgm on 2016/9/30.
 */

public class LogInState implements UserState {
    @Override
    public void openMsg(Context context) {
        BaseActivity baseActivity = (BaseActivity) context;
        if(baseActivity.findFragment(MsgFragment.class) == null){
            baseActivity.startWithPop(MsgFragment.newInstance());
        }
    }

    @Override
    public void openUserDetail(Context context) {

    }

    @Override
    public void openPublish(Context context) {
        context.startActivity(PublicationActivity.getStartIntent(context));
    }

    @Override
    public void openCollection(Context context) {

    }
}
