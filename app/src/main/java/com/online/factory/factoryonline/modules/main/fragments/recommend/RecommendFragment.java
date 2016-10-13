package com.online.factory.factoryonline.modules.main.fragments.recommend;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.base.BaseFragment;
import com.online.factory.factoryonline.customview.DividerItemDecoration;
import com.online.factory.factoryonline.customview.recyclerview.BaseRecyclerViewAdapter;
import com.online.factory.factoryonline.customview.recyclerview.OnPageListener;
import com.online.factory.factoryonline.databinding.FragmentRecommendBinding;
import com.online.factory.factoryonline.databinding.LayoutRecommendFilterDistrictBinding;
import com.online.factory.factoryonline.databinding.LayoutRecommendFilterPriceAreaBinding;
import com.online.factory.factoryonline.models.FactoryInfo;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.util.ArrayList;
import java.util.Arrays;
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
    private LayoutRecommendFilterPriceAreaBinding mPriceBinding;
    private LayoutRecommendFilterPriceAreaBinding mAreaBinding;

    @Inject
    RecommendRecyclerViewAdapter mAdapter;  //推荐列表适配器

    @Inject
    RecommendCategoryAdapter mDistrictFirCategoryAdapter;  //推荐页面区域一级目录适配器

    @Inject
    RecommendCategoryAdapter mDistrictSecCategoryAdapter;    //推荐页面区域二级目录适配器

    @Inject
    RecommendCategoryAdapter mPriceCategoryAdapter;           //推荐页面的价格目录

    @Inject
    RecommendCategoryAdapter mAreaCategoryAdapter;

    @Inject
    RecommendPresenter mPresenter;

    private int pageNo = 1;
    private int pageSize = 5;
    private boolean isInit = true;      //true为下拉加载或初始化，false为上拉刷新
    private Map<String, List<String>> mDistrictCategories;

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
        mPriceBinding = LayoutRecommendFilterPriceAreaBinding.inflate(inflater);
        mAreaBinding = LayoutRecommendFilterPriceAreaBinding.inflate(inflater);

        mBinding.setView(this);

        mBinding.recyclerView.setAdapter(mAdapter);                                   //初始化推荐列表
        mBinding.recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));
        mBinding.recyclerView.setPageFooter(inflater.inflate(R.layout.layout_recyclerview_footer,
                container, false));
        mBinding.recyclerView.setOnPageListener(this);

        mDistrictBinding.recyclerviewFirstCat.setAdapter(mDistrictFirCategoryAdapter);         //初始化推荐页面的一级目录
        mDistrictFirCategoryAdapter.setOnItemClickListener(this);

        mDistrictBinding.recyclerviewSecondCat.setAdapter(mDistrictSecCategoryAdapter);  //初始化推荐页面的二级目录

        mPriceBinding.recyclerView.setAdapter(mPriceCategoryAdapter);                  //初始化推荐页面的价格目录

        mAreaBinding.recyclerView.setAdapter(mAreaCategoryAdapter);

        String headers[] = {"区域", "面积", "价格"};
        List<View> popViews = new ArrayList<>();
        popViews.add(mDistrictBinding.getRoot());
        popViews.add(mPriceBinding.getRoot());
        popViews.add(mAreaBinding.getRoot());
        mBinding.dropDownMenu.setDropDownMenu(Arrays.asList(headers), popViews, new TextView(getContext()));

        mBinding.swipe.setColorSchemeResources(R.color.swipe_color_red, R.color.swipe_color_yellow, R.color
                .swipe_color_blue);                                                      //初始化swipeRefleshLayout
        mBinding.swipe.setOnRefreshListener(this);

        mPresenter.requestRecommendList(pageNo, pageSize, isInit);                      // 请求推荐列表
        mPresenter.requestDistrictCategories();                                           //请求推荐页面的目录
        mPresenter.requestPriceCategories();                                              //请求推荐页面的价格目录
        mPresenter.requestAreaCategories();

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
        } else {
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
    public void loadRecommendDistrictCategories(Map<String, List<String>> cats) {
        mDistrictCategories = cats;
        List<String> firstCat = new ArrayList<>();
        for (Map.Entry<String, List<String>> fir : cats.entrySet()) {
            firstCat.add(fir.getKey());
        }
        mDistrictFirCategoryAdapter.setData(firstCat);
        if (firstCat.size() > 0) {
            mDistrictSecCategoryAdapter.setData(cats.get(firstCat.get(0)));
        }
    }

    @Override
    public void loadRecommendPriceCategories(List<String> cats) {
        mPriceCategoryAdapter.setData(cats);
        mPriceBinding.recyclerView.notifyDataSetChanged();

    }

    @Override
    public void loadRecommendAreaCategories(List<String> area) {
        mAreaCategoryAdapter.setData(area);
        mAreaBinding.recyclerView.notifyDataSetChanged();
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

    @Override
    public void onItemClick(View view, int position) {
        String key = mDistrictFirCategoryAdapter.getData().get(position);
        List<String> sec = mDistrictCategories.get(key);
        mDistrictSecCategoryAdapter.setData(sec);
        mDistrictBinding.recyclerviewSecondCat.notifyDataSetChanged();
    }

}
