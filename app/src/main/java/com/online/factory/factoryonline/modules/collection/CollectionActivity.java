package com.online.factory.factoryonline.modules.collection;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.base.BaseActivity;
import com.online.factory.factoryonline.databinding.ActivityCollectionBinding;
import com.online.factory.factoryonline.modules.collection.agent.AgentCollectionFragment;
import com.online.factory.factoryonline.modules.collection.owner.OwnerCollectionFragment;
import com.online.factory.factoryonline.utils.StatusBarUtils;
import com.trello.rxlifecycle.LifecycleTransformer;

import javax.inject.Inject;

/**
 * 作者: GIndoc
 * 日期: 2016/12/1 17:34
 * 作用:
 */

public class CollectionActivity extends BaseActivity<CollectionContract.View, CollectionPresenter> implements CollectionContract.View {

    @Inject
    CollectionPresenter mPresenter;


    @Inject
    AgentCollectionFragment agentCollectionFragment;

    @Inject
    OwnerCollectionFragment ownerCollectionFragment;

    private ActivityCollectionBinding mBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getComponent().inject(this);
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_collection);
        mBinding.setView(this);
        StatusBarUtils.from(this)
                //白底黑字状态栏
                .setLightStatusBar(true)
                //设置toolbar,actionbar等view
                .setActionbarView(mBinding.rlTopBar)
                .process();

        mBinding.rbAgency.setChecked(true);
        loadRootFragment(R.id.rl_container, agentCollectionFragment);
    }


    @Override
    protected CollectionPresenter createPresent() {
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
        return new Intent(context, CollectionActivity.class);
    }

    public void loadAgentList(View view) {
        startWithPop(agentCollectionFragment);
    }

    public void loadOwnerList(View view) {
        startWithPop(ownerCollectionFragment);
    }

}
