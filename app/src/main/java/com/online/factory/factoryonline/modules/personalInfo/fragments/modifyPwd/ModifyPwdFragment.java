package com.online.factory.factoryonline.modules.personalInfo.fragments.modifyPwd;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.online.factory.factoryonline.base.BaseFragment;
import com.online.factory.factoryonline.base.BasePresenter;
import com.online.factory.factoryonline.databinding.FragmentModifyPwdBinding;
import com.trello.rxlifecycle.LifecycleTransformer;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * 作者: GIndoc
 * 日期: 2016/12/3 18:02
 * 作用:
 */

public class ModifyPwdFragment extends BaseFragment {
    private FragmentModifyPwdBinding mBinding;

    @Inject
    public ModifyPwdFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        getComponent().inject(this);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentModifyPwdBinding.inflate(inflater);
        mBinding.setView(this);
        return mBinding.getRoot();
    }

    @Override
    protected BasePresenter createPresent() {
        return null;
    }

    @Nonnull
    @Override
    public LifecycleTransformer bindUntilEvent(@Nonnull Object event) {
        return null;
    }
}
