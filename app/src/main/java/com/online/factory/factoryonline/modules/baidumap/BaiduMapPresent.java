package com.online.factory.factoryonline.modules.baidumap;

import com.online.factory.factoryonline.base.BasePresenter;
import com.online.factory.factoryonline.data.DataManager;
import com.online.factory.factoryonline.models.Factory;
import com.online.factory.factoryonline.utils.rx.RxSubscriber;

import java.util.List;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by louiszgm on 2016/10/18.
 */
public class BaiduMapPresent extends BasePresenter<BaiduMapConstract.View> implements BaiduMapConstract.Presenter {
    private DataManager dataManager;

    @Inject
    public BaiduMapPresent(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void getLatLngList(int streetId) {
        dataManager.getStreetFactories(streetId)
                .compose(getView().<List<Factory>>getBindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<List<Factory>>() {
                    @Override
                    public void _onNext(List<Factory> factories) {

                    }

                    @Override
                    public void _onError(Throwable throwable) {
                    }
                });
    }
}
