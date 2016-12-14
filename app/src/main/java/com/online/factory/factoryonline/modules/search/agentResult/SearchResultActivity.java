package com.online.factory.factoryonline.modules.search.agentResult;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.base.BaseActivity;
import com.online.factory.factoryonline.customview.recyclerview.BaseRecyclerViewAdapter;
import com.online.factory.factoryonline.customview.recyclerview.OnPageListener;
import com.online.factory.factoryonline.databinding.ActivityAgentSearchResultBinding;
import com.online.factory.factoryonline.models.ProMediumMessage;
import com.online.factory.factoryonline.modules.agentFactoryDetail.FactoryDetailActivity;
import com.online.factory.factoryonline.utils.StatusBarUtils;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.util.List;

import javax.inject.Inject;

/**
 * 作者: GIndoc
 * 日期: 2016/12/12 14:53
 * 作用:
 */

public class SearchResultActivity extends BaseActivity<SearchResultContract.View, SearchResultPresenter> implements SearchResultContract.View, OnPageListener, BaseRecyclerViewAdapter.OnItemClickListener {
    private static final String CONTENT_ID = "CONTENT_ID";
    @Inject
    SearchResultPresenter mPresenter;

    @Inject
    SearchResultAdapter mAdapter;
    private ActivityAgentSearchResultBinding mBinding;
    private String next;

    public static Intent getStartIntent(Context context, int contentId) {
        Intent intent = new Intent();
        intent.putExtra(CONTENT_ID, contentId);
        intent.setClass(context, SearchResultActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getComponent().inject(this);
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_agent_search_result);
        StatusBarUtils.from(this)
                //沉浸状态栏
                .setTransparentStatusbar(true)
                //白底黑字状态栏
                .setLightStatusBar(true)
                //设置toolbar,actionbar等view
                .setActionbarView(mBinding.rlTopBar)
                .process();
        mBinding.recyclerView.setAdapter(mAdapter);
        mBinding.recyclerView.setPageFooter(R.layout.layout_recyclerview_footer);
        mAdapter.setOnItemClickListener(this);

        mPresenter.requestSearchResult(getString(R.string.api)+"search/contents/"+getIntent().getIntExtra(CONTENT_ID, 0));
    }

    @Override
    protected SearchResultPresenter createPresent() {
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
    public void loadSearchResult(List<ProMediumMessage> proMediumMessage) {
        mAdapter.addData(proMediumMessage);
        mBinding.recyclerView.notifyDataSetChanged();
    }

    @Override
    public void loadNextUrl(String next) {
        this.next = next;
    }

    @Override
    public void onPage() {
        if (!TextUtils.isEmpty(next)) {
            mPresenter.requestSearchResult(next);
        }else {
            showError("没有更多内容了");
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        startActivity(FactoryDetailActivity.getStartIntent(this, mAdapter.getData().get(position), null));
    }
}