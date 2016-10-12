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
import com.online.factory.factoryonline.customview.DividerItemDecoration;
import com.online.factory.factoryonline.customview.recyclerview.BaseRecyclerViewAdapter;
import com.online.factory.factoryonline.customview.recyclerview.OnPageListener;
import com.online.factory.factoryonline.databinding.FragmentRecommendBinding;
import com.online.factory.factoryonline.databinding.LayoutRecommendFilterDistrictBinding;
import com.online.factory.factoryonline.databinding.LayoutRecommendFilterPriceAreaBinding;
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
    private LayoutRecommendFilterPriceAreaBinding mPriceAreaBinding;

    @Inject
    RecommendRecyclerViewAdapter mAdapter;  //推荐列表适配器

    @Inject
    RecommendCategoryAdapter mDistrictFirCategoryAdapter;  //推荐页面区域一级目录适配器

    @Inject
    RecommendCategoryAdapter mDistrictSecCategoryAdapter;    //推荐页面区域二级目录适配器

    @Inject
    RecommendCategoryAdapter mPriceCategoryAdapter;           //推荐页面的价格目录

    @Inject
    RecommendPresenter mPresenter;

    private CustomPopupWindow mDistrictPopupWindow;     //区域目录的popupWindow
    private CustomPopupWindow mPricePopupWindow;         //价格目录的popupWindow
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
        mPriceAreaBinding = LayoutRecommendFilterPriceAreaBinding.inflate(inflater);

        mBinding.setView(this);

        mDistrictPopupWindow = new CustomPopupWindow(mDistrictBinding.getRoot());     //初始化区域的popupWindow
        mPricePopupWindow = new CustomPopupWindow(mPriceAreaBinding.getRoot());       //初始化价格的popupWindow

        mBinding.recyclerView.setAdapter(mAdapter);                                   //初始化推荐列表
        mBinding.recyclerView.setPageFooter(inflater.inflate(R.layout.layout_recyclerview_footer,
                container, false));
        mBinding.recyclerView.setOnPageListener(this);

        mDistrictBinding.recyclerviewFirstCat.setAdapter(mDistrictFirCategoryAdapter);         //初始化推荐页面的一级目录
        mDistrictBinding.recyclerviewFirstCat.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));
        mDistrictFirCategoryAdapter.setOnItemClickListener(this);

        mDistrictBinding.recyclerviewSecondCat.setAdapter(mDistrictSecCategoryAdapter);  //初始化推荐页面的二级目录
        mDistrictBinding.recyclerviewSecondCat.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));

        mPriceAreaBinding.recyclerView.setAdapter(mPriceCategoryAdapter);                  //初始化推荐页面的价格目录
        mPriceAreaBinding.recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));

        mBinding.swipe.setColorSchemeResources(R.color.swipe_color_red, R.color.swipe_color_yellow, R.color
                .swipe_color_blue);                                                      //初始化swipeRefleshLayout
        mBinding.swipe.setOnRefreshListener(this);

        mPresenter.requestRecommendList(pageNo, pageSize, isInit);                      // 请求推荐列表
        mPresenter.requestDistrictCategories();                                           //请求推荐页面的目录
        mPresenter.requestPriceCategories();                                              //请求推荐页面的价格目录

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
        mCategories = cats;
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
        mPriceAreaBinding.recyclerView.notifyDataSetChanged();

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

    public void clickDistrictCategory() {
        showPopupWindow(mDistrictPopupWindow);
    }

    private void showPopupWindow(CustomPopupWindow popupWindow) {
        popupWindow.setDarkColor(Color.parseColor("#a0000000"));
        popupWindow.resetDarkPosition();
        popupWindow.darkBelow(mBinding.llFRecommendCat);
        popupWindow.showAsDropDown(mBinding.llFRecommendCat);
    }

    public void clickPriceCategory() {
        showPopupWindow(mPricePopupWindow);
    }

    public void clickAreaCategory() {

    }

    @Override
    public void onItemClick(View view, int position) {
        String key = mDistrictFirCategoryAdapter.getData().get(position);
        List<String> sec = mCategories.get(key);
        mDistrictSecCategoryAdapter.setData(sec);
        mDistrictBinding.recyclerviewSecondCat.notifyDataSetChanged();
    }

}
