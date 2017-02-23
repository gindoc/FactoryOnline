package com.online.factory.factoryonline.modules.personalInfo.fragments.modifyName;

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
import timber.log.Timber;

/**
 * 作者: GIndoc
 * 日期: 2016/12/7 14:42
 * 作用:
 */

public class ModifyNamePresenter extends BasePresenter<ModifyNameContract.View> implements ModifyNameContract.Presenter {
    private DataManager dataManager;

    @Inject
    LoginContext loginContext;

    @Inject
    public ModifyNamePresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    public void modifyName(final String newName) {
        UpdateUser updateUser = new UpdateUser();
        updateUser.setUpdate_type(2);
        updateUser.setUpdate_value(newName);
        dataManager.updateUser(updateUser)
                .compose(getView().<Response>getBindToLifecycle())
                .compose(RxResultHelper.<Response>handleResult())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<Response>() {
                    @Override
                    public void _onNext(Response response) {
                        User user = Saver.getSerializableObject(SharePreferenceKey.USER);
                        user.setUserName(newName);
                        Saver.saveSerializableObject(user, SharePreferenceKey.USER);
                        getView().finish();
                    }

                    @Override
                    public void _onError(Throwable throwable) {
                        Timber.e(throwable.getMessage());
                        if (throwable.getMessage().contains("Unauthorized")||throwable.getMessage().contains("请先登录")){
                            Saver.logout();
                            loginContext.setmState(new LogOutState());
                            getView().unLogin();
                        }
                    }
                });
    }
}
