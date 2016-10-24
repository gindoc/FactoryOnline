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
import com.online.factory.factoryonline.databinding.FragmentLoginbySmsBinding;
import com.online.factory.factoryonline.models.exception.ValidateException;
import com.online.factory.factoryonline.models.post.Login;
import com.online.factory.factoryonline.modules.login.LoginActivity;
import com.online.factory.factoryonline.utils.MD5;
import com.online.factory.factoryonline.utils.Validate;

import javax.inject.Inject;

/**
 * Created by louiszgm on 2016/9/30.
 */

public class SmsLoginFragment extends BaseFragment{
    private FragmentLoginbySmsBinding mBinding;
    @Inject
    public SmsLoginFragment() {
        setTitle("短信验证登录");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentLoginbySmsBinding.inflate(inflater);
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
                        login.setLogin_key_md5(MD5.getMD5(getInputPwd()));
                        login.setLogin_type(1);
                        ((LoginActivity)getActivity()).login(login);
                    }
                } catch (ValidateException e) {
                    e.printStackTrace();
                    ((LoginActivity)getActivity()).showError(e.getMessage());
                }

            }
        });
    }
    private String getInputPhoneNum() {
        Editable phone_num = mBinding.etPhonenum.getEditText().getEditableText();
        return phone_num == null ? null:phone_num.toString();
    }

    private String getInputPwd(){
        Editable pwd = mBinding.etVerificationcode.getEditText().getEditableText();
        return pwd == null ? null:pwd.toString();
    }
    @Override
    protected BasePresenter createPresent() {
        return null;
    }
}
