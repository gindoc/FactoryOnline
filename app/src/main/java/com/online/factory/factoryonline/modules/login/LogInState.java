package com.online.factory.factoryonline.modules.login;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.data.remote.Consts;
import com.online.factory.factoryonline.modules.browseHistory.BrowseHistoryActivity;
import com.online.factory.factoryonline.modules.collection.CollectionActivity;
import com.online.factory.factoryonline.modules.main.MainActivity;
import com.online.factory.factoryonline.modules.orderRecord.OrderRecordActivity;
import com.online.factory.factoryonline.modules.personalInfo.PersonalInfoActivity;
import com.online.factory.factoryonline.modules.publication.PublicationActivity;
import com.online.factory.factoryonline.modules.setting.SettingActivity;
import com.online.factory.factoryonline.modules.translucent.publish.TranslucentPublishActivity;
import com.online.factory.factoryonline.utils.BitmapManager;
import com.online.factory.factoryonline.utils.FastBlurUtil;

/**
 * Created by louiszgm on 2016/9/30.
 */

public class LogInState implements UserState {

    @Override
    public void openUserDetail(Context context) {
        context.startActivity(PersonalInfoActivity.getStartIntent(context));
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

    @Override
    public void openTransparentPublish(MainActivity context) {
        Bitmap blurBackground = BitmapManager.screenShot(context);
        blurBackground = Bitmap.createScaledBitmap(blurBackground, blurBackground.getWidth() / 10, blurBackground.getHeight() / 10, false);
        blurBackground = FastBlurUtil.doBlur(blurBackground, 8, true);
        Intent intent = TranslucentPublishActivity.getStartIntent(context, blurBackground);
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.no_anim, R.anim.no_animation);
    }

    @Override
    public void openRecord(Context context, int type) {
        if (type == Consts.TYPE_USER) {
            context.startActivity(OrderRecordActivity.getStartIntent(context));
        }else {
            context.startActivity(PublicationActivity.getStartIntent(context));
        }
    }
}
