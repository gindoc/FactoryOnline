package com.online.factory.factoryonline.customview.recyclerview;

/**
 * Created by louiszgm-pc on 2016/5/2.
 */

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * A RecyclerView using LinearLayoutManager or GridLayoutManager.
 * <p>
 * This RecycleView will listening itself whether scroll at the top or bottom.
 * </p>
 */
public class LinearRecyclerView extends RecyclerView {

    private boolean isBottom;
    private boolean isTop;

    private int firstVisibleItem;
    private int visibleItemCount;
    private int totalItemCount;

    private OnScrollPositionListener onScrollPositionListener;
    private OnScrollListener onScrollListener;

    private LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext());
    public void setOnScrollPositionListener(OnScrollPositionListener listener) {
        onScrollPositionListener = listener;
    }

    /**
     * 设置LinearLayoutManager的orientation
     * 不设置默认为vertical
     */
    public void setOrientation(int orientation){
        mLinearLayoutManager.setOrientation(orientation);
    }
    public void setOnScrollListener(OnScrollListener listener) {
        onScrollListener = listener;
    }

    public LinearRecyclerView(Context context) {
        super(context);
        init();
    }

    public LinearRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LinearRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setLayoutManager(mLinearLayoutManager);
        addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE && onScrollPositionListener != null) {
                    if (isBottom) onScrollPositionListener.onScrollToBottom();
                    if (isTop) onScrollPositionListener.onScrollToTop();
                }

                if (onScrollListener != null) {
                    onScrollListener.onScrollStateChanged(recyclerView, newState);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (onScrollPositionListener != null) {
                    visibleItemCount = recyclerView.getChildCount();
                    totalItemCount = getLayoutManager().getItemCount();
                    firstVisibleItem = getFirstVisibleItem();

                    isBottom = (firstVisibleItem + visibleItemCount) >= (totalItemCount - 1);
                    isTop = firstVisibleItem == 0;
                }

                if (onScrollListener != null) {
                    onScrollListener.onScrolled(recyclerView, dx, dy);
                }
            }
        });
    }

    private int getFirstVisibleItem() {
        LayoutManager layoutManager = getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            return ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
        } else {
            return ((GridLayoutManager) layoutManager).findFirstVisibleItemPosition();
        }
    }

    public interface OnScrollPositionListener {
        void onScrollToTop();

        void onScrollToBottom();
    }

    public interface OnScrollListener {
        void onScrollStateChanged(RecyclerView recyclerView, int newState);

        void onScrolled(RecyclerView recyclerView, int dx, int dy);
    }

}