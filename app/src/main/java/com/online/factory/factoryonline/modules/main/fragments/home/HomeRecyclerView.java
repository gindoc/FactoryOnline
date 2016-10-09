package com.online.factory.factoryonline.modules.main.fragments.home;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.customview.recyclerview.DecorateRecyclerViewAdapter;
import com.online.factory.factoryonline.customview.recyclerview.SuperRecyclerView;

/**
 * Created by cwenhui on 2016.02.23
 */
public class HomeRecyclerView extends SuperRecyclerView {
    public HomeRecyclerView(Context context) {
        this(context, null);
    }

    public HomeRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HomeRecyclerView(final Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void init() {
        DecorateRecyclerViewAdapter adapter = getBookendsAdapter();

        addOnScrollListener(new RecyclerView.OnScrollListener() {
            private int totalDy = 0;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalDy += dy;
                scrollChangedListener.onScrolled(totalDy);
            }
        });
    }

    private int getScrolledDistance() {
//        LinearLayoutManager layoutManager = (LinearLayoutManager) getLayoutManager();
//        View firstVisibleItem = this.getChildAt(0);
//        int firstItemPosition = layoutManager.findFirstVisibleItemPosition();
//        int itemHeight = firstVisibleItem.getHeight();
//        int firstItemBottom = layoutManager.getDecoratedBottom(firstVisibleItem);
//        Timber.d("firstItemBottom  %d" , firstItemBottom);
//        return firstItemBottom == 0? 0 :(firstItemPosition + 1) * itemHeight - firstItemBottom;
        LinearLayoutManager layoutManager = (LinearLayoutManager) this.getLayoutManager();
        int position = layoutManager.findFirstVisibleItemPosition();
        View firstVisiableChildView = layoutManager.findViewByPosition(position);
        int itemHeight = firstVisiableChildView.getHeight();
        return (position) * itemHeight - firstVisiableChildView.getTop();
    }

    interface ScrollChangedListener{
        void onScrolled(int dy);
    }

    private ScrollChangedListener scrollChangedListener;

    public void setScrollChangedListener(ScrollChangedListener scrollChangedListener) {
        this.scrollChangedListener = scrollChangedListener;
    }
}
