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
            private boolean isSwipeDown;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalDy += dy;
                if(dy <0 ){
                    isSwipeDown = true;
                }else if(dy >0){
                    isSwipeDown = false;
                }
                scrollChangedListener.onScrolled(totalDy,isSwipeDown);
            }
        });
    }



    interface ScrollChangedListener{
        void onScrolled(int dy , boolean isSwipeDown);
    }

    private ScrollChangedListener scrollChangedListener;

    public void setScrollChangedListener(ScrollChangedListener scrollChangedListener) {
        this.scrollChangedListener = scrollChangedListener;
    }
}
