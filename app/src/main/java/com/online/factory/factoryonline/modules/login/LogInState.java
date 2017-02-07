package com.online.factory.factoryonline.modules.login;

import android.content.Context;

import com.online.factory.factoryonline.modules.browseHistory.BrowseHistoryActivity;
import com.online.factory.factoryonline.modules.collection.CollectionActivity;
import com.online.factory.factoryonline.modules.personalInfo.PersonalInfoActivity;
import com.online.factory.factoryonline.modules.publication.PublicationActivity;
import com.online.factory.factoryonline.modules.setting.SettingActivity;

/**
 * Created by louiszgm on 2016/9/30.
 */

public class LogInState implements UserState {
//    @Override
//    public void openMsg(Context context) {
//        BaseActivity baseActivity = (BaseActivity) context;
//        if(baseActivity.findFragment(MsgFragment.class) == null){
//            baseActivity.startWithPop(MsgFragment.newInstance());
//        }
//    }

    @Override
    public void openUserDetail(Context context) {
        context.startActivity(PersonalInfoActivity.getStartIntent(context));
    }

    @Override
    public void openPublish(Context context) {
        context.startActivity(PublicationActivity.getStartIntent(context));
    }

    @Override
    public void openCollection(Context context) {
        context.startActivity(CollectionActivity.getStartIntent(context));
    }

    @Override
    public void openSetting(Context context) {
        context.startActivity(SettingActivity.getStartIntent(context));
    }

    @Override
    public void openHistory(Context context) {
        context.startActivity(BrowseHistoryActivity.getStartIntent(context));
    }
}
