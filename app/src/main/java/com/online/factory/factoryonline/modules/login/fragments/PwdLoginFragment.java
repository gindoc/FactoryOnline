package com.online.factory.factoryonline.modules.login.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.base.BaseFragment;
import com.online.factory.factoryonline.base.BasePresenter;
import com.online.factory.factoryonline.databinding.FragmentLoginbyPwdBinding;
import com.online.factory.factoryonline.models.exception.ValidateException;
import com.online.factory.factoryonline.models.post.Login;
import com.online.factory.factoryonline.modules.login.LoginActivity;
import com.online.factory.factoryonline.utils.MD5;
import com.online.factory.factoryonline.utils.Validate;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by louiszgm on 2016/9/30.
 */

public class PwdLoginFragment  extends BaseFragment{

    private FragmentLoginbyPwdBinding mBinding;
    @Inject
    public PwdLoginFragment() {
        setTitle("账号登录");
    }

    @Inject
    @Named("device_id")
    String device_id;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentLoginbyPwdBinding.inflate(inflater);
        handleLoginAction();
        return mBinding.getRoot();
    }

    private void handleLoginAction() {
        mBinding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login login = new Login();
                try {
                    if(Validate.validatePhoneNum(getInputPhoneNum())){
                        login.setUser_name(getInputPhoneNum());
                        login.setPwd(getInputPwd());
                        login.setLogin_type(2);
                        login.setDevice_id(device_id);
                        ((LoginActivity)getActivity()).login(login);
                    }
                } catch (ValidateException e) {
                    e.printStackTrace();
                    ((LoginActivity)getActivity()).showError(e.getMessage());
                }

            }
        });
    }

    @Override
    protected BasePresenter createPresent() {
        return null;
    }

    @Nullable
    private String getInputPhoneNum() {
        Editable phone_num = mBinding.etPhonenum.getEditText().getEditableText();
        return phone_num == null ? null:phone_num.toString();
    }

    @Nullable
    private String getInputPwd(){
        Editable pwd = mBinding.etPwd.getEditText().getEditableText();
        return pwd == null ? null:pwd.toString();
    }
}
