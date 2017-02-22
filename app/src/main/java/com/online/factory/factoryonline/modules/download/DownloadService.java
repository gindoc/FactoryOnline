package com.online.factory.factoryonline.modules.download;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.data.DataManager;
import com.online.factory.factoryonline.modules.splash.SplashActivity;
import com.online.factory.factoryonline.utils.ComponentHolder;
import com.online.factory.factoryonline.utils.FileUtils;
import com.online.factory.factoryonline.utils.rx.RxSubscriber;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.inject.Inject;
import javax.inject.Named;

import okhttp3.ResponseBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;
import timber.log.Timber;

/**
 * 作者: GIndoc
 * 日期: 2017/2/22 11:31
 * 作用:
 */

public class DownloadService extends IntentService {

    private static final String APK_URL = "APK_URL";
    private String apkUrl;
    private NotificationCompat.Builder notificationBuilder;
    private NotificationManager notificationManager;

    @Inject
    @Named("downloadSubject")
    BehaviorSubject subject;

    @Inject
    DataManager dataManager;
    private File file;

    public DownloadService() {
        super("DownloadService");
    }

    @Override
    public void onCreate() {
        ComponentHolder.getAppComponent().plus().inject(this);
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        apkUrl = intent.getStringExtra(APK_URL);
        file = FileUtils.getFile(System.currentTimeMillis() + ".apk");

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("找厂房")
                .setContentText("正在下载最新版本App,请稍后...")
                .setAutoCancel(true);

        notificationManager.notify(0, notificationBuilder.build());

        download();
    }

    private void download() {
        subject.subscribe(new RxSubscriber() {
            @Override
            public void _onNext(Object o) {
                DownloadState downloadState = (DownloadState) o;
                sendNotification(downloadState);
            }

            @Override
            public void _onError(Throwable throwable) {
                Timber.e(throwable.getMessage());
            }
        });

        dataManager.downloadApk(apkUrl)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .map(new Func1<ResponseBody, InputStream>() {
                    @Override
                    public InputStream call(ResponseBody responseBody) {
                        return responseBody.byteStream();
                    }
                })
                .observeOn(Schedulers.computation())
                .doOnNext(new Action1<InputStream>() {
                    @Override
                    public void call(InputStream inputStream) {
                        try {
                            FileUtils.writeFile(inputStream, file);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<InputStream>() {
                    @Override
                    public void onCompleted() {
                        downloadCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        downloadCompleted();
                    }

                    @Override
                    public void onNext(InputStream inputStream) {
                    }
                });;
    }

    private void downloadCompleted() {
        notificationManager.cancel(0);
        notificationBuilder.setProgress(0, 0, false);
        notificationBuilder.setContentText("下载完成");
        notificationManager.notify(0, notificationBuilder.build());

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        startActivity(intent);
    }

    private void sendNotification(DownloadState downloadState) {
        notificationBuilder.setProgress(100, (int)(downloadState.getBytesRead()*100/downloadState.getContentLength()), false);
        notificationBuilder.setContentText(
                FileUtils.formatFileSize(downloadState.getBytesRead()) + "/" + FileUtils.formatFileSize(downloadState.getContentLength()));
        notificationManager.notify(0, notificationBuilder.build());
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        notificationManager.cancel(0);
    }

    public static Intent getStartIntent(SplashActivity splashActivity, String apk_url) {
        Intent intent = new Intent(splashActivity, DownloadService.class);
        intent.putExtra(APK_URL, apk_url);
        return intent;
    }

}
