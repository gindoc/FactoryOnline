package com.online.factory.factoryonline.modules.main.fragments.recommend;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.base.BaseFragment;
import com.online.factory.factoryonline.customview.DividerItemDecoration;
import com.online.factory.factoryonline.customview.recyclerview.BaseRecyclerViewAdapter;
import com.online.factory.factoryonline.customview.recyclerview.OnPageListener;
import com.online.factory.factoryonline.databinding.FragmentRecommendBinding;
import com.online.factory.factoryonline.databinding.LayoutRecommendFilterDistrictBinding;
import com.online.factory.factoryonline.databinding.LayoutRecommendFilterPriceAreaBinding;
import com.online.factory.factoryonline.models.Area;
import com.online.factory.factoryonline.models.Factory;
import com.online.factory.factoryonline.models.WantedMessage;
import com.online.factory.factoryonline.modules.FactoryDetail.FactoryDetailActivity;
import com.online.factory.factoryonline.modules.baidumap.BaiduMapActivity;
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
    RecommendAreaCategoryAdapter mDistrictSecCategoryAdapter;    //推荐页面区域二级目录适配器

    @Inject
    RecommendWhiteCategoryAdapter mPriceCategoryAdapter;           //推荐页面的价格目录

    @Inject
    RecommendWhiteCategoryAdapter mAreaCategoryAdapter;

    @Inject
    RecommendPresenter mPresenter;

    private int downPage = 1;
    private int upPage = 1;
    private Map<String, List<Area>> mDistrictCategories;
    private List<WantedMessage> wantedMessages = new ArrayList<>();
    private List<Integer> ids = new ArrayList<>();

    @Inject
    public RecommendFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        getComponent().inject(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        downPage = 1;
        upPage = 1;
        mBinding = FragmentRecommendBinding.inflate(inflater);
        mDistrictBinding = LayoutRecommendFilterDistrictBinding.inflate(inflater);
        mPriceBinding = LayoutRecommendFilterPriceAreaBinding.inflate(inflater);
        mAreaBinding = LayoutRecommendFilterPriceAreaBinding.inflate(inflater);

        mBinding.setView(this);
        mPriceBinding.setView(this);
        mAreaBinding.setView(this);

        initRecyclerView(inflater, container);

        initDropDown();

        initSwipeLayout();

        requestRecommendMsg();

        mPriceBinding.confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkParams(mPriceBinding);
            }
        });

        mAreaBinding.confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkParams(mAreaBinding);
            }
        });

        ViewTreeObserver viewTreeObserver = getActivity().getWindow().getDecorView().getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int height = mBinding.dropDownMenu.getChildAt(0).getHeight();
                FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                lp.setMargins(0, height, 0, 0);
                mBinding.swipe.setLayoutParams(lp);
            }
        });
        return mBinding.getRoot();
    }

    public void openMap(View view) {
        startActivity(BaiduMapActivity.getStartIntent(getActivity()));
        getActivity().overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
    }

    private void initRecyclerView(LayoutInflater inflater, @Nullable ViewGroup container) {
        mBinding.recyclerView.setAdapter(mAdapter);                                   //初始化推荐列表
        mBinding.recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));
        mBinding.recyclerView.setPageFooter(inflater.inflate(R.layout.layout_recyclerview_footer, container, false));
        mBinding.recyclerView.setOnPageListener(this);
        mAdapter.setOnItemClickListener(this);

        mDistrictBinding.recyclerviewFirstCat.setAdapter(mDistrictFirCategoryAdapter);         //初始化推荐页面的一级目录
        mDistrictFirCategoryAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String key = mDistrictFirCategoryAdapter.getData().get(position);
                List<Area> sec = mDistrictCategories.get(key);
                mDistrictSecCategoryAdapter.setData(sec);
                mDistrictBinding.recyclerviewSecondCat.notifyDataSetChanged();
                mDistrictFirCategoryAdapter.getSubject().onNext(position);
            }
        });

        mDistrictBinding.recyclerviewSecondCat.setAdapter(mDistrictSecCategoryAdapter);  //初始化推荐页面的二级目录
        mDistrictSecCategoryAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mDistrictSecCategoryAdapter.getSubject().onNext(position);
                String title = mDistrictSecCategoryAdapter.getData().get(position).getName();
                mBinding.dropDownMenu.setTabText(title);
                mBinding.dropDownMenu.closeMenu();
            }
        });

        mPriceBinding.recyclerView.setAdapter(mPriceCategoryAdapter);                  //初始化推荐页面的价格目录
        mPriceCategoryAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                onItemClickAction(mPriceCategoryAdapter, position);
            }
        });

        mAreaBinding.recyclerView.setAdapter(mAreaCategoryAdapter);
        mAreaCategoryAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                onItemClickAction(mAreaCategoryAdapter, position);
            }
        });
    }

    private void onItemClickAction(RecommendWhiteCategoryAdapter adapter, int position) {
        adapter.getSubject().onNext(position);
        String title = adapter.getData().get(position);
        mBinding.dropDownMenu.setTabText(title);
        mBinding.dropDownMenu.closeMenu();
    }

    private void initDropDown() {
        String headers[] = {"区域", "价格", "面积"};
        List<View> popViews = new ArrayList<>();
        popViews.add(mDistrictBinding.getRoot());
        popViews.add(mPriceBinding.getRoot());
        popViews.add(mAreaBinding.getRoot());
        mBinding.dropDownMenu.setDropDownMenu(Arrays.asList(headers), popViews);
        mDistrictFirCategoryAdapter.getSubject().onNext(0);
        mDistrictSecCategoryAdapter.getSubject().onNext(0);
        mPriceCategoryAdapter.getSubject().onNext(0);
        mAreaCategoryAdapter.getSubject().onNext(0);
    }

    private void initSwipeLayout() {
        mBinding.swipe.setColorSchemeResources(R.color.swipe_color_red, R.color.swipe_color_yellow, R.color
                .swipe_color_blue);                                                      //初始化swipeRefleshLayout
        mBinding.swipe.setProgressViewEndTarget(true, 300);
        mBinding.swipe.setOnRefreshListener(this);
    }

    private void requestRecommendMsg() {
        mPresenter.initRecommendList();                                                     // 请求推荐列表
        mPresenter.requestDistrictCategories();                                           //请求推荐页面的目录
        mPresenter.requestPriceCategories();                                              //请求推荐页面的价格目录
        mPresenter.requestAreaCategories();
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
        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loadRecommendList(List<WantedMessage> recommendList, boolean action) {
        List<Factory> factories = new ArrayList<Factory>();
        if (action) {
            for (WantedMessage wantedMessage : recommendList) {
                factories.add(wantedMessage.getFactory());
                ids.add(Integer.parseInt(wantedMessage.getId()));
            }
            downPage++;
            wantedMessages.addAll(0, recommendList);
            mAdapter.getData().addAll(0, factories);
        } else {
            for (WantedMessage wantedMessage : recommendList) {
                factories.add(wantedMessage.getFactory());
            }
            upPage++;
            wantedMessages.addAll(recommendList);
            mAdapter.getData().addAll(factories);
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
    public void loadRecommendDistrictCategories(Map<String, List<Area>> cats) {
        mDistrictCategories = cats;
        List<String> firstCat = new ArrayList<>();
        for (Map.Entry<String, List<Area>> fir : cats.entrySet()) {
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
        mPresenter.requestRecommendListByNet(downPage, 0, 0, 0, 0);
    }

    @Override
    public void onPage() {
        mBinding.recyclerView.showLoadingFooter();
//        mPresenter.requestRecommendListByDB(upPage);
        mPresenter.requestRecommendListByDBWithoutIds(upPage, ids);
    }

    private void hideKeyboard() {
        Activity activity = getActivity();
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        // 隐藏软键盘
        imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
    }

    private void checkParams(LayoutRecommendFilterPriceAreaBinding binding) {
        CharSequence max = binding.maximum.getText();
        CharSequence min = binding.minimum.getText();
        if (TextUtils.isEmpty(min)) {
            Toast.makeText(getContext(), "请填写最小值", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(max)){
            Toast.makeText(getContext(), "请填写最大值", Toast.LENGTH_SHORT).show();
            return;
        }
        if (Integer.parseInt(max.toString()) < Integer.parseInt(min.toString())) {
            Toast.makeText(getContext(), "请输入正确的值（最大值不能小于最小值）", Toast.LENGTH_SHORT).show();
            return;
        }
        mBinding.dropDownMenu.setTabText(min+"~"+max);
        mBinding.dropDownMenu.closeMenu();
        hideKeyboard();
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(getContext(), FactoryDetailActivity.class);
        WantedMessage wantedMessage = wantedMessages.get(position);
        intent.putExtra(FactoryDetailActivity.WANTED_MESSAGE, wantedMessage);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
    }
}
