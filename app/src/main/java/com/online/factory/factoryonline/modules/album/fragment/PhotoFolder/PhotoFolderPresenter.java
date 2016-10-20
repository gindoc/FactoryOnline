package com.online.factory.factoryonline.modules.album.fragment.PhotoFolder;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import com.online.factory.factoryonline.base.BasePresenter;
import com.online.factory.factoryonline.utils.ScanImageUtils;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by cwenhui on 2016/10/20.
 */
public class PhotoFolderPresenter extends BasePresenter<PhotoFolderContract.View> implements PhotoFolderContract.Presenter{

    @Inject
    Context context;

    @Inject
    public PhotoFolderPresenter() {
    }

    @Override
    public void getPhotoFolders() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(context, "暂无外部存储", Toast.LENGTH_SHORT).show();
            return;
        }
        getView().showLoadingDialog();
        Observable.create(new Observable.OnSubscribe<Void>() {
            @Override
            public void call(Subscriber<? super Void> subscriber) {
                ScanImageUtils.scanImages(context);
                subscriber.onNext(null);
            }
        })
        .compose(getView().<Void>getBindToLifecycle())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<Void>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(Void aVoid) {
                getView().hideLoadingDialog();
            }
        });
    }
}
