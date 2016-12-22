package com.online.factory.factoryonline.modules.baidumap;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.clusterutil.clustering.Cluster;
import com.baidu.mapapi.clusterutil.clustering.ClusterItem;
import com.baidu.mapapi.clusterutil.clustering.ClusterManager;
import com.baidu.mapapi.clusterutil.clustering.algo.StaticCluster;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.base.BaseActivity;
import com.online.factory.factoryonline.base.PermissionCallback;
import com.online.factory.factoryonline.customview.recyclerview.BaseRecyclerViewAdapter;
import com.online.factory.factoryonline.customview.recyclerview.OnPageListener;
import com.online.factory.factoryonline.databinding.ActivityBaidumapBinding;
import com.online.factory.factoryonline.models.MapPoi;
import com.online.factory.factoryonline.models.WantedMessage;
import com.online.factory.factoryonline.modules.FactoryDetail.FactoryDetailActivity;
import com.online.factory.factoryonline.modules.locate.fragments.MyLocationListener;
import com.online.factory.factoryonline.utils.GeoHash;
import com.online.factory.factoryonline.utils.StatusBarUtils;
import com.online.factory.factoryonline.utils.rx.RxSubscriber;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.subjects.BehaviorSubject;
import timber.log.Timber;

/**
 * Created by louiszgm on 2016/10/13.
 */
public class BaiduMapActivity extends BaseActivity<BaiduMapConstract.View, BaiduMapPresent> implements BaiduMap.OnMapLoadedCallback,
        BaiduMapConstract.View, BaseRecyclerViewAdapter.OnItemClickListener, OnPageListener {
    public static final int PERMISSION_REQUEST_CODE = 199;

    private ActivityBaidumapBinding mBinding;

    BaiduMap mBaiduMap;
    MapStatus ms;
    private ClusterManager<MyItem> mClusterManager;
    private PCDSClusterRenderer pcdsClusterRenderer;

    @Inject
    LocationClient mLocationClient;

    @Inject
    BDLocationListener mBdLocationListener;

    @Inject
    BehaviorSubject mBehaviorSubject;

    @Inject
    BaiduMapPresent mPresenter;

    @Inject
    Resources resources;

    @Inject
    MapRecyclerViewAdapter mAdapter;
    private String mNext = null;
    private int clickedAreaId;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, BaiduMapActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getComponent().inject(this);
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_baidumap);
        mBinding.setView(this);

        initialMap();
        checkPermission();

        startCluster();

        initToolbar();

        mBinding.recyclerView.setAdapter(mAdapter);
        mBinding.recyclerView.setPageFooter(R.layout.layout_recyclerview_footer);
        mBinding.recyclerView.setOnPageListener(this);
        mAdapter.setOnItemClickListener(this);
    }

    private void initToolbar() {
        StatusBarUtils.from(this)
                //沉浸状态栏
                .setTransparentStatusbar(true)
                //白底黑字状态栏
                .setLightStatusBar(true)
                //设置toolbar,actionbar等view
                .setActionbarView(mBinding.toolbar)
                .process();
        mBinding.toolbar.setTitle("");
        setSupportActionBar(mBinding.toolbar);
        mBinding.toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
    }

    private boolean isFirstLoc = true;

    private void initialMap() {
        ((MyLocationListener) mBdLocationListener).setBdLocationBehaviorSubject(mBehaviorSubject);
        mLocationClient.registerLocationListener(mBdLocationListener);
        mBehaviorSubject.compose(this.bindToLifecycle())
                .subscribe(new RxSubscriber() {
                    @Override
                    public void _onNext(Object o) {
                        BDLocation bdLocation = (BDLocation) o;
                        Timber.e("纬度：%lf  精度：%lf" ,bdLocation.getLatitude() , bdLocation.getLongitude());
                        // 定位数据
                        MyLocationData data = new MyLocationData.Builder()
                                // 定位精度bdLocation.getRadius()
                                .accuracy(bdLocation.getRadius())
                                // 此处设置开发者获取到的方向信息，顺时针0-360
                                .direction(bdLocation.getDirection())
                                // 经度
                                .latitude(bdLocation.getLatitude())
                                // 纬度
                                .longitude(bdLocation.getLongitude())
                                // 构建
                                .build();
                        mBaiduMap.setMyLocationData(data);
                        if (isFirstLoc) {
                            isFirstLoc = false;
                            LatLng ll = new LatLng(bdLocation.getLatitude(),
                                    bdLocation.getLongitude());
                            MapStatusUpdate msu = MapStatusUpdateFactory.newLatLngZoom(ll, 8);
                            mBaiduMap.animateMapStatus(msu);
                        }
                    }

                    @Override
                    public void _onError(Throwable throwable) {}
                });
        mBaiduMap = mBinding.bmapView.getMap();
        mBaiduMap.setMyLocationEnabled(true);
        mBaiduMap.setOnMapLoadedCallback(this);
        mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true, null));
