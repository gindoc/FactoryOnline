package com.online.factory.factoryonline.modules.search.ownerResult;

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
import com.online.factory.factoryonline.databinding.ActivityOwnerSearchResultBinding;
import com.online.factory.factoryonline.models.WantedMessage;
import com.online.factory.factoryonline.modules.FactoryDetail.FactoryDetailActivity;
import com.online.factory.factoryonline.utils.StatusBarUtils;
import com.online.factory.factoryonline.utils.ToastUtil;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.util.List;

import javax.inject.Inject;

/**
 * 作者: GIndoc
 * 日期: 2016/12/12 14:53
 * 作用:
 */

public class SearchResultActivity extends BaseActivity<SearchResultContract.View, SearchResultPresenter> implements SearchResultContract.View,
        OnPageListener, TitleBar.OnTitleBarClickListener, BaseRecyclerViewAdapter.OnItemClickListener {
    private static final String CONTENT_ID = "CONTENT_ID";
    @Inject
    SearchResultPresenter mPresenter;

    @Inject
    SearchResultAdapter mAdapter;
    private ActivityOwnerSearchResultBinding mBinding;
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

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_owner_search_result);
        mBinding.setView(this);

        initToolbar();
        initRecyclerView();

        mPresenter.requestSearchResult(getString(R.string.api)+"search/contents/"+getIntent().getIntExtra(CONTENT_ID, 0));
    }


    private void initToolbar() {
        StatusBarUtils.from(this)
                //白底黑字状态栏
                .setLightStatusBar(true)
                //设置toolbar,actionbar等view
                .setTransparentStatusbar(true)
                .setActionbarView(mBinding.rlTitle)
                .process();
        mBinding.rlTitle.setOnTitleBarClickListener(this);
    }

    private void initRecyclerView() {
        mBinding.recyclerView.setAdapter(mAdapter);
        mBinding.recyclerView.setOnPageListener(this);
        mAdapter.setOnItemClickListener(this);
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
        ToastUtil.show(this, error);
    }

    @Override
    public void loadSearchResult(List<WantedMessage> wantedMessages) {
        mAdapter.addData(wantedMessages);
        mBinding.recyclerView.notifyDataSetChanged();
        mBinding.recyclerView.setIsLoading(false);
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
        Intent intent = new Intent();
        intent.setClass(this, FactoryDetailActivity.class);
        intent.putExtra(FactoryDetailActivity.WANTED_MESSAGE, mAdapter.getData().get(position));
        startActivity(intent);
    }
}
