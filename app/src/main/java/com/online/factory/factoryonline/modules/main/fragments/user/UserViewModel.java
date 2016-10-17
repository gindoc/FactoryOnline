package com.online.factory.factoryonline.modules.main.fragments.user;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.online.factory.factoryonline.models.UserBean;

import javax.inject.Inject;

/**
 * Created by cwenhui on 2016/10/17.
 */
public class UserViewModel extends BaseObservable {
    private UserBean userBean;

    @Inject
    public UserViewModel() {
    }

    public void setUserBean(UserBean userBean) {
        this.userBean = userBean;
    }

    @Bindable
    public String getItemText() {
        return userBean.getText();
    }

    @Bindable
    public int getItemImg() {
        return userBean.getImgId();
    }
}
