package com.online.factory.factoryonline.modules.album.fragment.PhotoWall;

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
import timber.log.Timber;

/**
 * Created by cwenhui on 2016/10/19.
 */
public class PhotoWallPresenter extends BasePresenter<PhotoWallContract.View> implements PhotoWallContract
        .Presenter {

    @Inject
    Context context;

    @Inject
    public PhotoWallPresenter() {
    }

    @Override
    public void getPhotos() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(context, "暂无外部存储", Toast.LENGTH_SHORT).show();
            return;
        }
        getView().showLoadingDialog();
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                ScanImageUtils.scanImages(context);
                subscriber.onNext("");
            }
        })
        .compose(getView().<String>getBindToLifecycle())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {}

            @Override
            public void onError(Throwable e) {
                Timber.e(e.getMessage());
            }

            @Override
            public void onNext(String o) {
                getView().hideLoadingDialog();
                getView().initRecyclerview(ScanImageUtils.getMaxImgDir(), ScanImageUtils.getTotalCount());
            }
        });
    }

}
