package com.online.factory.factoryonline.modules.main.fragments.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.online.factory.factoryonline.base.BaseFragment;
import com.online.factory.factoryonline.base.BasePresenter;
import com.online.factory.factoryonline.databinding.FragmentHomeBinding;

import javax.inject.Inject;

/**
 * Created by cwenhui on 2016.02.23
 */
public class HomeFragment extends BaseFragment {
    private FragmentHomeBinding mBinding;

    @Inject
    public HomeFragment() {
    }

    @Inject
    HomePresenter mPresenter;

    @Override
    protected BasePresenter createPresent() {
        return mPresenter;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        getComponent().inject(this);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = FragmentHomeBinding.inflate(inflater);
        mBinding.setPresenter(mPresenter);
        return mBinding.getRoot();
    }
}
