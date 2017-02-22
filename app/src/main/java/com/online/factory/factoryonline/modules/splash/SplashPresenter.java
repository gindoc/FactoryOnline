package com.online.factory.factoryonline.modules.splash;

import com.online.factory.factoryonline.base.BasePresenter;
import com.online.factory.factoryonline.data.DataManager;
import com.online.factory.factoryonline.models.response.UpdateInfoResponse;
import com.online.factory.factoryonline.utils.rx.RxResultHelper;
import com.online.factory.factoryonline.utils.rx.RxSubscriber;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 作者: GIndoc
 * 日期: 2017/2/21 15:41
 * 作用:
 */

public class SplashPresenter extends BasePresenter<SplashContract.View> implements SplashContract.Presenter{

    private DataManager dataManager;

    @Inject
    public SplashPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void requestUpdateInfo(int versionCode) {
        dataManager.requestUpdateInfo(versionCode)
                .compose(getView().<UpdateInfoResponse>getBindToLifecycle())
                .compose(RxResultHelper.<UpdateInfoResponse>handleResult())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<UpdateInfoResponse>() {
                    @Override
                    public void _onNext(UpdateInfoResponse updateInfoResponse) {
                        getView().showAlertDialog(updateInfoResponse.getUpdateInfo());
                    }

                    @Override
                    public void _onError(Throwable throwable) {
                        getView().toMainActivityForSeconds();
                    }
                });
    }

}
