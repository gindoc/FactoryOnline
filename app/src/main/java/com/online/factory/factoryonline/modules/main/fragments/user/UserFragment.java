package com.online.factory.factoryonline.modules.main.fragments.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.online.factory.factoryonline.base.BaseFragment;
import com.online.factory.factoryonline.base.BasePresenter;
import com.online.factory.factoryonline.databinding.FragmentUserBinding;

import javax.inject.Inject;

/**
 * Created by louiszgm on 2016/9/30.
 */

public class UserFragment extends BaseFragment {
    private FragmentUserBinding mBinding;

    @Inject
    UserPresenter mPresenter;

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
    protected BasePresenter createPresent() {
        return null;
    }
}
