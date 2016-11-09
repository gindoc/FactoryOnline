package com.online.factory.factoryonline.modules.city;

import com.online.factory.factoryonline.base.BasePresenter;
import com.online.factory.factoryonline.data.DataManager;
import com.online.factory.factoryonline.models.City;
import com.online.factory.factoryonline.utils.rx.RxSubscriber;

import java.util.List;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by cwenhui on 2016/11/7.
 */

public class CityPresenter extends BasePresenter<CityContract.View> implements CityContract.Presenter {

    private DataManager dataManager;

    @Inject
    public CityPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void requestCityList() {
        dataManager.requestCities()
                .compose(getView().<List<City>>getBindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<List<City>>() {
                    @Override
                    public void _onNext(List<City> cities) {
                        getView().initCityList(cities);
                    }

                    @Override
                    public void _onError(Throwable throwable) {

                    }
                });
    }
}
