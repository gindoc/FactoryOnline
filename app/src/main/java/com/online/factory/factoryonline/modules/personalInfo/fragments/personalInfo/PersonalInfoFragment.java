package com.online.factory.factoryonline.modules.personalInfo.fragments.personalInfo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.base.BaseFragment;
import com.online.factory.factoryonline.base.BasePresenter;
import com.online.factory.factoryonline.databinding.FragmentPersonalInfoBinding;
import com.online.factory.factoryonline.modules.login.LogOutState;
import com.online.factory.factoryonline.modules.login.LoginContext;
import com.online.factory.factoryonline.modules.personalInfo.fragments.modifyName.ModifyNameFragment;
import com.online.factory.factoryonline.modules.personalInfo.fragments.modifyPwd.ModifyPwdFragment;
import com.trello.rxlifecycle.LifecycleTransformer;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * 作者: GIndoc
 * 日期: 2016/12/3 16:47
 * 作用:
 */

public class PersonalInfoFragment extends BaseFragment<PersonalInfoContract.View, PersonalInfoPresenter> implements PersonalInfoContract.View {
    private FragmentPersonalInfoBinding mBinding;

    @Inject
    PersonalInfoPresenter mPresenter;

    @Inject
    ModifyNameFragment modifyNameFragment;

    @Inject
    ModifyPwdFragment modifyPwdFragment;

    @Inject
    LoginContext mLoginContext;

    @Inject
    public PersonalInfoFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        getComponent().inject(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected PersonalInfoPresenter createPresent() {
        return mPresenter;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentPersonalInfoBinding.inflate(inflater);
        mBinding.setView(this);
        mBinding.setPresenter(mPresenter);

        return mBinding.getRoot();
    }

    public void modifyName() {
        start(modifyNameFragment);
    }

    public void modifyPwd(){
        start(modifyPwdFragment);
    }

    public void finish() {
        getActivity().finish();
    }

    @Override
    public void refreshWhenLogOut() {
        Toast.makeText(getContext(), "注销成功", Toast.LENGTH_SHORT).show();
        mLoginContext.setmState(new LogOutState());
        getActivity().finish();
    }

    @Override
    public <T> LifecycleTransformer<T> getBindToLifecycle() {
        return bindToLifecycle();
    }

    @Override
    public void showError(String error) {

    }
}
