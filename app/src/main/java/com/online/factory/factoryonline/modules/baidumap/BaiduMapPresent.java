package com.online.factory.factoryonline.modules.baidumap;

import com.online.factory.factoryonline.base.BasePresenter;
import com.online.factory.factoryonline.data.DataManager;
import com.online.factory.factoryonline.models.response.BaiduMapResponse;
import com.online.factory.factoryonline.models.response.FactoryResponse;
import com.online.factory.factoryonline.models.response.RecommendResponse;
import com.online.factory.factoryonline.utils.rx.RxResultHelper;
import com.online.factory.factoryonline.utils.rx.RxSubscriber;

import java.net.ConnectException;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by louiszgm on 2016/10/18.
 */
public class BaiduMapPresent extends BasePresenter<BaiduMapConstract.View> implements BaiduMapConstract.Presenter {
    private DataManager dataManager;

    @Inject
    public BaiduMapPresent(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    /**
     * 请求推荐列表
     * @param page          请求的页码，如果不输，默认为1
     */
    public void requestWantedMessagesByNet(final int page, final int areaId) {
        dataManager.getMaxUpdateTime()
                .compose(getView().<Integer>getBindToLifecycle())
                .flatMap(new Func1<Integer, Observable<RecommendResponse>>() {
                    @Override
                    public Observable<RecommendResponse> call(Integer integer) {
                        Map<String, Object> params = new HashMap<String, Object>();
                        params.put("since", integer);
                        params.put("max", System.currentTimeMillis() / 1000);
                        params.put("page", page);
                        params.put("area_id", areaId);
                        params.put("filtertype", "[1]");
                        return dataManager.getStreetFactories(params);
                    }
                })
                .compose(RxResultHelper.<RecommendResponse>handleResult())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<RecommendResponse>() {
                    @Override
                    public void _onNext(RecommendResponse response) {
                        getView().loadWantedMessages(response.getWantedMessages());
                    }

                    @Override
                    public void _onError(Throwable throwable) {
                        if (throwable instanceof ConnectException) {
                            getView().showError("网络连接失败，请检查你的网络！！！");
                        }
                        Timber.e(throwable.getMessage());
                    }
                });
    }

    @Override
    public void getLatLngList(int cityId) {
        dataManager.getLatLngs(cityId)
                .compose(getView().<BaiduMapResponse>getBindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<BaiduMapResponse>() {
                    @Override
                    public void _onNext(BaiduMapResponse response) {
                        getView().loadMarker(response.getMapPois());
                    }

                    @Override
                    public void _onError(Throwable throwable) {
                        Timber.e(throwable.getMessage());
                    }
                });
    }
}
