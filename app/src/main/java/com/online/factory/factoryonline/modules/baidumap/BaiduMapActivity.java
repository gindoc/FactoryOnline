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
import com.online.factory.factoryonline.databinding.ActivityBaidumapBinding;
import com.online.factory.factoryonline.models.Factory;
import com.online.factory.factoryonline.models.FactoryPoi;
import com.online.factory.factoryonline.models.LbsCloud;
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

public class BaiduMapActivity extends BaseActivity<BaiduMapConstract.View, BaiduMapPresent> implements BaiduMap.OnMapLoadedCallback, BaiduMapConstract.View {

    private ActivityBaidumapBinding mBinding;

    BaiduMap mBaiduMap;
    MapStatus ms;
    private ClusterManager<MyItem> mClusterManager;
    private PCDSClusterRenderer pcdsClusterRenderer;

    @Inject
    MapRecyclerViewAdapter mAdapter;

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, BaiduMapActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_baidumap);
        initialMap();
        startCluster();
        mBinding.recyclerView.setAdapter(mAdapter);

    }

    private void startCluster() {
        // 定义点聚合管理类ClusterManager
        mClusterManager = new ClusterManager<MyItem>(this, mBaiduMap);
        // 添加Marker点
        addMarkers();
        // 设置地图监听，当地图状态发生改变时，进行点聚合运算
        mBaiduMap.setOnMapStatusChangeListener(mClusterManager);
        mBaiduMap.setOnMarkerClickListener(mClusterManager);
    }

    private void initialMap() {
        ms = new MapStatus.Builder().target(new LatLng( 23.04813, 113.742078)).zoom(8).build();
        mBaiduMap = mBinding.bmapView.getMap();
        mBaiduMap.setOnMapLoadedCallback(this);
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(ms));
    }

    @Override
    protected BaiduMapPresent createPresent() {
        return mPresenter;
    }

    /**
     * 向地图添加Marker点
     */
    public void addMarkers() {
        pcdsClusterRenderer = new PCDSClusterRenderer<MyItem>(this,mBaiduMap,mClusterManager);
        mClusterManager.setRenderer(pcdsClusterRenderer);
        mClusterManager.setAlgorithm(new PCDSAlgorithm<MyItem>());

        mClusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<MyItem>() {
            @Override
            public boolean onClusterClick(Cluster<MyItem> cluster) {
                Timber.d("onClusterClick");
                StaticCluster staticCluster = (StaticCluster) cluster;
                LatLng center = staticCluster.getPosition();
                ms = new MapStatus.Builder().target(center).zoom(mBaiduMap.getMapStatus().zoom+3).build();
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(ms));
                mBinding.toolbar.setVisibility(View.GONE);
                return true;
            }
        });
        mClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<MyItem>() {
            @Override
            public boolean onClusterItemClick(MyItem item) {
                return false;
            }
        });
        List<MyItem> items = getLatLngList();
        mClusterManager.addItems(items);

    }

    private List<MyItem> getLatLngList() {
        List<MyItem> results = new ArrayList<>();
        List<FactoryPoi> list = null;
        StringBuffer response = new StringBuffer();
        BufferedReader reader;
        AssetManager assetManager = this.getAssets();
        String line = "";
        try {
            reader = new BufferedReader(new InputStreamReader(assetManager.open("FactoryPois.json")));
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            JsonObject jsonObject;
            jsonObject = new Gson().fromJson(response.toString(),JsonObject.class);
            String arrayString = jsonObject.getAsJsonArray("pois").toString();
            list = new Gson().fromJson(arrayString, new TypeToken<List<FactoryPoi>>(){}.getType());
        } catch (IOException e) {
            e.printStackTrace();
            Timber.e(e.getLocalizedMessage());
        }
        for(FactoryPoi factoryPoi:list){
            results.add(new MyItem(factoryPoi));
        }
        return results;
    }

    @Override
    public void loadFactories(List<Factory> factories) {
        mAdapter.setData(factories);
        mBinding.recyclerView.notifyDataSetChanged();
    }

    @Override
    public <T> LifecycleTransformer<T> getBindToLifecycle() {
        return bindToLifecycle();
    }

    @Override
    public void showError(String error) {
    }

    /**
     * 每个Marker点，包含Marker点坐标以及图标
     */
    public class MyItem implements ClusterItem {
        private  LatLng mPosition;
        private  FactoryPoi mFactoryPoi;
        public MyItem(FactoryPoi factoryPoi) {
            double lat = factoryPoi.getLocation().get(0);
            double lng = factoryPoi.getLocation().get(1);
            LatLng  latLng = new LatLng(lat,lng);
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
