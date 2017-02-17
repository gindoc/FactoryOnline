package com.online.factory.factoryonline.modules.search;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewTextChangeEvent;
import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.base.BaseActivity;
import com.online.factory.factoryonline.databinding.ActivitySearchBinding;
import com.online.factory.factoryonline.models.SearchResult;
import com.online.factory.factoryonline.modules.search.agentResult.SearchResultActivity;
import com.online.factory.factoryonline.utils.StatusBarUtils;
import com.online.factory.factoryonline.utils.ToastUtil;
import com.online.factory.factoryonline.utils.rx.RxSubscriber;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import timber.log.Timber;

/**
 * Author: GIndoc on 2016/11/15 21:28
 * email : 735506583@qq.com
 * FOR   :
 */
public class SearchActivity extends BaseActivity<SearchContract.View, SearchPresenter> implements SearchContract.View {

    @Inject
    SearchPresenter mPresenter;

    private ActivitySearchBinding mBinding;
    private int OWNER_CONTENT_ID;
    private int AGENT_CONTENT_ID;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, SearchActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getComponent().inject(this);
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_search);
        mBinding.setView(this);
        mBinding.setPresenter(mPresenter);
        StatusBarUtils.from(this)
                //沉浸状态栏
                .setTransparentStatusbar(true)
                //白底黑字状态栏
                .setLightStatusBar(true)
                //设置toolbar,actionbar等view
                .setActionbarView(mBinding.llTopBar)
                .process();
        mPresenter.loadSearchHistory();

        attachListenerForEdittext();

    }


    public void cancelSearch() {
        mBinding.etSearch.setText("");
        mBinding.llSearchHistory.setVisibility(View.VISIBLE);
        mBinding.llSearchResult.setVisibility(View.GONE);
        mBinding.ivClear.setVisibility(View.GONE);
        mPresenter.loadSearchHistory();
    }

    @Override
    protected SearchPresenter createPresent() {
        return mPresenter;
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
    public void loadSearchList(List<SearchResult> searchResults) {
        if (searchResults != null && searchResults.size() > 0) {
            mBinding.llSearchHistory.setVisibility(View.GONE);
            mBinding.llSearchResult.setVisibility(View.VISIBLE);
            SearchResult ownerResult = searchResults.get(0);
            SearchResult agentResult = searchResults.get(1);
            OWNER_CONTENT_ID = ownerResult.getContentId();
            AGENT_CONTENT_ID = agentResult.getContentId();
            mBinding.tvOwnerMsgCount.setText(ownerResult.getCount() + "");
            mBinding.tvAgentMsgCount.setText(agentResult.getCount() + "");
        } else {
            mPresenter.loadSearchHistory();
            ToastUtil.show(this, "搜索不到相关结果");
        }
    }

    public void openOwnerMsgList() {
        startActivity(com.online.factory.factoryonline.modules.search.ownerResult.SearchResultActivity.getStartIntent(this, OWNER_CONTENT_ID));
    }

    public void openAgentMsgList() {
        startActivity(SearchResultActivity.getStartIntent(this, AGENT_CONTENT_ID));
    }

    private void attachListenerForEdittext() {
        RxTextView.textChangeEvents(mBinding.etSearch)
                .debounce(400, TimeUnit.MILLISECONDS)
                .filter(new Func1<TextViewTextChangeEvent, Boolean>() {
                    @Override
                    public Boolean call(TextViewTextChangeEvent textViewTextChangeEvent) {
                        return !TextUtils.isEmpty(textViewTextChangeEvent.text());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<TextViewTextChangeEvent>() {
                    @Override
                    public void _onNext(TextViewTextChangeEvent textViewTextChangeEvent) {
                        mBinding.ivClear.setVisibility(View.VISIBLE);
                        mPresenter.search(textViewTextChangeEvent.text().toString());
                    }

                    @Override
                    public void _onError(Throwable throwable) {
                        Timber.e(throwable.getMessage());
                    }
                });

        mBinding.etSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    mPresenter.cacheHistory(mBinding.etSearch.getText().toString());
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);        // 隐藏软键盘
                    return true;
                } else if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (mBinding.etSearch.getText().length() == 1) {
                        mBinding.ivClear.setVisibility(View.GONE);
                        cancelSearch();
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void initSearchHistory(Set<String> history) {
        mBinding.flexbox.removeAllViews();
        if (history == null || history.size() == 0) {
            mBinding.tvClear.setVisibility(View.GONE);
            mBinding.tvTips.setText("暂无搜索历史");
            return;
        }
        mBinding.tvClear.setVisibility(View.VISIBLE);
        mBinding.tvTips.setText("搜索记录");
        for (String s : history) {
            final TextView tvHistory = new TextView(this);
            FlexboxLayout.LayoutParams lp = new FlexboxLayout.LayoutParams(FlexboxLayout.LayoutParams.WRAP_CONTENT, FlexboxLayout.LayoutParams.WRAP_CONTENT);
            Resources resources = getResources();
            int margin = (int) resources.getDimension(R.dimen.x10);
            int topPadding = (int) resources.getDimension(R.dimen.x6);
            int leftPadding = (int) resources.getDimension(R.dimen.x15);
            lp.setMargins(0, margin, margin, 0);
            tvHistory.setLayoutParams(lp);
            tvHistory.setGravity(Gravity.CENTER);
            tvHistory.setPadding(leftPadding, topPadding, leftPadding, topPadding);
            tvHistory.setBackground(ResourcesCompat.getDrawable(resources, R.drawable.gray_rectangle_outline_with_corner, null));
            tvHistory.setTextColor(Color.parseColor("#424242"));
            tvHistory.setTextSize(16);
            tvHistory.setText(s);
            tvHistory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mBinding.etSearch.setText(tvHistory.getText());
                }
            });
            mBinding.flexbox.addView(tvHistory);
        }
    }
}
