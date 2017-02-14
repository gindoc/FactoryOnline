package com.online.factory.factoryonline.modules.feedback;

import com.online.factory.factoryonline.base.BasePresenter;
import com.online.factory.factoryonline.data.DataManager;
import com.online.factory.factoryonline.models.response.Response;
import com.online.factory.factoryonline.utils.rx.RxResultHelper;
import com.online.factory.factoryonline.utils.rx.RxSubscriber;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 作者: GIndoc
 * 日期: 2017/2/14 10:25
 * 作用:
 */

public class FeedbackPresenter extends BasePresenter<FeedbackContract.View> implements FeedbackContract.Presenter {

    private DataManager dataManager;

    @Inject
    public FeedbackPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }


    @Override
    public void submitFeedback(String description, String phoneNum) {
        dataManager.feedback("2", description, phoneNum)
                .compose(getView().<Response>getBindToLifecycle())
                .compose(RxResultHelper.<Response>handleResult())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<Response>() {
                    @Override
                    public void _onNext(Response response) {
                        getView().submitSuccessful();
                    }

                    @Override
                    public void _onError(Throwable throwable) {
                        getView().showError(throwable.getMessage());
                    }
                });
    }
}
