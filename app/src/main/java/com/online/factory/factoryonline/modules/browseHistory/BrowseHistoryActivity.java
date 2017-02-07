package com.online.factory.factoryonline.modules.browseHistory;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.base.BaseActivity;
import com.online.factory.factoryonline.base.BasePresenter;
import com.online.factory.factoryonline.databinding.ActivityBrowseHistoryBinding;
import com.online.factory.factoryonline.models.WantedMessage;
import com.online.factory.factoryonline.modules.main.fragments.home.index.IndexRecyclerViewAdapter;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.util.List;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import timber.log.Timber;

/**
 * 作者: GIndoc
 * 日期: 2017/2/6 17:44
 * 作用:
 */

public class BrowseHistoryActivity extends BaseActivity<BrowseHistoryContract.View, BrowseHistoryPresenter> implements BrowseHistoryContract.View {

    @Inject
    IndexRecyclerViewAdapter mAdapter;

    @Inject
    BrowseHistoryPresenter mPresenter;

    private ActivityBrowseHistoryBinding mBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getComponent().inject(this);
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_browse_history);
        mBinding.setView(this);

        mBinding.recyclerView.setAdapter(mAdapter);

        mPresenter.requestHistory();

    }

    @Override
    protected BrowseHistoryPresenter createPresent() {
        return mPresenter;
    }

    public static Intent getStartIntent(Context context) {
        return new Intent(context, BrowseHistoryActivity.class);
    }

    @Override
    public void loadHistory(List<WantedMessage> wantedMessages) {
        mAdapter.addData(wantedMessages);
        mBinding.recyclerView.notifyDataSetChanged();

    }

    @Override
    public <T> LifecycleTransformer<T> getBindToLifecycle() {
        return bindToLifecycle();
    }

    @Override
    public void showError(String error) {
        Timber.e(error);
    }
}
