package com.online.factory.factoryonline.customview.recyclerview;

/**
 * Created by louiszgm-pc on 2016/5/2.
 */

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;


import timber.log.Timber;

/**
 * A super recycler view that has implemented paging at bottom, paging at top, adding header and adding footer.
 * <p/>
 * Created by Eric on 16/3/15.
 */
public class SuperRecyclerView extends LinearRecyclerView implements LinearRecyclerView.OnScrollPositionListener {

    private View loadingFooter;

    private boolean hasAttachedFooter = false;
    private boolean pageEnable = true;
    private boolean isLoading = false;
    private DecorateRecyclerViewAdapter decorateRecyclerViewAdapter;
    private OnPageListener onPageListener;
    private OnTopPageListener onTopPageListener;

    public SuperRecyclerView(Context context) {
        this(context, null);
    }

    public SuperRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SuperRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void setPageEnable(boolean pageEnable) {
        this.pageEnable = pageEnable;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setIsLoading(boolean b) {
        isLoading = b;
    }

    private void init() {
        setOnScrollPositionListener(this);
    }

    public void setOnPageListener(OnPageListener pageListener) {
        this.onPageListener = pageListener;
    }

    public void setOnTopPageListener(OnTopPageListener onTopPageListener) {
        this.onTopPageListener = onTopPageListener;
    }

    @Override
    public void onScrollToTop() {
        if (pageEnable && onTopPageListener != null && !isLoading) {
            isLoading = true;
            onTopPageListener.onTopPage();
        }
    }

    @Override
    public void onScrollToBottom() {
        Timber.d("onScrollToBottom,isLoading:%s", isLoading);
        if (pageEnable && onPageListener != null && !isLoading) {
            isLoading = true;
            onPageListener.onPage();
        }
    }

    public void notifyDataSetChanged() {
        decorateRecyclerViewAdapter.notifyDataSetChanged();
    }

    public void notifyItemInserted(int position) {
        decorateRecyclerViewAdapter.notifyItemInserted(position);
    }
    public void notifyItemChanged(int position){
        decorateRecyclerViewAdapter.notifyItemChanged(position);
    }

    public void notifyItemRemoved(int position) {
        decorateRecyclerViewAdapter.notifyItemRemoved(position);
    }

    public void setAdapter(BaseRecyclerViewAdapter adapter) {
        decorateRecyclerViewAdapter = new DecorateRecyclerViewAdapter(adapter);
        super.setAdapter(decorateRecyclerViewAdapter);
    }

    public void setAdapter(DecorateRecyclerViewAdapter adapter) {
        super.setAdapter(adapter);
        this.decorateRecyclerViewAdapter = adapter;
    }

    public DecorateRecyclerViewAdapter<?> getBookendsAdapter() {
        return decorateRecyclerViewAdapter;
    }

    /**
     * Add a header view.
     *
     * @param view
     */
    public void addHeader(View view) {
        decorateRecyclerViewAdapter.addHeader(view);
    }

    /**
     * Add a footer view.
     *
     * @param view
     */
    public void addFooter(View view) {
        decorateRecyclerViewAdapter.addFooter(view);
    }

    public void setPageFooter(View view) {
        loadingFooter = view;
    }

    public void setPageFooter(int resId) {
        loadingFooter = LayoutInflater.from(getContext()).inflate(resId, null);
    }

    private void attachPageFooter() {
        if (loadingFooter != null && !hasAttachedFooter) {
            hasAttachedFooter = true;
            decorateRecyclerViewAdapter.addFooter(loadingFooter);
            decorateRecyclerViewAdapter.setFooterVisibility(loadingFooter, false);
        }
    }

    /**
     * Remove the footer after load finished.
     */
    public void removePageFooter() {
        if (loadingFooter != null && hasAttachedFooter) {
            hasAttachedFooter = false;
            decorateRecyclerViewAdapter.removeFooter(loadingFooter);
        }
    }

    public void showLoadingFooter() {
        if (!hasAttachedFooter) {
            attachPageFooter();
        }

        if (loadingFooter != null)
            decorateRecyclerViewAdapter.setFooterVisibility(loadingFooter, true);
    }

    public void hideLoadingFooter() {
        if (hasAttachedFooter) {
            isLoading = false;
//            hasAttachedFooter = false;
            decorateRecyclerViewAdapter.setFooterVisibility(loadingFooter, false);
        }
    }

}
