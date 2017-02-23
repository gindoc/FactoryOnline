package com.online.factory.factoryonline.modules.main.fragments.home.agent.areaAgent;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.base.BaseActivity;
import com.online.factory.factoryonline.customview.TitleBar;
import com.online.factory.factoryonline.customview.recyclerview.BaseRecyclerViewAdapter;
import com.online.factory.factoryonline.customview.recyclerview.OnPageListener;
import com.online.factory.factoryonline.databinding.ActivityAreaAgentBinding;
import com.online.factory.factoryonline.models.Branch;
import com.online.factory.factoryonline.models.ProMedium;
import com.online.factory.factoryonline.modules.agent.AgentActivity;
import com.online.factory.factoryonline.utils.StatusBarUtils;
import com.online.factory.factoryonline.utils.ToastUtil;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.util.List;

import javax.inject.Inject;

/**
 * 作者: GIndoc
 * 日期: 2017/1/19 16:03
 * 作用:
 */

public class AreaActivity extends BaseActivity<AreaContract.View, AreaPresenter> implements AreaContract.View, OnPageListener,
        TitleBar.OnTitleBarClickListener, BaseRecyclerViewAdapter.OnItemClickListener {

    private static final String BRANCH = "BRANCH";
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

        StatusBarUtils.from(this)
                //白底黑字状态栏
                .setLightStatusBar(true)
                .setTransparentStatusbar(true)
                //设置toolbar,actionbar等view
                .setActionbarView(mBinding.rlTitle)
                .process();
        mBinding.rlTitle.setOnTitleBarClickListener(this);

        Branch branch = (Branch) getIntent().getSerializableExtra(BRANCH);
        if (branch != null) {
            mPresenter.requestAgent(getString(R.string.api) + "promediums/branches/" + branch.getId());
            mBinding.rlTitle.setTitle(branch.getName());
        }

        mBinding.recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
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
        ToastUtil.show(this, error);
    }

    public static Intent getStartIntent(Context context, Branch branch) {
        Intent intent = new Intent(context, AreaActivity.class);
        intent.putExtra(BRANCH, branch);
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

    @Override
    public void onLeftButtonClickListener(View view) {
        finish();
    }

    @Override
    public void onRightButtonClickListener(View view) {

    }

    @Override
    public void onItemClick(View view, int position) {
        ProMedium proMedium = mAdapter.getData().get(position);
        startActivity(AgentActivity.getStartIntent(this, proMedium));
        overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
    }
}
