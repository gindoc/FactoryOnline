package com.online.factory.factoryonline.base;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.widget.Toast;

import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.lang.ref.WeakReference;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * 作者: GIndoc
 * 日期: 2017/1/17 15:03
 * 作用:
 */

public class BaseShareListener implements UMShareListener {

    Context context;

    public BaseShareListener(Context context) {
        this.context = context;
    }

    @Override
    public void onResult(SHARE_MEDIA platform) {
//        Toast.makeText(context, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(SHARE_MEDIA platform, Throwable t) {
//        Toast.makeText(context, platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
        if (t != null) {
            Timber.e("throw:" + t.getMessage());
        }
    }

    @Override
    public void onCancel(SHARE_MEDIA platform) {
//        Toast.makeText(context, platform + " 分享取消啦", Toast.LENGTH_SHORT).show();
    }
}
