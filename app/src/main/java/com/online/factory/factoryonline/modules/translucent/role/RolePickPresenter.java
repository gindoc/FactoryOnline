package com.online.factory.factoryonline.modules.translucent.role;

import com.online.factory.factoryonline.base.BasePresenter;
import com.online.factory.factoryonline.data.DataManager;
import com.online.factory.factoryonline.models.UpdateUser;
import com.online.factory.factoryonline.models.response.Response;
import com.online.factory.factoryonline.utils.rx.RxResultHelper;
import com.online.factory.factoryonline.utils.rx.RxSubscriber;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 作者: GIndoc
 * 日期: 2017/2/4 10:52
 * 作用:
 */

public class RolePickPresenter extends BasePresenter<RolePickContract.View> implements RolePickContract.Presenter {
    private DataManager dataManager;
    @Inject
    public RolePickPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }


    public void switchRole(String type) {
        UpdateUser updateUser = new UpdateUser();
        updateUser.setUpdate_type(4);
        updateUser.setUpdate_value(type);
        dataManager.updateUser(updateUser)
                .compose(getView().<Response>getBindToLifecycle())
                .compose(RxResultHelper.<Response>handleResult())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<Response>() {
                    @Override
                    public void _onNext(Response response) {
                        getView().roleSwitchingSuccessful();
                    }

                    @Override
                    public void _onError(Throwable throwable) {
                        getView().showError(throwable.getMessage());
                    }
                });
    }
}
