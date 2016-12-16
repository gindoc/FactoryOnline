package com.online.factory.factoryonline.modules.setting;

import android.content.Context;

import com.online.factory.factoryonline.base.BasePresenter;
import com.online.factory.factoryonline.utils.DataCleanManager;
import com.online.factory.factoryonline.utils.FileUtils;
import com.online.factory.factoryonline.utils.rx.RxSubscriber;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * 作者: GIndoc
 * 日期: 2016/12/2 17:05
 * 作用:
 */

public class SettingPresenter extends BasePresenter<SettingContract.View> implements SettingContract.Presenter{

    @Inject
    Context context;

    @Inject
    public SettingPresenter() {
    }

    public void caculateCacheSize() {
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                long filesSize = 0;
                long cacheSize = FileUtils.getDirSize(context.getCacheDir());
                long databaseSize = FileUtils.getDirSize(FileUtils.getDatabaseDir(context));
                long imagesSize = FileUtils.getDirSize(FileUtils.getImageDir());
                filesSize = cacheSize + databaseSize + imagesSize;
                subscriber.onNext(FileUtils.formatFileSize(filesSize));
            }
        }).compose(getView().<String>getBindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<String>() {
                    @Override
                    public void _onNext(String cacheSize) {
                        getView().loadCacheSize(cacheSize);
                    }

                    @Override
                    public void _onError(Throwable throwable) {
                        Timber.e(throwable.getMessage());
                    }
                });
    }

    public void clearCache() {
        Observable.create(new Observable.OnSubscribe<Void>() {
            @Override
            public void call(Subscriber<? super Void> subscriber) {
                DataCleanManager.cleanInternalCache(context);
                DataCleanManager.cleanDatabases(context);
                DataCleanManager.cleanCustomCache(FileUtils.getImagePath());
                subscriber.onNext(null);
            }
        }).compose(getView().<Void>getBindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<Void>() {
                    @Override
                    public void _onNext(Void aVoid) {
                        getView().showError("清除缓存成功");
                        caculateCacheSize();
                    }

                    @Override
                    public void _onError(Throwable throwable) {
                        Timber.e(throwable.getMessage());
                    }
                });
    }

}
