package com.online.factory.factoryonline.modules.login.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.base.BaseFragment;
import com.online.factory.factoryonline.base.BasePresenter;

import javax.inject.Inject;

/**
 * Created by louiszgm on 2016/9/30.
 */

public class SmsLoginFragment extends BaseFragment{
    @Inject
    public SmsLoginFragment() {
        setTitle("短信验证登录");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_loginby_sms,null);
    }

    @Override
    protected BasePresenter createPresent() {
        return null;
    }
}
