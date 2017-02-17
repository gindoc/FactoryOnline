package com.online.factory.factoryonline.modules.personalInfo.fragments.modifyName;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.online.factory.factoryonline.base.BaseFragment;
import com.online.factory.factoryonline.databinding.FragmentModifyNameBinding;
import com.online.factory.factoryonline.utils.StatusBarUtils;
import com.online.factory.factoryonline.utils.ToastUtil;
import com.trello.rxlifecycle.LifecycleTransformer;

import javax.inject.Inject;

/**
 * 作者: GIndoc
 * 日期: 2016/12/3 15:57
 * 作用:
 */

public class ModifyNameFragment extends BaseFragment<ModifyNameContract.View, ModifyNamePresenter> implements ModifyNameContract.View {
    public static final String USER_NAME = "USER_NAME";
    private FragmentModifyNameBinding mBinding;

    @Inject
    ModifyNamePresenter mPresenter;

    @Inject
    public ModifyNameFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        getComponent().inject(this);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentModifyNameBinding.inflate(inflater);
        mBinding.setView(this);

        StatusBarUtils.from((Activity) getContext())
                //白底黑字状态栏
                .setLightStatusBar(true)
                //设置toolbar,actionbar等view
                .setActionbarView(mBinding.rlTopBar)
                .process();

        mBinding.etUsername.setText(getArguments().getString(USER_NAME));

        return mBinding.getRoot();
    }

    @Override
    protected ModifyNamePresenter createPresent() {
        return mPresenter;
    }

    @Override
    public <T> LifecycleTransformer<T> getBindToLifecycle() {
        return bindToLifecycle();
    }

    @Override
    public void showError(String error) {

    }

    public void modifyName() {
        if (!TextUtils.isEmpty(mBinding.etUsername.getText())) {
            mPresenter.modifyName(mBinding.etUsername.getText().toString());
        } else {
            ToastUtil.show(getContext(), "请输入新用户名");
        }
    }

    public void clearName() {
        mBinding.etUsername.setText("");
    }

    @Override
    public void finish() {
        ToastUtil.show(getContext(), "修改名字成功");
        pop();
    }
}
