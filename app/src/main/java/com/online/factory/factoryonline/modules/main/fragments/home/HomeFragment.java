package com.online.factory.factoryonline.modules.main.fragments.home;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.base.BaseFragment;
import com.online.factory.factoryonline.customview.DividerItemDecoration;
import com.online.factory.factoryonline.customview.recyclerview.BaseRecyclerViewAdapter;
import com.online.factory.factoryonline.data.remote.FactoryApi;
import com.online.factory.factoryonline.databinding.FragmentFindBinding;
import com.online.factory.factoryonline.databinding.FragmentHomeBinding;
import com.online.factory.factoryonline.databinding.LayoutHomeHeaderBinding;
import com.online.factory.factoryonline.models.Factory;
import com.online.factory.factoryonline.models.News;
import com.online.factory.factoryonline.modules.FactoryDetail.FactoryDetailActivity;
import com.online.factory.factoryonline.modules.baidumap.BaiduMapActivity;
import com.online.factory.factoryonline.modules.publishRental.PublishRentalActivity;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Created by cwenhui on 2016.02.23
 */
public class HomeFragment extends BaseFragment<HomeContract.View, HomePresenter> implements HomeContract
        .View, HomeRecyclerView.ScrollChangedListener, BaseRecyclerViewAdapter.OnItemClickListener {


    public static float MAX_SCALE_RATE = 0.3f;
    public static float MAX_TRANSLATIONY = 200;
    public  float MAX_TOP;
    private FragmentHomeBinding mBinding;
    private LayoutHomeHeaderBinding mHeaderBinding;
    private FragmentFindBinding mFindBinding;
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
        mBinding.recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));
        mBinding.recyclerView.setScrollChangedListener(this);
        mBinding.recyclerView.addHeader(mHeaderBinding.getRoot());
        mBinding.recyclerView.init();
        mAdapter.setOnItemClickListener(this);

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
        mFindBinding = FragmentFindBinding.inflate(LayoutInflater.from(getContext()), mHeaderBinding.rolePick, true);
        mFindBinding.setView(this);

    }

    /**
     * 切换rolePick为“我是中介”
     */
    public void getCommission() {
        mHeaderBinding.rolePick.removeAllViews();
        LayoutInflater.from(getActivity()).inflate(R.layout.fragment_commission, mHeaderBinding.rolePick);
    }

    /**
     * 切换rolePick为“我是业主”
     */
    public void isOwner() {
        mHeaderBinding.rolePick.removeAllViews();
        LayoutInflater.from(getActivity()).inflate(R.layout.fragment_owner, mHeaderBinding.rolePick);
    }

    public void publishRental() {
        Activity activity = getActivity();
        Intent intent = new Intent(activity, PublishRentalActivity.class);
        startActivity(intent);
        activity.overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
    }

    public void useMap() {
        startActivity(BaiduMapActivity.getStartIntent(getActivity()));
        getActivity().overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
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
    public <T>LifecycleTransformer<T> getBindToLifecycle() {
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
    public void initRecyclerView(List<Factory> infos) {
        mAdapter.setData(infos);
        mBinding.recyclerView.notifyDataSetChanged();
    }

    @Override
    public void onScrolled(int dy,boolean isSwipeDown) {
        Timber.d("dy %d",dy);
        int limit = 300;
        int i = dy % 300;
        float percentage = (float) Math.abs(i) / (float) limit;
        Timber.d("percentage : %f",percentage);
        float scale = (float) (percentage*MAX_SCALE_RATE);


        int height = mBinding.coverView.getHeight();
        float translationY = -percentage*(height-50);

        Timber.d("scale : %f",scale);
        Timber.d("translationY %f",translationY);

            if(Math.abs(dy) < limit){
                mBinding.coverView.setAlpha(percentage);
                ObjectAnimator
                        .ofFloat(mBinding.searchview, "scaleX", 1-scale)
                        .setDuration(limit / 700)
                        .start();
                ObjectAnimator.ofFloat(mBinding.searchview, "translationY", translationY)
                        .setDuration(limit / 700)
                        .start();
            }else {

            }



        Timber.d("height %f",mBinding.searchview.getY());
        Timber.d("actual scalX %f",mBinding.searchview.getScaleX());
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent();
        intent.setClass(getContext(), FactoryDetailActivity.class);
        Factory factory = mAdapter.getData().get(position);
        intent.putExtra(FactoryDetailActivity.FACTORY_DETIAL, factory);
        startActivity(intent);
    }
}
