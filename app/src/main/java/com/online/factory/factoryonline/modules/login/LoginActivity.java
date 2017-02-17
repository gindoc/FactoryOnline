package com.online.factory.factoryonline.modules.login;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.base.BaseActivity;
import com.online.factory.factoryonline.base.BaseFragment;
import com.online.factory.factoryonline.base.BaseFragmentPagerAdapter;
import com.online.factory.factoryonline.base.BasePresenter;
import com.online.factory.factoryonline.databinding.ActivityLoginBinding;
import com.online.factory.factoryonline.models.post.Login;
import com.online.factory.factoryonline.modules.forgetPwd.ForgetPwdActivity;
import com.online.factory.factoryonline.modules.regist.RegistActivity;
import com.online.factory.factoryonline.utils.StatusBarUtils;
import com.online.factory.factoryonline.utils.ToastUtil;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by louiszgm on 2016/9/30.
 */

public class LoginActivity extends BaseActivity implements LoginContract.View{
    public static final int TO_REGIST_ACTIVITY = 99;

    private ActivityLoginBinding mBinding;

    @Inject
    LoginPresenter presenter;
    @Inject
    @Named("LoginFragments")
    ArrayList<BaseFragment> fragments;

    @Inject
    BaseFragmentPagerAdapter adapter;
    public static Intent getStartIntent(Context context) {
        return new Intent(context, LoginActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getComponent().inject(this);
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this , R.layout.activity_login);
        mBinding.setView(this);
        StatusBarUtils.from(this)
                //沉浸状态栏
                .setTransparentStatusbar(true)
                //白底黑字状态栏
                .setLightStatusBar(true)
                //设置toolbar,actionbar等view
                .setActionbarView(mBinding.rlTopBar)
                .process();
        setUpTabs();
    }

    public void exitLogin() {
        finish();
        overridePendingTransition(R.anim.no_anim, R.anim.translate_bottom_out);
    }

    public void login(Login loginBean){
        presenter.login(loginBean);
    }

    private void setUpTabs() {
        mBinding.viewpager.setAdapter(adapter);
        adapter.setFragments(fragments);
        mBinding.tabs.setupWithViewPager(mBinding.viewpager);
    }

    public void onCLickSignUp(View view){
        startActivityForResult(RegistActivity.getStartIntent(this), TO_REGIST_ACTIVITY);
        overridePendingTransition(R.anim.zoomin,R.anim.zoomout);
    }

    public void onClickForgetPwd(View view) {
        startActivity(ForgetPwdActivity.getStartIntent(this));
        overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RegistActivity.REGIST_SUCCESS && requestCode == TO_REGIST_ACTIVITY) {
            finish();
        }
    }

    @Override
    protected BasePresenter createPresent() {
        return presenter;
    }

    @Override
    public <T> LifecycleTransformer<T> getBindToLifecycle() {
        return bindToLifecycle();
    }

    @Override
    public void showError(String error) {
        ToastUtil.show(this, error);
    }

    @Override
    public void loginSuccessfully() {
//        startActivity(MainActivity.getStartIntent(this));
        finish();
        overridePendingTransition(R.anim.zoomin,R.anim.zoomout);
    }


}
