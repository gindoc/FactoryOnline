package com.online.factory.factoryonline.modules.main.fragments.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.online.factory.factoryonline.base.BaseFragment;
import com.online.factory.factoryonline.data.remote.FactoryApi;
import com.online.factory.factoryonline.databinding.FragmentHomeBinding;
import com.online.factory.factoryonline.models.News;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by cwenhui on 2016.02.23
 */
public class HomeFragment extends BaseFragment<HomeContract.View, HomePresenter> implements HomeContract.View {
    private FragmentHomeBinding mBinding;
//    private SlideShowView slideShowView;

    @Inject
    public HomeFragment() {
    }

    @Inject
    HomePresenter mPresenter;

    @Inject
    FactoryApi mApi;

    @Override
    protected HomePresenter createPresent() {
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

        mPresenter.requestIndexPicUrls();
//        mPresenter.requestScrollMsg();

        return mBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

        mBinding.scrollTxtView.startAutoScroll();

        mBinding.slideShowView.startPlay();
    }

    @Override
    public void onPause() {
        super.onPause();
        mBinding.slideShowView.stopPlay();

        mBinding.scrollTxtView.stopAutoScroll();

    }

    @Override
    public LifecycleTransformer getBindToLifecycle() {
        return bindToLifecycle();
    }

    @Override
    public void showError(String error) {
    }

    @Override
    public void initSlideShowView(String[] urls) {
        mBinding.slideShowView.setImageUrls(urls);
    }

    @Override
    public void initScrollTextView(List<News> newses) {
        mBinding.scrollTxtView.setNews(newses);
    }
}
