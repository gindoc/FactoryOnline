package com.online.factory.factoryonline.modules.main.fragments.home.index;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.base.BaseFragment;
import com.online.factory.factoryonline.customview.DividerItemDecoration;
import com.online.factory.factoryonline.customview.FullyLinearLayoutManager;
import com.online.factory.factoryonline.customview.recyclerview.BaseRecyclerViewAdapter;
import com.online.factory.factoryonline.databinding.FragmentIndexBinding;
import com.online.factory.factoryonline.databinding.ItemHighQualityFactoryBinding;
import com.online.factory.factoryonline.models.WantedMessage;
import com.online.factory.factoryonline.modules.FactoryDetail.FactoryDetailActivity;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * 作者: GIndoc
 * 日期: 2017/1/11 15:59
 * 作用:
 */

public class IndexFragment extends BaseFragment<IndexContract.View, IndexPresenter> implements IndexContract.View, BaseRecyclerViewAdapter.OnItemClickListener{
    private FragmentIndexBinding mBinding;

    @Inject
    IndexRecyclerViewAdapter mAdapter;

    @Inject
    Provider<IndexViewModel> provider;

    @Inject
    IndexPresenter mPresenter;

    @Inject
    public IndexFragment() {
        setTitle("首页");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        getComponent().inject(this);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentIndexBinding.inflate(inflater);
        mBinding.setPresenter(mPresenter);
        mBinding.setView(this);

        initRecyclerView();

        mPresenter.requestIndexPicUrls();
        mPresenter.requestWantedMessages();
        mPresenter.requestHighQualityFactory();

        return mBinding.getRoot();
    }

    private void initRecyclerView() {
        FullyLinearLayoutManager fullyLinearLayoutManager = new FullyLinearLayoutManager(getContext());
        fullyLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mBinding.recyclerView.setLayoutManager(fullyLinearLayoutManager);
        mBinding.recyclerView.setAdapter(mAdapter);
        mBinding.recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));
        mAdapter.setOnItemClickListener(this);
        mBinding.llEmptyView.setVisibility(View.VISIBLE);
    }

    @Override
    protected IndexPresenter createPresent() {
        return mPresenter;
    }

    @Override
    public <T> LifecycleTransformer<T> getBindToLifecycle() {
        return bindToLifecycle();
    }

    @Override
    public void showError(String error) {

    }

    @Override
    public void onResume() {
        super.onResume();
        mBinding.slideShowView.startPlay();
    }

    @Override
    public void onPause() {
        super.onPause();
        mBinding.slideShowView.stopPlay();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent();
        intent.setClass(getContext(), FactoryDetailActivity.class);
        intent.putExtra(FactoryDetailActivity.WANTED_MESSAGE, mAdapter.getData().get(position));
        startActivity(intent);
    }

    @Override
    public void initSlideShowView(String[] urls) {
        mBinding.slideShowView.setImageUrls(urls);
    }

    @Override
    public void loadWantedMessages(List<WantedMessage> wantedMessages) {
        mAdapter.setData(wantedMessages);
        mBinding.recyclerView.notifyDataSetChanged();
        if (wantedMessages.size() > 0) {
            mBinding.llEmptyView.setVisibility(View.GONE);
        }
    }

    @Override
    public void loadHighQualityFactory(List<WantedMessage> wantedMessages) {
        mBinding.llHighQualityDots.removeAllViews();
        List<View> highQualityItem = new ArrayList<>();
        List<View> dotViewsList = new ArrayList<>();
        for (int i=0;i<wantedMessages.size();i++) {
            WantedMessage w = wantedMessages.get(i);
            ItemHighQualityFactoryBinding binding = ItemHighQualityFactoryBinding.inflate(LayoutInflater.from(getContext()),
                    mBinding.highQualityViewpager, false);
            IndexViewModel model = provider.get();
            model.setFactoryInfo(w.getFactory());
            binding.setViewModel(model);
            View view = binding.getRoot();
            view.setTag(w);
            highQualityItem.add(view);

            ImageView dotView = new ImageView(getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.leftMargin = 4;
            params.rightMargin = 4;
            int size = getResources().getDimensionPixelOffset(R.dimen.x8);
            params.width = params.height = size;
            mBinding.llHighQualityDots.addView(dotView, params);
            dotViewsList.add(dotView);
            if (i == 0) {
                dotView.setBackgroundResource(R.drawable.hight_quality_selected_dot);
            }else {
                dotView.setBackgroundResource(R.drawable.gray_oval_background);
            }
        }
        initHighQualityViewPager(highQualityItem, dotViewsList);
    }

    private void initHighQualityViewPager(final List<View> highQualityItem, final List<View> dotViewsList) {
        PagerAdapter pagerAdapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return highQualityItem.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(highQualityItem.get(position));
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View view = highQualityItem.get(position);
                container.addView(view);
                return view;
            }
        };

        ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
            boolean isAutoPlay = false;
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int pos) {
                for(int i=0;i < dotViewsList.size();i++){
                    if(i == pos){
                        (dotViewsList.get(pos)).setBackgroundResource(R.drawable.hight_quality_selected_dot);
                    }else {
                        (dotViewsList.get(i)).setBackgroundResource(R.drawable.gray_oval_background);
                    }
                }
                mCurrentItem = pos;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state) {
                    case 1:// 手势滑动，空闲中
                        isAutoPlay = false;
                        break;
                    case 2:// 界面切换中
                        isAutoPlay = true;
                        break;
                    case 0:// 滑动结束，即切换完毕或者加载完毕
                        // 当前为最后一张，此时从右向左滑，则切换到第一张
                        if (mBinding.highQualityViewpager.getCurrentItem() == mBinding.highQualityViewpager.getAdapter().getCount() - 1 && !isAutoPlay) {
                            mBinding.highQualityViewpager.setCurrentItem(0);
                        }
                        // 当前为第一张，此时从左向右滑，则切换到最后一张
                        else if (mBinding.highQualityViewpager.getCurrentItem() == 0 && !isAutoPlay) {
                            mBinding.highQualityViewpager.setCurrentItem(mBinding.highQualityViewpager.getAdapter().getCount() - 1);
                        }
                        break;
                }
            }
        };
        mBinding.highQualityViewpager.setAdapter(pagerAdapter);
        mBinding.highQualityViewpager.addOnPageChangeListener(pageChangeListener);
        handler.sendEmptyMessageDelayed(0, 3000);
    }

    private int mCurrentItem;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mBinding.highQualityViewpager.setCurrentItem(mCurrentItem);
            mCurrentItem = (mCurrentItem+1)%mBinding.llHighQualityDots.getChildCount();
            sendEmptyMessageDelayed(0, 3000);
        }
    };
}