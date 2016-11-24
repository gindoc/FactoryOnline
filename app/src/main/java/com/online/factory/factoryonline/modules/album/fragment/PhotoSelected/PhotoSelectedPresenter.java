package com.online.factory.factoryonline.modules.album.fragment.PhotoSelected;

import com.google.gson.JsonObject;
import com.online.factory.factoryonline.base.BasePresenter;
import com.online.factory.factoryonline.data.DataManager;
import com.online.factory.factoryonline.utils.rx.RxSubscriber;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by cwenhui on 2016.02.23
 */
public class PhotoSelectedPresenter extends BasePresenter<PhotoSelectedContract.View> implements
        PhotoSelectedContract.Presenter {
    private DataManager dataManager;

    @Inject
    public PhotoSelectedPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void deleteImage(final String imageKey) {
        dataManager.deleteImage(imageKey)
                .compose(getView().<JsonObject>getBindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<JsonObject>() {
                    @Override
                    public void _onNext(JsonObject jsonObject) {
                        getView().removeUploadedImage(imageKey);
                    }

                    @Override
                    public void _onError(Throwable throwable) {
                        Timber.e(throwable.getMessage());
                    }
                });
    }
}
