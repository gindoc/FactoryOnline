package com.online.factory.factoryonline.modules.search;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.flexbox.FlexboxLayout;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewTextChangeEvent;
import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.base.BaseActivity;
import com.online.factory.factoryonline.databinding.ActivitySearchBinding;
import com.online.factory.factoryonline.models.WantedMessage;
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

    @Inject
    SearchAdapter mAdapter;

    private ActivitySearchBinding mBinding;


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

        mBinding.recyclerView.setAdapter(mAdapter);
        mPresenter.loadSearchHistory();

        attachListenerForEdittext();
    }


    public void cancelSearch(View view) {
        mBinding.etSearch.setText("");
        mBinding.llSearchHistory.setVisibility(View.VISIBLE);
        mBinding.recyclerView.setVisibility(View.GONE);
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
    public void initSearchHistory(Set<String> history) {
        mBinding.flexbox.removeAllViews();
        if (history==null || history.size() == 0) {
            mBinding.tvClear.setVisibility(View.GONE);
            mBinding.tvTips.setText("暂无搜索历史");
            return;
        }
        for (String s : history) {
            TextView tvHistory = new TextView(this);
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
            mBinding.flexbox.addView(tvHistory);
        }
    }

    @Override
    public void loadSearchList(List<WantedMessage> wantedMessages) {
        if (wantedMessages != null && wantedMessages.size() > 0) {
            mBinding.llSearchHistory.setVisibility(View.GONE);
            mBinding.recyclerView.setVisibility(View.VISIBLE);
            mAdapter.getData().clear();
            mAdapter.setData(wantedMessages);
            mBinding.recyclerView.notifyDataSetChanged();
        }else {
            mPresenter.loadSearchHistory();
            mAdapter.getData().clear();
            mBinding.recyclerView.notifyDataSetChanged();
            Toast.makeText(this,"搜索不到相关结果",Toast.LENGTH_SHORT).show();
        }
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
                        mPresenter.search(textViewTextChangeEvent.text().toString());
                    }

                    @Override
                    public void _onError(Throwable throwable) {
                        Timber.e(throwable.getMessage());
                    }
                });
        mBinding.etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    mPresenter.cacheHistory(v.getText().toString());
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);        // 隐藏软键盘
//                    mPresenter.loadSearchHistory();
                    return true;
                }
                return false;
            }
        });
    }
}
