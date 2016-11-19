package com.online.factory.factoryonline.modules.publishRental.area;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.online.factory.factoryonline.base.BasePresenter;
import com.online.factory.factoryonline.data.DataManager;
import com.online.factory.factoryonline.models.Area;
import com.online.factory.factoryonline.utils.rx.RxSubscriber;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * 作者: GIndoc
 * 日期: 2016/11/18 10:49
 * 作用:
 */

public class AreaPresenter extends BasePresenter<AreaContract.View> implements AreaContract.Presenter {

    private DataManager dataManager;
    @Inject
    public AreaPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    public void getAreas() {
        dataManager.getAreas()
                .compose(getView().<JsonObject>getBindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<JsonObject>() {
                    @Override
                    public void _onNext(JsonObject jsonObject) {
                        List<Area> areas = new Gson().fromJson(jsonObject.get("child"),
                                new TypeToken<List<Area>>() {}.getType());
                        getView().initAreaList(areas);
                    }

                    @Override
                    public void _onError(Throwable throwable) {
                        Timber.e(throwable.getMessage());
                    }
                });
    }
}
