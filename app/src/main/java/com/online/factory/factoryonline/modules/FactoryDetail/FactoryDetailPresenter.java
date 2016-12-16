package com.online.factory.factoryonline.modules.FactoryDetail;

import android.view.MenuItem;

import com.online.factory.factoryonline.base.BasePresenter;
import com.online.factory.factoryonline.data.DataManager;
import com.online.factory.factoryonline.models.PublishUserResponse;
import com.online.factory.factoryonline.models.response.CollectionResponse;
import com.online.factory.factoryonline.models.response.Response;
import com.online.factory.factoryonline.utils.rx.RxResultHelper;
import com.online.factory.factoryonline.utils.rx.RxSubscriber;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by cwenhui on 2016/10/18.
 */
public class FactoryDetailPresenter extends BasePresenter<FactoryDetailContract.View> implements FactoryDetailContract.Presenter{
    private DataManager dataManager;

    @Inject
    public FactoryDetailPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }


    @Override
    public void isCollected(int fId, final MenuItem item) {
        dataManager.isFactoryCollected(fId)
                .compose(getView().<CollectionResponse>getBindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<CollectionResponse>() {
                    @Override
                    public void _onNext(CollectionResponse response) {
                        if (response.getErro_code() == 200 && response.isCollect()) {
                            getView().refleshCollectionState(item, response.isCollect());
                        } else{
                            getView().refleshCollectionState(item, false);
                        }
                    }

                    @Override
                    public void _onError(Throwable throwable) {
                        Timber.e(throwable.getMessage());
                    }
                });
    }

    public void postCollectionState(final MenuItem item, int id) {
        dataManager.postCollectionState(id)
                .compose(getView().<Response>getBindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<Response>() {
                    @Override
                    public void _onNext(Response response) {
                        if (response.getErro_code() == 200) {
                            getView().toogleCollectionState(item);
                        }
                    }

                    @Override
                    public void _onError(Throwable throwable) {
                        Timber.e(throwable.getMessage());
                    }
                });
    }

    public void deleteCollectionState(final MenuItem item, int id) {
        dataManager.deleteCollectionState(id)
                .compose(getView().<Response>getBindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<Response>() {
                    @Override
                    public void _onNext(Response response) {
                        if (response.getErro_code() == 200) {
                            getView().toogleCollectionState(item);
                        }
                    }

                    @Override
                    public void _onError(Throwable throwable) {
                        Timber.e(throwable.getMessage());
                    }
                });
    }

    @Override
    public void getPublishUser(int userId) {
        dataManager.getUserById(userId)
                .compose(getView().<PublishUserResponse>getBindToLifecycle())
                .compose(RxResultHelper.<PublishUserResponse>handleResult())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<PublishUserResponse>() {
                    @Override
                    public void _onNext(PublishUserResponse publishUserResponse) {
                        getView().initPublishUser(publishUserResponse.getUserPublic());
                    }

                    @Override
                    public void _onError(Throwable throwable) {
                        Timber.e(throwable.getMessage());
                    }
                });
    }
}