//        mLocationClient.start();
    }

    private void startCluster() {
        // 定义点聚合管理类ClusterManager
        mClusterManager = new ClusterManager<MyItem>(this, mBaiduMap);
        // 添加Marker点
        mPresenter.getLatLngList(305);
        // 设置地图监听，当地图状态发生改变时，进行点聚合运算
        mBaiduMap.setOnMapStatusChangeListener(mClusterManager);
        mBaiduMap.setOnMarkerClickListener(mClusterManager);
    }

    @Override
    protected BaiduMapPresent createPresent() {
        return mPresenter;
    }


    @Override
    public void loadWantedMessages(List<WantedMessage> wantedMessages) {
        mAdapter.setData(wantedMessages);
        mBinding.recyclerView.notifyDataSetChanged();
        mBinding.recyclerView.hideLoadingFooter();
    }

    public void retract() {
        mBinding.toolbar.setVisibility(View.VISIBLE);
        mBinding.llFactoryList.setVisibility(View.GONE);
    }

    /**
     * 向地图添加Marker点
     */
    @Override
    public void loadMarker(List<MapPoi> mapPois) {
        pcdsClusterRenderer = new PCDSClusterRenderer<MyItem>(this, mBaiduMap, mClusterManager);
        mClusterManager.setRenderer(pcdsClusterRenderer);
        mClusterManager.setAlgorithm(new PCDSAlgorithm<MyItem>());

        mClusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<MyItem>() {
            @Override
            public boolean onClusterClick(Cluster<MyItem> cluster) {
                Timber.d("onClusterClick");
                StaticCluster staticCluster = (StaticCluster) cluster;
                LatLng center = staticCluster.getPosition();
                int currentZoom = (int) mBaiduMap.getMapStatus().zoom;
                if (currentZoom >= 13) {
                    MyItem item = ((ArrayList<MyItem>) cluster.getItems()).get(0);
                    int areaId = item.getMapPoi().getArea_id();
                    String url = resources.getString(R.string.api) + "wantedmessages/recommend";
                    mPresenter.requestWantedMessagesByNet(url, areaId);
                    clickedAreaId = areaId;
                    mBinding.toolbar.setVisibility(View.GONE);
                    mBinding.llFactoryList.setVisibility(View.VISIBLE);
                } else {
                    ms = new MapStatus.Builder().target(center).zoom(currentZoom + 3).build();
                    mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(ms));
                }
                return true;
            }
        });
        mClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<MyItem>() {
            @Override
            public boolean onClusterItemClick(MyItem item) {
                int areaId = item.getMapPoi().getArea_id();
                String url = resources.getString(R.string.api) + "wantedmessages/recommend";
                mPresenter.requestWantedMessagesByNet(url, areaId);
                clickedAreaId = areaId;
                mBinding.toolbar.setVisibility(View.GONE);
                mBinding.llFactoryList.setVisibility(View.VISIBLE);
                return true;
            }
        });
        List<MyItem> results = new ArrayList<>();
        for (MapPoi mapPoi : mapPois) {
            results.add(new MyItem(mapPoi));
        }
        mClusterManager.addItems(results);
    }

    @Override
    public void loadNextUrlAndCount(String next) {
        mNext = next;
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
        WantedMessage wantedMessage = mAdapter.getData().get(position);
        intent.putExtra(FactoryDetailActivity.WANTED_MESSAGE, wantedMessage);
        startActivity(intent);
    }

    @Override
    public void onPage() {
        mBinding.recyclerView.showLoadingFooter();
        if (!TextUtils.isEmpty(mNext)) {
            mPresenter.requestWantedMessagesByNet(mNext, clickedAreaId);
        }else {
            Toast.makeText(this, "没有更多数据了", Toast.LENGTH_SHORT).show();
            mBinding.recyclerView.hideLoadingFooter();
        }
    }

    /**
     * 每个Marker点，包含Marker点坐标以及图标
     */
    public class MyItem implements ClusterItem {
        private LatLng mPosition;
        private MapPoi mapPoi;

        public MyItem(MapPoi mapPoi) {
            double[] latAndLon = new GeoHash().decode(mapPoi.getGeohash());
            double lat = latAndLon[0];
            double lng = latAndLon[1];
            LatLng latLng = new LatLng(lat, lng);
            mPosition = latLng;
            this.mapPoi = mapPoi;
        }

        public MapPoi getMapPoi() {
            return mapPoi;
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
        ms = new MapStatus.Builder().zoom(12).build();
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(ms));
    }

    @Override
    protected void onPause() {
        mLocationClient.unRegisterLocationListener(mBdLocationListener);
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
        mLocationClient.stop();
        mBaiduMap.setMyLocationEnabled(false);
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }

    private void checkPermission() {
        performCodeWithPermission(getString(R.string.permission_location_rationale), PERMISSION_REQUEST_CODE,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, new PermissionCallback() {
                    @Override
                    public void hasPermission() {
                        mLocationClient.start();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            mLocationClient.start();
        }
    }
}
