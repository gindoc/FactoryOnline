package com.online.factory.factoryonline.modules.login;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.base.BaseActivity;
import com.online.factory.factoryonline.base.BaseFragment;
import com.online.factory.factoryonline.base.BaseFragmentPagerAdapter;
import com.online.factory.factoryonline.base.BasePresenter;
import com.online.factory.factoryonline.databinding.ActivityLoginBinding;
import com.online.factory.factoryonline.modules.main.MainActivity;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by louiszgm on 2016/9/30.
 */

public class LoginActivity extends BaseActivity {


    private ActivityLoginBinding mBinding;

    @Inject
    @Named("LoginFragments")
    ArrayList<BaseFragment> fragments;

    @Inject
    BaseFragmentPagerAdapter adapter;
    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getComponent().inject(this);
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this , R.layout.activity_login);
        setUpTabs();
    }

    private void setUpTabs() {
        mBinding.viewpager.setAdapter(adapter);
        adapter.setFragments(fragments);
        mBinding.tabs.setupWithViewPager(mBinding.viewpager);
    }

    @Override
    protected BasePresenter createPresent() {
        return null;
    }
}
