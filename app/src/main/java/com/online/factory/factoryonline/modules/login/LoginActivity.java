package com.online.factory.factoryonline.modules.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.base.BaseActivity;
import com.online.factory.factoryonline.base.BaseFragment;
import com.online.factory.factoryonline.base.BaseFragmentPagerAdapter;
import com.online.factory.factoryonline.base.BasePresenter;
import com.online.factory.factoryonline.databinding.ActivityLoginBinding;
import com.online.factory.factoryonline.models.User;
import com.online.factory.factoryonline.models.post.Login;
import com.online.factory.factoryonline.modules.forgetPwd.ForgetPwdActivity;
import com.online.factory.factoryonline.modules.main.MainActivity;
import com.online.factory.factoryonline.modules.regist.RegistActivity;
import com.online.factory.factoryonline.utils.DensityUtil;
import com.online.factory.factoryonline.utils.StatusBarUtils;
import com.online.factory.factoryonline.utils.ToastUtil;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.lang.reflect.Field;
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
        for (int i=0;i<adapter.getCount();i++) {
            TabLayout.Tab tab = mBinding.tabs.getTabAt(i);
            tab.setCustomView(R.layout.tab_item_login);//给每一个tab设置view
            if (i == 0) {
                // 设置第一个tab的TextView是被选择的样式
                View view = tab.getCustomView();
                view.findViewById(R.id.view_indicator).setSelected(true);       //第一个tab被选中
                view.findViewById(R.id.tab_text).setSelected(true);
            }
            TextView textView = (TextView) tab.getCustomView().findViewById(R.id.tab_text);
            textView.setText(fragments.get(i).getTitle());//设置tab上的文字
        }
        mBinding.tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                View view = tab.getCustomView();
                view.findViewById(R.id.view_indicator).setSelected(true);
                view.findViewById(R.id.tab_text).setSelected(true);
                mBinding.viewpager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View view = tab.getCustomView();
                view.findViewById(R.id.view_indicator).setSelected(false);
                view.findViewById(R.id.tab_text).setSelected(false);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        setUpIndicatorWidth();
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
    public void loginSuccessfully(User user) {
//        startActivity(MainActivity.getStartIntent(this));
        Intent intent = new Intent();
        intent.putExtra(MainActivity.RESULT_USER, user);
        setResult(Activity.RESULT_OK, intent);
        finish();
        overridePendingTransition(R.anim.zoomin,R.anim.zoomout);
    }

    /**
     * 通过反射修改TabLayout Indicator的宽度（仅在Android 4.2及以上生效）
     */
    private void setUpIndicatorWidth() {
        Class<?> tabLayoutClass = mBinding.tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayoutClass.getDeclaredField("mTabStrip");
            tabStrip.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        LinearLayout layout = null;
        try {
            if (tabStrip != null) {
                layout = (LinearLayout) tabStrip.get(mBinding.tabs);
            }
            for (int i = 0; i < layout.getChildCount(); i++) {
                View child = layout.getChildAt(i);
                child.setPadding(0, 0, 0, 0);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    int spacing = getResources().getDimensionPixelOffset(R.dimen.x10);
                    params.setMarginStart(spacing);
                    params.setMarginEnd(spacing);
                }
                child.setLayoutParams(params);
                child.invalidate();
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
