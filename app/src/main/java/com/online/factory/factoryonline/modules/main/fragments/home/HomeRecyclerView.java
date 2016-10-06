package com.online.factory.factoryonline.modules.main.fragments.home;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

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
        final View header = adapter.getHeader(0);
        final View searchView = header.findViewById(R.id.scroll_txt_view);

        addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int[] pos = new int[2];
                searchView.getLocationInWindow(pos);
                if (pos[1] <= 0) {
                    Toast.makeText(getContext(), "get y = " + pos[1], Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private int getScrolledDistance() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) getLayoutManager();
        View firstVisibleItem = this.getChildAt(0);
        int firstItemPosition = layoutManager.findFirstVisibleItemPosition();
        int itemHeight = firstVisibleItem.getHeight();
        int firstItemBottom = layoutManager.getDecoratedBottom(firstVisibleItem);
        return (firstItemPosition + 1) * itemHeight - firstItemBottom;
    }

}
