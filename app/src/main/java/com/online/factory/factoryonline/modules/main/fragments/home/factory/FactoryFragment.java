package com.online.factory.factoryonline.modules.main.fragments.home.factory;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.online.factory.factoryonline.base.BaseFragment;
import com.online.factory.factoryonline.base.BasePresenter;
import com.online.factory.factoryonline.databinding.FragmentFindBinding;
import com.online.factory.factoryonline.modules.main.MainActivity;
import com.trello.rxlifecycle.LifecycleTransformer;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * 作者: GIndoc
 * 日期: 2016/12/19 9:35
 * 作用:
 */

public class FactoryFragment extends BaseFragment {
    private FragmentFindBinding mFindBinding;

    @Inject
    public FactoryFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        getComponent().inject(this);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFindBinding = FragmentFindBinding.inflate(inflater);
        mFindBinding.setFindView(this);

        return mFindBinding.getRoot();
    }

    public void openRecommend() {
        ((MainActivity)getActivity()).onClickRecommend(null);
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
