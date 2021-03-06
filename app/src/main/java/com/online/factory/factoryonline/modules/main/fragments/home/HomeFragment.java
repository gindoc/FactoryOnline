package com.online.factory.factoryonline.modules.main.fragments.home;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.base.BaseFragment;
import com.online.factory.factoryonline.base.PermissionCallback;
import com.online.factory.factoryonline.databinding.FragmentHomeBinding;
import com.online.factory.factoryonline.modules.city.CityActivity;
import com.online.factory.factoryonline.modules.locate.fragments.MyLocationListener;
import com.online.factory.factoryonline.modules.main.fragments.home.agent.AgentFragment;
import com.online.factory.factoryonline.modules.main.fragments.home.index.IndexFragment;
import com.online.factory.factoryonline.modules.main.fragments.recommend.RecommendFragment;
import com.online.factory.factoryonline.modules.search.SearchActivity;
import com.online.factory.factoryonline.utils.StatusBarUtils;
import com.online.factory.factoryonline.utils.ToastUtil;
import com.online.factory.factoryonline.utils.rx.RxSubscriber;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.subjects.BehaviorSubject;
import timber.log.Timber;


/**
 * Created by cwenhui on 2016.02.23
 */
public class HomeFragment extends BaseFragment<HomeContract.View, HomePresenter> implements HomeContract.View {
    public static final int PERMISSION_REQUEST_CODE = 199;
    private FragmentHomeBinding mBinding;

    @Inject
    IndexFragment indexFragment;

    @Inject
    RecommendFragment recommendFragment;

    @Inject
    AgentFragment agentFragment;

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            locating();
        }
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

        initViewPager();
        return mBinding.getRoot();
    }

    private void initViewPager() {
        final List<BaseFragment> fragments = new ArrayList<>();
        fragments.add(indexFragment);
        fragments.add(recommendFragment);
        fragments.add(agentFragment);
        FragmentPagerAdapter pagerAdapter = new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }

        };
        mBinding.viewpager.setAdapter(pagerAdapter);
        mBinding.tabLayout.setupWithViewPager(mBinding.viewpager);
        mBinding.viewpager.setOffscreenPageLimit(3);
        for (int i = 0; i < pagerAdapter.getCount(); i++) {
            TabLayout.Tab tab = mBinding.tabLayout.getTabAt(i);//获得每一个tab
            tab.setCustomView(R.layout.tab_item_home);//给每一个tab设置view
            if (i == 0) {
                // 设置第一个tab的TextView是被选择的样式
                View view = tab.getCustomView();
                view.findViewById(R.id.view_indicator).setSelected(true);       //第一个tab被选中
                view.findViewById(R.id.tab_text).setSelected(true);
            }
            TextView textView = (TextView) tab.getCustomView().findViewById(R.id.tab_text);
            textView.setText(fragments.get(i).getTitle());//设置tab上的文字
        }
        mBinding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                View view = tab.getCustomView();
                view.findViewById(R.id.view_indicator).setSelected(true);
                view.findViewById(R.id.tab_text).setSelected(true);
                mBinding.viewpager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View view = tab.getCustomView();
                view.findViewById(R.id.view_indicator).setSelected(false);
                view.findViewById(R.id.tab_text).setSelected(false);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
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
    }

    @Override
    public void onPause() {
        locationClient.unRegisterLocationListener(mBdLocationListener);
        super.onPause();
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
        ToastUtil.show(getContext(), error);
    }

}
