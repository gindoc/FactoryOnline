package com.online.factory.factoryonline.modules.FactoryDetail.report;

import com.online.factory.factoryonline.base.BasePresenter;
import com.online.factory.factoryonline.data.DataManager;
import com.online.factory.factoryonline.models.response.Response;
import com.online.factory.factoryonline.utils.rx.RxResultHelper;
import com.online.factory.factoryonline.utils.rx.RxSubscriber;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * 作者: GIndoc
 * 日期: 2016/11/14 14:24
 * 作用:
 */

public class ReportPresenter extends BasePresenter<ReportContract.View> implements ReportContract.Presenter {
    private DataManager dataManager;
    @Inject
    public ReportPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    public void feedback(int messageId, String content, String remark){
        dataManager.messageFeedback(messageId, content, remark)
                .compose(getView().<Response>getBindToLifecycle())
                .compose(RxResultHelper.<Response>handleResult())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<Response>() {
                    @Override
                    public void _onNext(Response response) {
                        getView().feedbackSuccessful();
                    }

                    @Override
                    public void _onError(Throwable throwable) {
                        Timber.e(throwable.getMessage());
                    }
                });
    }
}
