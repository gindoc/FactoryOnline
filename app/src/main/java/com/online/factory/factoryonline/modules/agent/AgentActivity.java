package com.online.factory.factoryonline.modules.agent;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.base.BaseActivity;
import com.online.factory.factoryonline.customview.DividerItemDecoration;
import com.online.factory.factoryonline.databinding.ActivityAgentBinding;
import com.online.factory.factoryonline.databinding.LayoutAgentHeaderBinding;
import com.trello.rxlifecycle.LifecycleTransformer;

import javax.inject.Inject;

/**
 * 作者: GIndoc
 * 日期: 2016/12/6 9:50
 * 作用:
 */

public class AgentActivity extends BaseActivity<AgentContract.View, AgentPresenter> implements AgentContract.View {
    private ActivityAgentBinding mBinding;
    private LayoutAgentHeaderBinding mHeaderBinding;

    @Inject
    AgentPresenter mPresenter;

    @Inject
    AgentRecyclerViewAdapter mAdapter;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, AgentActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getComponent().inject(this);
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_agent);
        mHeaderBinding = LayoutAgentHeaderBinding.inflate(LayoutInflater.from(this), null);
        mBinding.setView(this);

        mBinding.recyclerView.setAdapter(mAdapter);
        mBinding.recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        mBinding.recyclerView.addHeader(mHeaderBinding.getRoot());
    }

    @Override
    protected AgentPresenter createPresent() {
        return mPresenter;
    }

    @Override
    public <T> LifecycleTransformer<T> getBindToLifecycle() {
        return bindToLifecycle();
    }

    @Override
    public void showError(String error) {

    }
}
