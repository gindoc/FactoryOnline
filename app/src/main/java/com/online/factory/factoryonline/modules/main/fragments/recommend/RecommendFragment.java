package com.online.factory.factoryonline.modules.main.fragments.recommend;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.base.BaseFragment;
import com.online.factory.factoryonline.customview.CustomPopupWindow;
import com.online.factory.factoryonline.customview.recyclerview.BaseRecyclerViewAdapter;
import com.online.factory.factoryonline.customview.recyclerview.OnPageListener;
import com.online.factory.factoryonline.databinding.FragmentRecommendBinding;
import com.online.factory.factoryonline.databinding.LayoutRecommendFilterDistrictBinding;
import com.online.factory.factoryonline.models.FactoryInfo;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Created by louiszgm on 2016/9/30.
 */
public class RecommendFragment extends BaseFragment<RecommendContract.View, RecommendPresenter> implements
        RecommendContract.View, SwipeRefreshLayout.OnRefreshListener, OnPageListener, BaseRecyclerViewAdapter.OnItemClickListener {

    private FragmentRecommendBinding mBinding;
    private LayoutRecommendFilterDistrictBinding mDistrictBinding;

    @Inject
    RecommendRecyclerViewAdapter mAdapter;  //推荐列表适配器

    @Inject
    RecommendCategoryAdapter mCategoryAdapter;  //推荐页面一级目录适配器

    @Inject
    RecommendCategoryAdapter mSecondCategoryAdapter;    //推荐页面二级目录适配器

    @Inject
    RecommendPresenter mPresenter;

    private CustomPopupWindow mCustomPopupWindow;
    private int pageNo = 1;
    private int pageSize = 5;
    private boolean isInit = true;      //true为下拉加载或初始化，false为上拉刷新
    private Map<String, List<String>> mCategories;

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
        mDistrictBinding = LayoutRecommendFilterDistrictBinding.inflate(inflater);

        mBinding.setView(this);

        mCustomPopupWindow = new CustomPopupWindow(mDistrictBinding.getRoot());

        mBinding.recyclerView.setAdapter(mAdapter);
        mBinding.recyclerView.setPageFooter(inflater.inflate(R.layout.layout_recyclerview_footer,
                container, false));
        mBinding.recyclerView.setOnPageListener(this);

        mDistrictBinding.recyclerviewFirstCat.setAdapter(mCategoryAdapter);
        mCategoryAdapter.setOnItemClickListener(this);
        mDistrictBinding.recyclerviewSecondCat.setAdapter(mSecondCategoryAdapter);

        mBinding.swipe.setColorSchemeResources(R.color.swipe_color_red, R.color.swipe_color_yellow, R.color
                .swipe_color_blue);
        mBinding.swipe.setOnRefreshListener(this);

        mPresenter.requestRecommendList(pageNo, pageSize, isInit);
        mPresenter.requestRcommendCat();

        return mBinding.getRoot();
    }

    @Override
    protected RecommendPresenter createPresent() {
        return mPresenter;
    }


    @Override
    public <T> LifecycleTransformer<T> getBindToLifecycle() {
        return bindToLifecycle();
    }

    @Override
    public void showError(String error) {
        Timber.e(error);
    }

    @Override
    public void loadRecommendList(List<FactoryInfo> recommendList) {
        if (isInit) {
            mAdapter.setData(recommendList);
        }else {
            mAdapter.addData(recommendList);
        }
        mBinding.recyclerView.notifyDataSetChanged();
    }

    @Override
    public void cancelLoading() {
        mBinding.swipe.setRefreshing(false);
        mBinding.recyclerView.hideLoadingFooter();
    }

    @Override
    public void startLoading() {
        mBinding.swipe.setRefreshing(true);
    }

    @Override
    public void loadRecommendCategories(Map<String, List<String>> cats) {
        mCategories = cats;
        List<String> firstCat = new ArrayList<>();
        for (Map.Entry<String, List<String>> fir : cats.entrySet()) {
            firstCat.add(fir.getKey());
        }
        mCategoryAdapter.setData(firstCat);
        if (firstCat.size() > 0) {
            mSecondCategoryAdapter.setData(cats.get(firstCat.get(0)));
        }
//        mDistrictBinding.recyclerviewFirstCat.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        pageNo = 1;
        isInit = true;
        mPresenter.requestRecommendList(pageNo, pageSize, isInit);
    }

    @Override
    public void onPage() {
        isInit = false;
        mBinding.recyclerView.showLoadingFooter();
        mPresenter.requestRecommendList(++pageNo, pageSize, isInit);
    }

    public void clickCategory() {
        mCustomPopupWindow.setDarkColor(Color.parseColor("#a0000000"));
        mCustomPopupWindow.resetDarkPosition();
        mCustomPopupWindow.darkBelow(mBinding.llFRecommendCat);
        mCustomPopupWindow.showAsDropDown(mBinding.llFRecommendCat);
    }

    @Override
    public void onItemClick(View view, int position) {
        String key = mCategoryAdapter.getData().get(position);
        List<String> sec = mCategories.get(key);
        mSecondCategoryAdapter.setData(sec);
        mDistrictBinding.recyclerviewSecondCat.notifyDataSetChanged();

    }
}
