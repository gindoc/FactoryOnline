package com.online.factory.factoryonline.modules.translucent.role;

import com.online.factory.factoryonline.base.BasePresenter;
import com.online.factory.factoryonline.data.DataManager;
import com.online.factory.factoryonline.data.local.SharePreferenceKey;
import com.online.factory.factoryonline.models.UpdateUser;
import com.online.factory.factoryonline.models.User;
import com.online.factory.factoryonline.models.response.Response;
import com.online.factory.factoryonline.modules.login.LogOutState;
import com.online.factory.factoryonline.modules.login.LoginContext;
import com.online.factory.factoryonline.utils.Saver;
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
    LoginContext loginContext;

    @Inject
    public RolePickPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }


    public void switchRole(final String type) {
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
                        getUser(type);
                    }

                    @Override
                    public void _onError(Throwable throwable) {
                        if (throwable.getMessage().contains("Unauthorized")||throwable.getMessage().contains("请先登录")){
                            Saver.logout();
                            loginContext.setmState(new LogOutState());
                            getView().unLogin();
                        }
                        getView().showError(throwable.getMessage());
                    }
                });
    }

    public void getUser(String type) {
        User user = Saver.getSerializableObject(SharePreferenceKey.USER);
        user.setType(Integer.parseInt(type));
        getView().roleSwitchingSuccessful(user);
    }
}
