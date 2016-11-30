package com.online.factory.factoryonline.modules.main.fragments.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.base.BaseFragment;
import com.online.factory.factoryonline.customview.DividerItemDecoration;
import com.online.factory.factoryonline.customview.recyclerview.BaseRecyclerViewAdapter;
import com.online.factory.factoryonline.data.remote.FactoryApi;
import com.online.factory.factoryonline.databinding.FragmentHomeBinding;
import com.online.factory.factoryonline.databinding.FragmentOwnerBinding;
import com.online.factory.factoryonline.databinding.LayoutHomeHeaderBinding;
import com.online.factory.factoryonline.models.Factory;
import com.online.factory.factoryonline.models.News;
import com.online.factory.factoryonline.models.WantedMessage;
import com.online.factory.factoryonline.modules.FactoryDetail.FactoryDetailActivity;
import com.online.factory.factoryonline.modules.baidumap.BaiduMapActivity;
import com.online.factory.factoryonline.modules.city.CityActivity;
import com.online.factory.factoryonline.modules.locate.fragments.MyLocationListener;
import com.online.factory.factoryonline.modules.publishRental.PublishRentalActivity;
import com.online.factory.factoryonline.utils.rx.RxSubscriber;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.util.List;

import javax.inject.Inject;

import rx.subjects.BehaviorSubject;
import timber.log.Timber;

//import org.apache.commons.codec.binary.Base64;

/**
 * Created by cwenhui on 2016.02.23
 */
public class HomeFragment extends BaseFragment<HomeContract.View, HomePresenter> implements HomeContract
        .View, BaseRecyclerViewAdapter.OnItemClickListener {
    private FragmentHomeBinding mBinding;
    private LayoutHomeHeaderBinding mHeaderBinding;
    private FragmentOwnerBinding mOwnBinding;
    @Inject
    HomeRecyclerViewAdapter mAdapter;

    @Inject
    LocationClient locationClient;

    @Inject
    BehaviorSubject subject;

    @Inject
    BDLocationListener mBdLocationListener;

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

        locating();
    }

    private void locating() {
        ((MyLocationListener) mBdLocationListener).setBdLocationBehaviorSubject(subject);
        locationClient.registerLocationListener(mBdLocationListener);
        locationClient.start();            // 启动定位
        subject.compose(this.bindToLifecycle())
                .subscribe(new RxSubscriber<BDLocation>() {
                    @Override
                    public void _onNext(BDLocation bdLocation) {
                        Timber.d("定位成功");
                        mBinding.tvCity.setText(bdLocation.getCity());
                        if (locationClient.isStarted()) {
                            locationClient.stop();
                        }
                    }

                    @Override
                    public void _onError(Throwable throwable) {
                        Timber.e(throwable.getMessage());
                    }
                });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = FragmentHomeBinding.inflate(inflater);
        mHeaderBinding = LayoutHomeHeaderBinding.inflate(inflater);

        mBinding.setPresenter(mPresenter);
        mBinding.setView(this);
        mHeaderBinding.setView(this);

        initRecyclerView();

        findFactory();
        mHeaderBinding.rbFind.setChecked(true); //设置“找房”为选中状态

        mPresenter.requestIndexPicUrls();
        mPresenter.requestScrollMsg();
        mPresenter.requestWantedMessages();

        return mBinding.getRoot();
    }

    private void initRecyclerView() {
        mBinding.recyclerView.setAdapter(mAdapter);
        mBinding.recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));
        mBinding.recyclerView.addHeader(mHeaderBinding.getRoot());
        mAdapter.setOnItemClickListener(this);
    }

    /**
     * 切换rolePick为“找房”
     */
    public void findFactory() {
        mHeaderBinding.rolePick.removeAllViews();
        LayoutInflater.from(getContext()).inflate(R.layout.fragment_find, mHeaderBinding.rolePick);
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
        mOwnBinding = FragmentOwnerBinding.inflate(LayoutInflater.from(getContext()), mHeaderBinding.rolePick, true);
        mOwnBinding.setView(this);
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

    public void openCityPage() {
        startActivity(CityActivity.getStartIntent(getContext()));
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
        locationClient.unRegisterLocationListener(mBdLocationListener);
        super.onPause();
        mHeaderBinding.slideShowView.stopPlay();
        mHeaderBinding.scrollTxtView.stopAutoScroll();
    }

    @Override
    public void onDestroy() {
        locationClient.stop();
        super.onDestroy();
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
    public void initSlideShowView(String[] urls) {
        mHeaderBinding.slideShowView.setImageUrls(urls);
    }

    @Override
    public void initScrollTextView(List<News> newses) {
        mHeaderBinding.scrollTxtView.setNews(newses);
    }

    @Override
    public void loadWantedMessages(List<WantedMessage> wantedMessages) {
        mAdapter.setData(wantedMessages);
        mBinding.recyclerView.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent();
        intent.setClass(getContext(), FactoryDetailActivity.class);
        intent.putExtra(FactoryDetailActivity.WANTED_MESSAGE, mAdapter.getData().get(position));
        startActivity(intent);
    }
}
