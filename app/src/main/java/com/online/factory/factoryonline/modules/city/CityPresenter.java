package com.online.factory.factoryonline.modules.city;

import com.online.factory.factoryonline.base.BasePresenter;
import com.online.factory.factoryonline.data.DataManager;

import javax.inject.Inject;

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
        /*dataManager.requestCities()
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
                });*/
    }
}
