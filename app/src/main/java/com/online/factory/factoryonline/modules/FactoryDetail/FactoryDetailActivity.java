package com.online.factory.factoryonline.modules.FactoryDetail;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.base.BaseActivity;
import com.online.factory.factoryonline.customview.AppBarStateChangeListener;
import com.online.factory.factoryonline.databinding.ActivityFactoryDetailBinding;
import com.online.factory.factoryonline.models.Factory;
import com.online.factory.factoryonline.modules.locate.fragments.MyLocationListener;
import com.online.factory.factoryonline.utils.rx.RxSubscriber;
import com.squareup.picasso.Picasso;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.subjects.BehaviorSubject;
import timber.log.Timber;

/**
 * Created by cwenhui on 2016/10/17.
 */
public class FactoryDetailActivity extends BaseActivity<FactoryDetailContract.View, FactoryDetailPresenter> implements FactoryDetailContract.View {
    public static final String FACTORY_DETIAL = "factory_detail";
    private ActivityFactoryDetailBinding mBinding;

    @Inject
    FactoryDetailPresenter mPresenter;

    @Inject
    FactoryDetailViewModel mViewModel;

    private List<ImageView> imageViewsList = new ArrayList<>();

    BaiduMap mBaiduMap;

    @Inject
    LocationClient mLocationClient;

    @Inject
    BDLocationListener mBdLocationListener;

    @Inject
    BehaviorSubject mBehaviorSubject;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getComponent().inject(this);
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_factory_detail);

        initToolbar();
        initFactoryDetail();
        initViewPager();
        initBaiduMap();
    }

    private void initBaiduMap() {
        ((MyLocationListener) mBdLocationListener).setBdLocationBehaviorSubject(mBehaviorSubject);
        mLocationClient.registerLocationListener(mBdLocationListener);
        mBehaviorSubject.compose(this.bindToLifecycle())
                .subscribe(new RxSubscriber() {
                    @Override
                    public void _onNext(Object o) {
                        BDLocation bdLocation = (BDLocation) o;
                        Timber.e("纬度：" + bdLocation.getLatitude() + "   精度：" + bdLocation.getLongitude());
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
                        double latitude = bdLocation.getLatitude();
                        double longitude = bdLocation.getLongitude();
                        LatLng ll = new LatLng(latitude, longitude);
                        MapStatusUpdate msu = MapStatusUpdateFactory.newLatLngZoom(ll, 18);
                        mBaiduMap.animateMapStatus(msu);

                        mLocationClient.unRegisterLocationListener(mBdLocationListener);

                        StringBuilder api = new StringBuilder("http://api.map.baidu.com/staticimage?center=");
                        api.append(longitude+","+latitude);
                        api.append("&width=" + mBinding.ivMapview.getWidth());
                        api.append("&height=" + mBinding.ivMapview.getHeight());
                        api.append("&zoom=18");
                        api.append("&markers=");
                        api.append(longitude+","+latitude);
                        Picasso.with(FactoryDetailActivity.this).load(api.toString()).into(mBinding.ivMapview);
                    }

                    @Override
                    public void _onError(Throwable throwable) {
                    }
                });

        mBinding.mapview.showZoomControls(false);       //关闭缩放按钮
        mBaiduMap = mBinding.mapview.getMap();
        mBaiduMap.setMyLocationEnabled(true);

        UiSettings settings = mBaiduMap.getUiSettings();
        settings.setAllGesturesEnabled(false);          //关闭一切手势操作

        mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true, null));
        mLocationClient.start();

    }

    private void initViewPager() {
        Factory factory = (Factory) getIntent().getSerializableExtra(FACTORY_DETIAL);
        List<String> imageUrls = factory.getImage_urls();
        for (int i = 0; i < imageUrls.size(); i++) {
            ImageView imageView = new ImageView(FactoryDetailActivity.this);
            imageView.setTag(imageUrls.get(i));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageViewsList.add(imageView);
        }
        mBinding.viewpager.setFocusable(true);
        mBinding.viewpager.setAdapter(new MyPagerAdapter());
        mBinding.viewpager.addOnPageChangeListener(new MyPageListener());
    }

    private void initToolbar() {
        mBinding.toolbar.setTitle("");
        setSupportActionBar(mBinding.toolbar);
        mBinding.toolbar.setNavigationIcon(R.drawable.ic_arrow_left_with_shadow);

        mBinding.appbar.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                MenuItem menuItem = mBinding.toolbar.getMenu().findItem(R.id.collect);
                if (menuItem != null) {
                    if (state == State.EXPANDED) {         //展开状态
                        menuItem.setIcon(R.drawable.ic_collected_with_shadow);
                        mBinding.toolbar.setNavigationIcon(R.drawable.ic_arrow_left_with_shadow);
                    } else if (state == State.COLLAPSED) {     //折叠状态
                        menuItem.setIcon(R.drawable.ic_collected);
                        mBinding.toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
                    }
                }
            }
        });
    }

    @Override
    protected FactoryDetailPresenter createPresent() {
        return mPresenter;
    }

    @Override
    public void initFactoryDetail() {
        Factory factory = (Factory) getIntent().getSerializableExtra(FACTORY_DETIAL);
        mViewModel.setFactory(factory);
        mBinding.setViewModel(mViewModel);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.factory_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
                break;
            case R.id.collect:
                break;
        }
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem item = menu.findItem(R.id.collect);
        mPresenter.isCollected(0, item);
        return true;
    }

    @Override
    public <T> LifecycleTransformer<T> getBindToLifecycle() {
        return bindToLifecycle();
    }

    @Override
    public void showError(String error) {
    }

    class MyPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return imageViewsList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = imageViewsList.get(position);

            Picasso.with(FactoryDetailActivity.this)
                    .load((String) imageView.getTag())
                    .placeholder(R.drawable.ic_msg_online)
                    .error(R.drawable.ic_home_full)
                    .into(imageView);

            ((ViewPager) container).addView(imageViewsList.get(position));
            return imageViewsList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(imageViewsList.get(position));
        }
    }

    class MyPageListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            mBinding.tvPagerIndex.setText(position + 1 + "/" + imageViewsList.size());
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }

    @Override
    protected void onPause() {
        mLocationClient.unRegisterLocationListener(mBdLocationListener);
        mBinding.mapview.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mBinding.mapview.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        mBinding.mapview.onDestroy();
        mLocationClient.stop();
        mBaiduMap.setMyLocationEnabled(false);
        super.onDestroy();
    }
}
