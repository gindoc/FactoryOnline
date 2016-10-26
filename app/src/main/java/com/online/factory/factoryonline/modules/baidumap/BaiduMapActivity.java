package com.online.factory.factoryonline.modules.baidumap;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;


import com.baidu.mapapi.clusterutil.clustering.Cluster;
import com.baidu.mapapi.clusterutil.clustering.ClusterItem;
import com.baidu.mapapi.clusterutil.clustering.ClusterManager;
import com.baidu.mapapi.clusterutil.clustering.algo.StaticCluster;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.base.BaseActivity;
import com.online.factory.factoryonline.base.BasePresenter;
import com.online.factory.factoryonline.customview.recyclerview.BaseRecyclerViewAdapter;
import com.online.factory.factoryonline.databinding.ActivityBaidumapBinding;
import com.online.factory.factoryonline.models.Factory;
import com.online.factory.factoryonline.models.FactoryPoi;
import com.online.factory.factoryonline.models.LbsCloud;
import com.online.factory.factoryonline.modules.FactoryDetail.FactoryDetailActivity;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Created by louiszgm on 2016/10/13.
 */
public class BaiduMapActivity extends BaseActivity<BaiduMapConstract.View, BaiduMapPresent> implements BaiduMap.OnMapLoadedCallback, BaiduMapConstract.View, BaseRecyclerViewAdapter.OnItemClickListener {

    private ActivityBaidumapBinding mBinding;

    BaiduMap mBaiduMap;
    MapStatus ms;
    private ClusterManager<MyItem> mClusterManager;
    private PCDSClusterRenderer pcdsClusterRenderer;

    @Inject
    BaiduMapPresent mPresenter;

    @Inject
    MapRecyclerViewAdapter mAdapter;

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, BaiduMapActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getComponent().inject(this);
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_baidumap);
        mBinding.setView(this);

        initialMap();

        startCluster();

        mBinding.toolbar.setTitle("");
        setSupportActionBar(mBinding.toolbar);
        mBinding.toolbar.setNavigationIcon(R.drawable.ic_arrow_left);

        mBinding.recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
    }

    private void initialMap() {
        ms = new MapStatus.Builder().target(new LatLng(23.04813, 113.742078)).zoom(8).build();
        mBaiduMap = mBinding.bmapView.getMap();
        mBaiduMap.setOnMapLoadedCallback(this);
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(ms));
    }

    private void startCluster() {
        // 定义点聚合管理类ClusterManager
        mClusterManager = new ClusterManager<MyItem>(this, mBaiduMap);
        // 添加Marker点
        mPresenter.getLatLngList(12);
        // 设置地图监听，当地图状态发生改变时，进行点聚合运算
        mBaiduMap.setOnMapStatusChangeListener(mClusterManager);
        mBaiduMap.setOnMarkerClickListener(mClusterManager);
    }

    @Override
    protected BaiduMapPresent createPresent() {
        return mPresenter;
    }


    @Override
    public void loadFactories(List<Factory> factories) {
        mAdapter.setData(factories);
        mBinding.recyclerView.notifyDataSetChanged();
    }

    public void retract() {
        mBinding.toolbar.setVisibility(View.VISIBLE);
        mBinding.llFactoryList.setVisibility(View.GONE);
    }

    /**
     * 向地图添加Marker点
     */
    @Override
    public void loadMarker(List<FactoryPoi> factoryPois) {
        pcdsClusterRenderer = new PCDSClusterRenderer<MyItem>(this, mBaiduMap, mClusterManager);
        mClusterManager.setRenderer(pcdsClusterRenderer);
        mClusterManager.setAlgorithm(new PCDSAlgorithm<MyItem>());

        mClusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<MyItem>() {
            @Override
            public boolean onClusterClick(Cluster<MyItem> cluster) {
                Timber.d("onClusterClick");
                StaticCluster staticCluster = (StaticCluster) cluster;
                LatLng center = staticCluster.getPosition();
                float currentZoom = mBaiduMap.getMapStatus().zoom;
                if (currentZoom >= 13) {
                    MyItem item = ((ArrayList<MyItem>) cluster.getItems()).get(0);
                    int streetId = item.getFactoryPoi().getStreet_id();
                    mPresenter.getStreetFactoryList(streetId);
                    mBinding.toolbar.setVisibility(View.GONE);
                    mBinding.llFactoryList.setVisibility(View.VISIBLE);
                } else {
                    ms = new MapStatus.Builder().target(center).zoom(currentZoom + 3).build();
                    mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(ms));
                }
//                if (staticCluster)
                return true;
            }
        });
        mClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<MyItem>() {
            @Override
            public boolean onClusterItemClick(MyItem item) {
                int streetId = item.getFactoryPoi().getStreet_id();
                mPresenter.getStreetFactoryList(streetId);
                mBinding.toolbar.setVisibility(View.GONE);
                mBinding.llFactoryList.setVisibility(View.VISIBLE);
                return true;
            }
        });
        List<MyItem> results = new ArrayList<>();
        for (FactoryPoi factoryPoi : factoryPois) {
            results.add(new MyItem(factoryPoi));
        }
        mClusterManager.addItems(results);
    }

    @Override
    public <T> LifecycleTransformer<T> getBindToLifecycle() {
        return bindToLifecycle();
    }

    @Override
    public void showError(String error) {
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent();
        intent.setClass(this, FactoryDetailActivity.class);
        Factory factory = mAdapter.getData().get(position);
        intent.putExtra(FactoryDetailActivity.FACTORY_DETIAL, factory);
        startActivity(intent);
    }

    /**
     * 每个Marker点，包含Marker点坐标以及图标
     */
    public class MyItem implements ClusterItem {
        private LatLng mPosition;
        private FactoryPoi mFactoryPoi;

        public MyItem(FactoryPoi factoryPoi) {
            double lat = factoryPoi.getLocation().get(0);
            double lng = factoryPoi.getLocation().get(1);
            LatLng latLng = new LatLng(lat, lng);
            mPosition = latLng;
            mFactoryPoi = factoryPoi;
        }

        public FactoryPoi getFactoryPoi() {
            return mFactoryPoi;
        }

        @Override
        public LatLng getPosition() {
            return mPosition;
        }

        @Override
        public BitmapDescriptor getBitmapDescriptor() {
            return BitmapDescriptorFactory
                    .fromResource(R.drawable.icon_gcoding);
        }
    }

    @Override
    public void onMapLoaded() {
        // TODO Auto-generated method stub
        ms = new MapStatus.Builder().zoom(12).build();
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(ms));
    }

    @Override
    protected void onPause() {
        mBinding.bmapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mBinding.bmapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        mBinding.bmapView.onDestroy();
        super.onDestroy();
    }
}
