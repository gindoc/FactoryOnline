package com.online.factory.factoryonline.modules.locate;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.base.BaseActivity;
import com.online.factory.factoryonline.base.BasePresenter;

/**
 * Created by louiszgm on 2016/10/26.
 */

public class LocateActivity extends BaseActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locate);

    }

    @Override
    protected BasePresenter createPresent() {
        return null;
    }
}
