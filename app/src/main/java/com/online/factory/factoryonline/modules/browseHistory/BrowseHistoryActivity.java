package com.online.factory.factoryonline.modules.browseHistory;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.base.BaseActivity;
import com.online.factory.factoryonline.base.BasePresenter;
import com.online.factory.factoryonline.customview.TitleBar;
import com.online.factory.factoryonline.customview.recyclerview.OnPageListener;
import com.online.factory.factoryonline.databinding.ActivityBrowseHistoryBinding;
import com.online.factory.factoryonline.models.WantedMessage;
import com.online.factory.factoryonline.modules.login.LoginContext;
import com.online.factory.factoryonline.modules.main.fragments.home.index.IndexRecyclerViewAdapter;
import com.online.factory.factoryonline.utils.StatusBarUtils;
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

public class BrowseHistoryActivity extends BaseActivity<BrowseHistoryContract.View, BrowseHistoryPresenter> implements BrowseHistoryContract.View,
        TitleBar.OnTitleBarClickListener, OnPageListener{

    @Inject
    IndexRecyclerViewAdapter mAdapter;

    @Inject
    BrowseHistoryPresenter mPresenter;

    @Inject
    LoginContext loginContext;

    private ActivityBrowseHistoryBinding mBinding;
    private String next = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getComponent().inject(this);
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_browse_history);
        mBinding.setView(this);
        StatusBarUtils.from(this)
                //白底黑字状态栏
                .setLightStatusBar(true)
                .setTransparentStatusbar(true)
                //设置toolbar,actionbar等view
                .setActionbarView(mBinding.rlTitle)
                .process();
        mBinding.rlTitle.setOnTitleBarClickListener(this);
        mBinding.recyclerView.setAdapter(mAdapter);
        mBinding.recyclerView.setOnPageListener(this);

        mPresenter.requestHistory(getResources().getString(R.string.api)+"user/historys/wantedmessages");

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
        int lastRange = mAdapter.getItemCount();
        if (mAdapter.getItemCount() == 0) {
            mAdapter.addData(wantedMessages);
            mBinding.recyclerView.notifyDataSetChanged();
        }else {
            mAdapter.notifyItemRangeInserted(lastRange, wantedMessages.size());
            mAdapter.addData(wantedMessages);
//            mAdapter.notifyItemRangeChanged(lastRange, wantedMessages.size());
        }
    }

    @Override
    public void loadNext(String next) {
        this.next = next;
    }

    @Override
    public void unLogin() {
        new AlertDialog.Builder(this)
                .setMessage(R.string.unLogin)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        loginContext.openUserDetail(BrowseHistoryActivity.this);
                    }
                }).create().show();
    }

    @Override
    public <T> LifecycleTransformer<T> getBindToLifecycle() {
        return bindToLifecycle();
    }

    @Override
    public void showError(String error) {
        Timber.e(error);
    }

    @Override
    public void onLeftButtonClickListener(View view) {
        finish();
    }

    @Override
    public void onRightButtonClickListener(View view) {
    }

    @Override
    public void onPage() {
        if (!TextUtils.isEmpty(next)) {
            mPresenter.requestHistory(next);
        }
    }
}
