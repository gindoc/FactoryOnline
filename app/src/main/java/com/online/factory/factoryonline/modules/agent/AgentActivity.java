package com.online.factory.factoryonline.modules.agent;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.base.BaseActivity;
import com.online.factory.factoryonline.customview.DividerItemDecoration;
import com.online.factory.factoryonline.customview.recyclerview.BaseRecyclerViewAdapter;
import com.online.factory.factoryonline.customview.recyclerview.OnPageListener;
import com.online.factory.factoryonline.databinding.ActivityAgentBinding;
import com.online.factory.factoryonline.databinding.LayoutAgentHeaderBinding;
import com.online.factory.factoryonline.models.ProMedium;
import com.online.factory.factoryonline.models.ProMediumMessage;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.util.List;

import javax.inject.Inject;

/**
 * 作者: GIndoc
 * 日期: 2016/12/6 9:50
 * 作用:
 */

public class AgentActivity extends BaseActivity<AgentContract.View, AgentPresenter> implements AgentContract.View, OnPageListener, BaseRecyclerViewAdapter.OnItemClickListener {
    public static final String PROMEDIUM = "PROMEDIUM";
    private ActivityAgentBinding mBinding;
    private LayoutAgentHeaderBinding mHeaderBinding;
    private String next;

    @Inject
    AgentPresenter mPresenter;

    @Inject
    AgentRecyclerViewAdapter mAdapter;

    @Inject
    AgentViewModel viewModel;

    public static Intent getStartIntent(Context context, ProMedium proMedium) {
        Intent intent = new Intent(context, AgentActivity.class);
        intent.putExtra(PROMEDIUM, proMedium);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getComponent().inject(this);
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_agent);
        mHeaderBinding = LayoutAgentHeaderBinding.inflate(LayoutInflater.from(this), (ViewGroup) mBinding.getRoot(), false);
        mBinding.setView(this);

        initAgent();
        initRecyclerView();
    }

    private void initAgent() {
        ProMedium proMedium = (ProMedium) getIntent().getSerializableExtra(PROMEDIUM);
        viewModel.setProMedium(proMedium);
        mHeaderBinding.setViewModel(viewModel);

        mPresenter.requestProMediumMessages(getString(R.string.api)+"promediums/messages/"+proMedium.getId());
    }

    private void initRecyclerView() {
        mBinding.recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        mBinding.recyclerView.setAdapter(mAdapter);
        mBinding.recyclerView.addHeader(mHeaderBinding.getRoot());
        mBinding.recyclerView.setOnPageListener(this);
        mBinding.recyclerView.setPageFooter(R.layout.layout_recyclerview_footer);
        mAdapter.setOnItemClickListener(this);
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
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loadProMediumMessage(List<ProMediumMessage> proMediumMessage) {
        mAdapter.addData(proMediumMessage);
        mBinding.recyclerView.notifyDataSetChanged();
        mBinding.recyclerView.hideLoadingFooter();
    }

    @Override
    public void loadNextUrlAndCount(String next, int count) {
        this.next = next;
        mHeaderBinding.tvCount.setText("( " + count + " )");
    }

    @Override
    public void onPage() {
        mBinding.recyclerView.showLoadingFooter();
        if (!TextUtils.isEmpty(next)) {
            mPresenter.requestProMediumMessages(next);
        }else {
            mBinding.recyclerView.hideLoadingFooter();
            showError("没有更多数据了");
        }
    }

    @Override
    public void onItemClick(View view, int position) {

    }
}
