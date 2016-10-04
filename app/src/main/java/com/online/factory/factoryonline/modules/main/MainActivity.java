package com.online.factory.factoryonline.modules.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.base.BaseActivity;
import com.online.factory.factoryonline.base.BasePresenter;
import com.online.factory.factoryonline.modules.login.LoginContext;

import javax.inject.Inject;

public class MainActivity extends BaseActivity {

    @Inject
    RecommendFragment recommendFragment;

    @Inject
    UserFragment userFragment;

    @Inject
    HomeFragment homeFragment;

    @Inject
    LoginContext mLoginContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getComponent().inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadRootFragment(R.id.tab_content,recommendFragment);
    }

    @Override
    protected BasePresenter createPresent() {
        return null;
    }

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        return intent;
    }

    public void onClickMsg(View view){
        mLoginContext.openMsg(MainActivity.this);
    }
    public void onClickRecommend(View view){
        if(findFragment(RecommendFragment.class) == null){
            startWithPop(recommendFragment);
        }
    }

    public void onClickUser(View view){
        if(findFragment(UserFragment.class) == null){
            startWithPop(userFragment);
        }
    }

}
