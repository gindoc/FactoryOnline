package com.online.factory.factoryonline.modules.FactoryDetail;

import android.view.MenuItem;

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.base.BasePresenter;
import com.online.factory.factoryonline.data.DataManager;
import com.online.factory.factoryonline.utils.rx.RxSubscriber;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;

/**
 * Created by cwenhui on 2016/10/18.
 */
public class FactoryDetailPresenter extends BasePresenter<FactoryDetailContract.View> implements FactoryDetailContract.Presenter{
    private DataManager dataManager;

    @Inject
    BehaviorSubject subject;

    @Inject
    public FactoryDetailPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }


    @Override
    public void isCollected(int fId, final MenuItem item) {
        dataManager.isFactoryCollected(fId)
                .compose(getView().<Boolean>getBindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if (aBoolean) {
                            item.setIcon(R.drawable.ic_collected_with_shadow);
                        }else{
                            item.setIcon(R.drawable.ic_collect_with_shadow);
                        }
                    }
                });
        subject.subscribe(new RxSubscriber() {
            @Override
            public void _onNext(Object o) {

            }

            @Override
            public void _onError(Throwable throwable) {

            }
        });
    }
}
