package com.online.factory.factoryonline.modules.main.fragments.home;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.online.factory.factoryonline.base.BaseFragment;
import com.online.factory.factoryonline.customview.recyclerview.BaseRecyclerViewAdapter;
import com.online.factory.factoryonline.data.remote.FactoryApi;
import com.online.factory.factoryonline.databinding.FragmentHomeBinding;
import com.online.factory.factoryonline.databinding.LayoutHomeHeaderBinding;
import com.online.factory.factoryonline.models.FactoryInfo;
import com.online.factory.factoryonline.models.News;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Created by cwenhui on 2016.02.23
 */
public class HomeFragment extends BaseFragment<HomeContract.View, HomePresenter> implements HomeContract
        .View, HomeRecyclerView.ScrollChangedListener {
    private FragmentHomeBinding mBinding;
    private LayoutHomeHeaderBinding mHeaderBinding;

    private BaseRecyclerViewAdapter mAdapter;

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
        mHeaderBinding = LayoutHomeHeaderBinding.inflate(inflater);

        mBinding.setPresenter(mPresenter);

        mPresenter.requestIndexPicUrls();

        List<FactoryInfo> datas = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            FactoryInfo info = new FactoryInfo();
            info.setName("No. " + i);
            datas.add(info);
        }
        mAdapter = new HomeRecyclerViewAdapter(getContext(), datas);
        mBinding.recyclerView.setAdapter(mAdapter);
        mBinding.recyclerView.setScrollChangedListener(this);
        mBinding.recyclerView.addHeader(mHeaderBinding.getRoot());
        mBinding.recyclerView.init();

        return mBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

        mHeaderBinding.scrollTxtView.startAutoScroll();
        mHeaderBinding.slideShowView.startPlay();
    }

    @Override
    public void onPause() {
        super.onPause();
        mHeaderBinding.slideShowView.stopPlay();
        mHeaderBinding.scrollTxtView.stopAutoScroll();

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
        mHeaderBinding.slideShowView.setImageUrls(urls);
    }

    @Override
    public void initScrollTextView(List<News> newses) {
        mHeaderBinding.scrollTxtView.setNews(newses);
    }

    @Override
    public void onScrolled(int dy) {
        Timber.e("dy: " + dy);
        int limit = mBinding.searchview.getHeight();
        Timber.e("limit:  " + limit);
        mBinding.coverView.setAlpha(dy / 100f);
        if (limit > 0) {
            if (dy <= limit) {
                Timber.e("1 - dy / limit:" + (1 - dy * 1.0f / limit));
                ObjectAnimator
                        .ofFloat(mBinding.searchview, "scaleX", 1 - dy * 1.0f / (3*limit))
                        .setDuration(limit / 700)
                        .start();
                ObjectAnimator.ofFloat(mBinding.searchview, "translationY", -limit / 100 * dy)
                        .setDuration(limit / 700 )
                        .start();
                Timber.e("Y   : " + -limit / 100 * dy);
            }
        }
    }

}