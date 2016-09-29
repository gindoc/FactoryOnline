package com.online.factory.factoryonline.modules.splash;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.online.factory.factoryonline.base.BaseActivity;
import com.online.factory.factoryonline.base.BasePresenter;
import com.online.factory.factoryonline.modules.main.MainActivity;

/**
 * Created by louiszgm on 2016/9/29.
 */

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivity(MainActivity.getStartIntent(SplashActivity.this));
        SplashActivity.this.finish();
        overridePendingTransition(0, 0);
    }

    @Override
    protected BasePresenter createPresent() {
       return null;
    }
}
