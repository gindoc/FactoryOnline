package com.online.factory.factoryonline.modules.setting.qrcode;

import com.online.factory.factoryonline.base.BasePresenter;
import com.online.factory.factoryonline.data.DataManager;
import com.online.factory.factoryonline.models.response.DownloadUrlResponse;
import com.online.factory.factoryonline.utils.QRCodeUtil;
import com.online.factory.factoryonline.utils.WindowUtil;
import com.online.factory.factoryonline.utils.rx.RxResultHelper;
import com.online.factory.factoryonline.utils.rx.RxSubscriber;

import org.apache.commons.codec.binary.Base64;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * 作者: GIndoc
 * 日期: 2016/12/3 10:48
 * 作用:
 */

public class QRCodePresenter extends BasePresenter<QRCodeContract.View> implements QRCodeContract.Presenter {
    private DataManager dataManager;

    @Inject
    public QRCodePresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    public void createQRCode(String versionCode, final int width, final int height) {
        dataManager.requestDownloadUrl(versionCode)
                .compose(getView().<DownloadUrlResponse>getBindToLifecycle())
                .compose(RxResultHelper.<DownloadUrlResponse>handleResult())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<DownloadUrlResponse>() {
                    @Override
                    public void _onNext(DownloadUrlResponse response) {
                        String url = new String(Base64.decodeBase64(response.getDownload_url().getBytes()));
                        saveQRCode(url, width, height);
                    }

                    @Override
                    public void _onError(Throwable throwable) {
                        Timber.e(throwable.getMessage());
                    }
                });

    }

    private void saveQRCode(final String url, final int width, final int height) {
        Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                boolean success = QRCodeUtil.createQRImage(url, width, height, null, QRCodeContract.View.QRCODE_PATH);
                subscriber.onNext(success);
            }
        }).compose(getView().<Boolean>getBindToLifecycle())
                .filter(new Func1<Boolean, Boolean>() {
                    @Override
                    public Boolean call(Boolean aBoolean) {
                        return aBoolean;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<Boolean>() {
                    @Override
                    public void _onNext(Boolean aBoolean) {
                        getView().loadQRCode();
                    }

                    @Override
                    public void _onError(Throwable throwable) {
                        Timber.e(throwable.getMessage());
                    }
                });

    }
}
