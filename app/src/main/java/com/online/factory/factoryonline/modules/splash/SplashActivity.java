package com.online.factory.factoryonline.modules.splash;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.online.factory.factoryonline.R;
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

        ImageView imageView = new ImageView(this);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageResource(R.drawable.demo);
        setContentView(imageView);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(MainActivity.getStartIntent(SplashActivity.this));
                finish();
                overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
            }
        }, 1000);

    }

    @Override
    protected BasePresenter createPresent() {
       return null;
    }

}
