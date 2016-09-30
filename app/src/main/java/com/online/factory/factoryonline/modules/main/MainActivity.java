package com.online.factory.factoryonline.modules.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.base.BaseActivity;
import com.online.factory.factoryonline.base.BasePresenter;
import com.online.factory.factoryonline.modules.main.fragments.home.HomeFragment;

import javax.inject.Inject;

public class MainActivity extends BaseActivity {

    @Inject
    HomeFragment homeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadRootFragment(R.id.tab_content,homeFragment);
    }

    @Override
    protected BasePresenter createPresent() {
        return null;
    }

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        return intent;
    }
}
