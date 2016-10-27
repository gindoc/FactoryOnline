package com.online.factory.factoryonline.modules.locate.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.online.factory.factoryonline.base.BaseFragment;
import com.online.factory.factoryonline.base.BasePresenter;
import com.online.factory.factoryonline.databinding.FragmentLocationBinding;

import javax.inject.Inject;

/**
 * Created by louiszgm on 2016/10/26.
 */

public class LocationFragment extends BaseFragment {

    private FragmentLocationBinding mBinding;

    @Inject
     LocationClient mLocationClient;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentLocationBinding.inflate(inflater);

        return mBinding.getRoot();
    }

    @Override
    protected BasePresenter createPresent() {
        return null;
    }
}
