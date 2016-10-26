package com.online.factory.factoryonline.modules.baidumap;

import com.online.factory.factoryonline.base.BasePresenter;
import com.online.factory.factoryonline.data.DataManager;
import com.online.factory.factoryonline.models.Factory;
import com.online.factory.factoryonline.models.FactoryPoi;
import com.online.factory.factoryonline.models.response.FactoryPoiResponse;
import com.online.factory.factoryonline.models.response.FactoryResponse;
import com.online.factory.factoryonline.utils.rx.RxSubscriber;

import java.util.ArrayList;
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
    public void getStreetFactoryList(int streetId) {
        dataManager.getStreetFactories(streetId)
                .compose(getView().<FactoryResponse>getBindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<FactoryResponse>() {
                    @Override
                    public void _onNext(FactoryResponse response) {
                        getView().loadFactories(response.getFactory());
                    }

                    @Override
                    public void _onError(Throwable throwable) {
                    }
                });
    }

    @Override
    public void getLatLngList(int cityId) {
        dataManager.getLatLngs(cityId)
                .compose(getView().<FactoryPoiResponse>getBindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<FactoryPoiResponse>() {
                    @Override
                    public void _onNext(FactoryPoiResponse response) {
                        getView().loadMarker(response.getFactoryPois());
                    }

                    @Override
                    public void _onError(Throwable throwable) {

                    }
                });
    }
}
