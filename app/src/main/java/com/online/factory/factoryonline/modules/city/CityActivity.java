package com.online.factory.factoryonline.modules.city;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.base.BaseActivity;
import com.online.factory.factoryonline.databinding.ActivityCityBinding;
import com.online.factory.factoryonline.modules.locate.fragments.MyLocationListener;
import com.online.factory.factoryonline.utils.rx.RxSubscriber;
import com.trello.rxlifecycle.LifecycleTransformer;

import javax.inject.Inject;

import rx.subjects.BehaviorSubject;
import timber.log.Timber;

/**
 * Created by cwenhui on 2016/11/7.
 */

public class CityActivity extends BaseActivity<CityContract.View, CityPresenter> implements CityContract.View/*, IndexListBar.OnTouchingLetterChangedListener*/ {

    private ActivityCityBinding mBinding;

    @Inject
    CityPresenter mPresenter;

    @Inject
    CityAdapter mAdapter;

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
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_city);
        mBinding.setView(this);

        locating();
    }

    private void locating() {
        ((MyLocationListener) mBdLocationListener).setBdLocationBehaviorSubject(mBehaviorSubject);
        mLocationClient.registerLocationListener(mBdLocationListener);
        mLocationClient.start();            // 启动定位

        mBehaviorSubject.compose(this.bindToLifecycle())
                .subscribe(new RxSubscriber<BDLocation>() {
                    @Override
                    public void _onNext(BDLocation bdLocation) {
                        mBinding.tvCurrentCity.setText(bdLocation.getCity());
                        if (mLocationClient.isStarted()) {
                            mLocationClient.stop();
                        }
                    }

                    @Override
                    public void _onError(Throwable throwable) {
                        Timber.e(throwable.getMessage());
                    }
                });
    }

    @Override
    protected CityPresenter createPresent() {
        return mPresenter;
    }

    @Override
    public <T> LifecycleTransformer<T> getBindToLifecycle() {
        return bindToLifecycle();
    }

    @Override
    public void showError(String error) {
    }


    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, CityActivity.class);
        return intent;
    }

    @Override
    protected void onPause() {
        mLocationClient.unRegisterLocationListener(mBdLocationListener);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mLocationClient.stop();
        super.onDestroy();
    }
}
