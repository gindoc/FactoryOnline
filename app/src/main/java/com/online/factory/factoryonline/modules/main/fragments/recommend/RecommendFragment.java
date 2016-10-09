package com.online.factory.factoryonline.modules.main.fragments.recommend;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.base.BaseFragment;
import com.online.factory.factoryonline.databinding.FragmentRecommendBinding;
import com.online.factory.factoryonline.models.FactoryInfo;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Created by louiszgm on 2016/9/30.
 */
public class RecommendFragment extends BaseFragment<RecommendContract.View, RecommendPresenter> implements
        RecommendContract.View, SwipeRefreshLayout.OnRefreshListener {

    private FragmentRecommendBinding mBinding;
    @Inject
    RecommendRecyclerViewAdapter mAdapter;
    @Inject
    RecommendPresenter mPresenter;

    @Inject
    public RecommendFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        getComponent().inject(this);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        mBinding = FragmentRecommendBinding.inflate(inflater);

        mBinding.recyclerView.setAdapter(mAdapter);
//        mBinding.recyclerView.addFooter();

        mBinding.swipe.setColorSchemeResources(R.color.swipe_color_red, R.color.swipe_color_yellow, R.color
                .swipe_color_blue);
        mBinding.swipe.setOnRefreshListener(this);

        mPresenter.requestRecommendList(1, 5);

        return mBinding.getRoot();
    }

    @Override
    protected RecommendPresenter createPresent() {
        return mPresenter;
    }

    @Override
    public LifecycleTransformer getBindToLifecycle() {
        return bindToLifecycle();
    }

    @Override
    public void showError(String error) {
        Timber.e(error);
    }

    @Override
    public void loadRecommendList(List<FactoryInfo> recommendList) {
        mAdapter.setData(recommendList);
        mBinding.recyclerView.notifyDataSetChanged();
    }

    @Override
    public void cancelLoading() {
        mBinding.swipe.setRefreshing(false);
    }

    @Override
    public void startLoading() {
        mBinding.swipe.setRefreshing(true);
    }

    @Override
    public void onRefresh() {
        mPresenter.requestRecommendList(1, 5);
    }
}
