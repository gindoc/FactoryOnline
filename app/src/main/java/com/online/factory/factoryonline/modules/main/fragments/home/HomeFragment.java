package com.online.factory.factoryonline.modules.main.fragments.home;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.base.BaseFragment;
import com.online.factory.factoryonline.data.remote.FactoryApi;
import com.online.factory.factoryonline.databinding.FragmentHomeBinding;
import com.online.factory.factoryonline.databinding.LayoutHomeHeaderBinding;
import com.online.factory.factoryonline.models.FactoryInfo;
import com.online.factory.factoryonline.models.News;
import com.trello.rxlifecycle.LifecycleTransformer;

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
    @Inject
    HomeRecyclerViewAdapter mAdapter;

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
        mHeaderBinding.setView(this);

        mBinding.recyclerView.setAdapter(mAdapter);
        mBinding.recyclerView.setScrollChangedListener(this);
        mBinding.recyclerView.addHeader(mHeaderBinding.getRoot());
        mBinding.recyclerView.init();

        findFactory();
        mHeaderBinding.rbFind.setChecked(true); //设置“找房”为选中状态

        mPresenter.requestIndexPicUrls();
        mPresenter.requestScrollMsg();
        mPresenter.requestFactoryInfo();

        return mBinding.getRoot();
    }

    /**
     * 切换rolePick为“找房”
     */
    public void findFactory() {
        mHeaderBinding.rolePick.removeAllViews();
        LayoutInflater.from(getActivity()).inflate(R.layout.fragment_find, mHeaderBinding.rolePick);
    }

    /**
     * 切换rolePick为“我是中介”
     */
    public void isAgency() {
        mHeaderBinding.rolePick.removeAllViews();
        LayoutInflater.from(getActivity()).inflate(R.layout.fragment_agency, mHeaderBinding.rolePick);
    }

    /**
     * 切换rolePick为“我是业主”
     */
    public void isOwner() {
        mHeaderBinding.rolePick.removeAllViews();
        LayoutInflater.from(getActivity()).inflate(R.layout.fragment_owner, mHeaderBinding.rolePick);
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
        Timber.e(error);
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
    public void initRecyclerView(List<FactoryInfo> infos) {
        mAdapter.setData(infos);
        mBinding.recyclerView.notifyDataSetChanged();
    }

    @Override
    public void onScrolled(int dy) {
        Timber.e("dy:%d" , dy);
        int limit = mBinding.searchview.getHeight();
        mBinding.coverView.setAlpha(dy / 100f);
        if (limit > 0) {
            if (dy <= limit) {
                Timber.e("1 - dy / (3*limit): %f" , (1 - dy * 1.0f / (3 * limit)));
                ObjectAnimator
                        .ofFloat(mBinding.searchview, "scaleX", 1 - dy * 1.0f / (3 * limit))
                        .setDuration(limit / 700)
                        .start();
                ObjectAnimator.ofFloat(mBinding.searchview, "translationY", -limit / 100 * dy)
                        .setDuration(limit / 700)
                        .start();
                Timber.e("translationY   : %d" , -limit / 100 * dy);
            }
        }
    }
}
