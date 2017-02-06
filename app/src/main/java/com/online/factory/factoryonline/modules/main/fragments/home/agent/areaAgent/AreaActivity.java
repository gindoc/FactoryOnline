package com.online.factory.factoryonline.modules.main.fragments.home.agent.areaAgent;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.Toast;

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.base.BaseActivity;
import com.online.factory.factoryonline.customview.recyclerview.OnPageListener;
import com.online.factory.factoryonline.databinding.ActivityAreaAgentBinding;
import com.online.factory.factoryonline.models.ProMedium;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.util.List;

import javax.inject.Inject;

/**
 * 作者: GIndoc
 * 日期: 2017/1/19 16:03
 * 作用:
 */

public class AreaActivity extends BaseActivity<AreaContract.View, AreaPresenter> implements AreaContract.View, OnPageListener {

    private static final String BRANCH_ID = "BRANCH_ID";
    private ActivityAreaAgentBinding mBinding;
    private String next;

    @Inject
    AreaPresenter mPresenter;

    @Inject
    AreaRecyclerViewAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getComponent().inject(this);
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_area_agent);
        mBinding.setView(this);

        int branchId = getIntent().getIntExtra(BRANCH_ID, -1);
        if (branchId != -1) {
            mPresenter.requestAgent(getString(R.string.api) + "promediums/branches/" + branchId);
        }

        mBinding.recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected AreaPresenter createPresent() {
        return mPresenter;
    }

    @Override
    public <T> LifecycleTransformer<T> getBindToLifecycle() {
        return bindToLifecycle();
    }

    @Override
    public void showError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    public static Intent getStartIntent(Context context, int id) {
        Intent intent = new Intent(context, AreaActivity.class);
        intent.putExtra(BRANCH_ID, id);
        return intent;
    }

    @Override
    public void loadNextUrl(String next) {
        this.next = next;
    }

    @Override
    public void loadAgents(List<ProMedium> proMedium) {
        mAdapter.addData(proMedium);
        mBinding.recyclerView.notifyDataSetChanged();
    }

    @Override
    public void onPage() {
        if (!TextUtils.isEmpty(next)) {
            mPresenter.requestAgent(next);
        }else {
            showError("没有更多内容了");
            mBinding.recyclerView.setIsLoading(false);
        }
    }
}
