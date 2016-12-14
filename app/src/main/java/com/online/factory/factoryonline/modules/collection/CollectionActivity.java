package com.online.factory.factoryonline.modules.collection;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.baidu.mapapi.clusterutil.MarkerManager;
import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.base.BaseActivity;
import com.online.factory.factoryonline.customview.DividerItemDecoration;
import com.online.factory.factoryonline.customview.recyclerview.BaseRecyclerViewAdapter;
import com.online.factory.factoryonline.customview.recyclerview.OnPageListener;
import com.online.factory.factoryonline.databinding.ActivityCollectionBinding;
import com.online.factory.factoryonline.databinding.ActivityPublicationBinding;
import com.online.factory.factoryonline.models.WantedMessage;
import com.online.factory.factoryonline.modules.FactoryDetail.FactoryDetailActivity;
import com.online.factory.factoryonline.modules.collection.agent.AgentCollectionFragment;
import com.online.factory.factoryonline.modules.collection.owner.OwnerCollectionFragment;
import com.online.factory.factoryonline.modules.publication.PublicationContract;
import com.online.factory.factoryonline.modules.publication.PublicationPresenter;
import com.online.factory.factoryonline.modules.publication.PublicationRecyclerViewAdapter;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.util.List;

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
