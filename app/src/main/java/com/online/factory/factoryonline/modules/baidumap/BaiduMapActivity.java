package com.online.factory.factoryonline.modules.baidumap;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;


import com.baidu.mapapi.clusterutil.clustering.Cluster;
import com.baidu.mapapi.clusterutil.clustering.ClusterItem;
import com.baidu.mapapi.clusterutil.clustering.ClusterManager;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.model.LatLng;
import com.bluelinelabs.logansquare.LoganSquare;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.base.BaseActivity;
import com.online.factory.factoryonline.base.BasePresenter;
import com.online.factory.factoryonline.databinding.ActivityBaidumapBinding;
import com.online.factory.factoryonline.models.LbsCloud;
import com.online.factory.factoryonline.models.response.LbsCloudResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * Created by louiszgm on 2016/10/13.
 */

public class BaiduMapActivity extends BaseActivity implements BaiduMap.OnMapLoadedCallback {

    private ActivityBaidumapBinding mBinding;

    BaiduMap mBaiduMap;
    MapStatus ms;
    private ClusterManager<MyItem1> mClusterManager;


    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, BaiduMapActivity.class);
        return intent;
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_baidumap);

        ms = new MapStatus.Builder().target(new LatLng( 23.04813, 113.742078)).zoom(8).build();
        mBaiduMap = mBinding.bmapView.getMap();
        mBaiduMap.setOnMapLoadedCallback(this);
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(ms));
        // 定义点聚合管理类ClusterManager
        mClusterManager = new ClusterManager<MyItem1>(this, mBaiduMap);
        // 添加Marker点
        addMarkers();
        // 设置地图监听，当地图状态发生改变时，进行点聚合运算
        mBaiduMap.setOnMapStatusChangeListener(mClusterManager);
    }

    @Override
    protected BasePresenter createPresent() {
        return null;
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

    /**
     * 向地图添加Marker点
     */
    public void addMarkers() {

        mClusterManager.setRenderer(new MyRenDerer(this,mBaiduMap,mClusterManager));
        mClusterManager.setAlgorithm(new MyAlgorithm<MyItem1>());
        mClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<MyItem1>() {
            @Override
            public boolean onClusterItemClick(MyItem1 item) {
                ms = new MapStatus.Builder().zoom(12).build();
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(ms));
                return true;
            }
        });
        List<MyItem1> items = getLatLngList();

        mClusterManager.addItems(items);

    }

    private List<MyItem1> getLatLngList() {
        List<MyItem1> results = new ArrayList<>();
        LbsCloudResponse lbsCloudResponse = null;
        List<LbsCloud> list = null;
        StringBuffer response = new StringBuffer();
        BufferedReader reader;
        AssetManager assetManager = this.getAssets();
        String line = "";
        try {
            reader = new BufferedReader(new InputStreamReader(assetManager.open("LbsCloud.json")));
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            JsonObject jsonObject;
            jsonObject = new Gson().fromJson(response.toString(),JsonObject.class);
            String arrayString = jsonObject.getAsJsonArray("pois").toString();
            list = new Gson().fromJson(arrayString, new TypeToken<List<LbsCloud>>(){}.getType());



        } catch (IOException e) {
            e.printStackTrace();
            Timber.e(e.getLocalizedMessage());
        }

        for(LbsCloud lbsCloud:list){
//            String address = lbsCloud.getAddress();
//            String province = lbsCloud.getProvince();
//            float lat = lbsCloud.getLocation().get(1);
//            float lng = lbsCloud.getLocation().get(0);
//            Timber.d(" lat  lng : %f %f",lat,lng);
//            LatLng  latLng = new LatLng(lat,lng);
            results.add(new MyItem1(lbsCloud));
        }
        return results;
    }

    /**
     * 每个Marker点，包含Marker点坐标以及图标
     */
    public class MyItem implements ClusterItem {
        private final LatLng mPosition;

        public MyItem(LatLng latLng) {
            mPosition = latLng;
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
    /**
     * 每个Marker点，包含Marker点坐标以及图标
     */
    public class MyItem1 implements ClusterItem {
        private  LatLng mPosition;
        private  LbsCloud mLbs;
        public MyItem1(LbsCloud lbsCloud) {
            float lat = lbsCloud.getLocation().get(1);
            float lng = lbsCloud.getLocation().get(0);
            LatLng  latLng = new LatLng(lat,lng);
            mPosition = latLng;
            mLbs = lbsCloud;
        }
        public int getDistrictId(){
            return mLbs.getDistrictId();
        }
        public String getDescription(){
            return mLbs.getCustom_district();
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
        ms = new MapStatus.Builder().zoom(9).build();
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(ms));
    }
}
