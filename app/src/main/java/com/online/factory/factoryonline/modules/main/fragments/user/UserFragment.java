package com.online.factory.factoryonline.modules.main.fragments.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.online.factory.factoryonline.base.BaseFragment;
import com.online.factory.factoryonline.databinding.FragmentUserBinding;
import com.online.factory.factoryonline.modules.login.LogInState;
import com.online.factory.factoryonline.modules.login.LogOutState;
import com.online.factory.factoryonline.modules.login.LoginContext;
import com.trello.rxlifecycle.LifecycleTransformer;

import javax.inject.Inject;

/**
 * Created by louiszgm on 2016/9/30.
 */

public class UserFragment extends BaseFragment<UserContract.View,UserPresenter> implements UserContract.View {
    private FragmentUserBinding mBinding;

    @Inject
    UserPresenter mPresenter;

    @Inject
    LoginContext mLoginContext;
    @Inject
    public UserFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        getComponent().inject(this);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentUserBinding.inflate(inflater);
        mBinding.setPresenter(mPresenter);
        return mBinding.getRoot();
    }

    @Override
    protected UserPresenter createPresent() {
        return mPresenter;
    }

    @Override
    public void startLogIn() {
        Toast.makeText(getContext(),"登录成功",Toast.LENGTH_SHORT).show();
        mLoginContext.setmState(new LogInState());
    }

    @Override
    public void refreshWhenLogOut() {
        Toast.makeText(getContext(),"注销成功",Toast.LENGTH_SHORT).show();
        mLoginContext.setmState(new LogOutState());
    }

    @Override
    public LifecycleTransformer getBindToLifecycle() {
        return null;
    }

    @Override
    public void showError(String error) {

    }
}
