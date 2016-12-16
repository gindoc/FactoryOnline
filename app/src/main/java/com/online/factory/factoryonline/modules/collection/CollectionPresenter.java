package com.online.factory.factoryonline.modules.collection;

import com.online.factory.factoryonline.base.BasePresenter;
import com.online.factory.factoryonline.data.DataManager;
import com.online.factory.factoryonline.models.response.MyCollectionResponse;
import com.online.factory.factoryonline.utils.rx.RxResultHelper;
import com.online.factory.factoryonline.utils.rx.RxSubscriber;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * 作者: GIndoc
 * 日期: 2016/12/1 17:36
 * 作用:
 */

public class CollectionPresenter extends BasePresenter<CollectionContract.View> implements CollectionContract.Presenter {

    private DataManager dataManager;
    @Inject
    public CollectionPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }

}
