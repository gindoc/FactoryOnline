package com.online.factory.factoryonline.modules.main.fragments.home;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.base.BaseFragment;
import com.online.factory.factoryonline.base.PermissionCallback;
import com.online.factory.factoryonline.customview.DividerItemDecoration;
import com.online.factory.factoryonline.customview.FullyLinearLayoutManager;
import com.online.factory.factoryonline.customview.recyclerview.BaseRecyclerViewAdapter;
import com.online.factory.factoryonline.databinding.FragmentHomeBinding;
import com.online.factory.factoryonline.models.News;
import com.online.factory.factoryonline.models.WantedMessage;
import com.online.factory.factoryonline.modules.FactoryDetail.FactoryDetailActivity;
import com.online.factory.factoryonline.modules.city.CityActivity;
import com.online.factory.factoryonline.modules.locate.fragments.MyLocationListener;
import com.online.factory.factoryonline.modules.main.fragments.home.agent.AgentFragment;
import com.online.factory.factoryonline.modules.main.fragments.home.factory.FactoryFragment;
import com.online.factory.factoryonline.modules.main.fragments.home.owner.OwnerFragment;
import com.online.factory.factoryonline.modules.main.fragments.recommend.RecommendFragment;
import com.online.factory.factoryonline.modules.search.SearchActivity;
import com.online.factory.factoryonline.utils.StatusBarUtils;
import com.online.factory.factoryonline.utils.rx.RxSubscriber;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.util.List;

import javax.inject.Inject;

import rx.subjects.BehaviorSubject;
import timber.log.Timber;


/**
 * Created by cwenhui on 2016.02.23
 */
public class HomeFragment extends BaseFragment<HomeContract.View, HomePresenter> implements HomeContract
        .View, BaseRecyclerViewAdapter.OnItemClickListener {
    public static final int PERMISSION_REQUEST_CODE = 199;
    private FragmentHomeBinding mBinding;

    @Inject
    HomeRecyclerViewAdapter mAdapter;

    @Inject
    LocationClient locationClient;

    @Inject
    BehaviorSubject subject;

    @Inject
    BDLocationListener mBdLocationListener;

    @Inject
    RecommendFragment recommendFragment;

    @Inject
    FactoryFragment factoryFragment;

    @Inject
    OwnerFragment ownerFragment;

    @Inject
    AgentFragment agentFragment;

    @Inject
    public HomeFragment() {
    }

    @Inject
    HomePresenter mPresenter;

    @Override
    protected HomePresenter createPresent() {
        return mPresenter;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        getComponent().inject(this);
        super.onCreate(savedInstanceState);

        checkPermission();

    }

    private void checkPermission() {
        performCodeWithPermission(getString(R.string.permission_location_rationale), PERMISSION_REQUEST_CODE,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, new PermissionCallback() {
                    @Override
                    public void hasPermission() {
                        locating();
                    }

                    @Override
                    public void noPermission(Boolean hasPermanentlyDenied) {
                        if (hasPermanentlyDenied) {
                            alertAppSetPermission(getString(R.string.permission_location_deny_again), PERMISSION_REQUEST_CODE);
                        }
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            locating();
        }
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
                        if (bdLocation.getCity() == null) {
                            return;
                        }
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

        StatusBarUtils.from((Activity) getContext())
                //沉浸状态栏
                .setTransparentStatusbar(true)
                //白底黑字状态栏
                .setLightStatusBar(true)
                //设置toolbar,actionbar等view
                .setActionbarView(mBinding.llTopBar)
                .process();

        mBinding.setPresenter(mPresenter);
        mBinding.setView(this);
        loadMultipleRootFragment(R.id.rolePick, 0, factoryFragment, ownerFragment, agentFragment);
        mBinding.layoutHomeHeader.rbFind.setChecked(true);

        initRecyclerView();

        mPresenter.requestIndexPicUrls();
        mPresenter.requestScrollMsg();
        mPresenter.requestWantedMessages();

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

    /**
     * 切换rolePick为“找房”
     */
    public void findFactory() {
        showHideFragment(factoryFragment, agentFragment);
        showHideFragment(factoryFragment, ownerFragment);
    }

    /**
     * 切换rolePick为“我是中介”
     */
    public void getCommission() {
        showHideFragment(agentFragment, factoryFragment);
        showHideFragment(agentFragment, ownerFragment);
    }

    /**
     * 切换rolePick为“我是业主”
     */
    public void isOwner() {
        showHideFragment(ownerFragment, agentFragment);
        showHideFragment(ownerFragment, factoryFragment);
    }

    public void openCityPage() {
        startActivity(CityActivity.getStartIntent(getContext()));
        getActivity().overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
    }

    public void openSearchPage() {
        startActivity(SearchActivity.getStartIntent(getContext()));
        getActivity().overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
    }

    @Override
    public void onResume() {
        super.onResume();
        mBinding.layoutHomeHeader.scrollTxtView.startAutoScroll();
        mBinding.layoutHomeHeader.slideShowView.startPlay();
    }

    @Override
    public void onPause() {
        locationClient.unRegisterLocationListener(mBdLocationListener);
        super.onPause();
        mBinding.layoutHomeHeader.slideShowView.stopPlay();
        mBinding.layoutHomeHeader.scrollTxtView.stopAutoScroll();
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
        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void initSlideShowView(String[] urls) {
        mBinding.layoutHomeHeader.slideShowView.setImageUrls(urls);
    }

    @Override
    public void initScrollTextView(List<News> newses) {
        mBinding.layoutHomeHeader.scrollTxtView.setNews(newses);
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
    public void onItemClick(View view, int position) {
        Intent intent = new Intent();
        intent.setClass(getContext(), FactoryDetailActivity.class);
        intent.putExtra(FactoryDetailActivity.WANTED_MESSAGE, mAdapter.getData().get(position));
        startActivity(intent);
    }
//
//    public void shareToWX() {
//        new ShareAction(getActivity()).withText("Hello, welcome to use share sdk!!")
//                .setPlatform(SHARE_MEDIA.SINA)
//                .setCallback(new UMShareListener() {
//                    @Override
//                    public void onResult(SHARE_MEDIA share_media) {
//                        Toast.makeText(getContext(),"成功了",Toast.LENGTH_LONG).show();
//                    }
//
//                    @Override
//                    public void onError(SHARE_MEDIA share_media, Throwable throwable) {
//                        Toast.makeText(getContext(),"失败"+throwable.getMessage(),Toast.LENGTH_LONG).show();
//                    }
//
//                    @Override
//                    public void onCancel(SHARE_MEDIA share_media) {
//                        Toast.makeText(getContext(),"取消了",Toast.LENGTH_LONG).show();
//                    }
//                }).share();
//    }
}
