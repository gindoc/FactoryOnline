package com.online.factory.factoryonline.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.ScrollView;

/**
 * 作者: GIndoc
 * 日期: 2016/12/19 15:45
 * 作用:
 */

public class InterceptScrollView extends ScrollView {
    private float xDistance, yDistance;     // 水平/竖直滑动距离
    private float downX, downY;              // 点击屏幕时的坐标
    private int mTouchSlop;

    public InterceptScrollView(Context context) {
        this(context, null);
    }

    public InterceptScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public InterceptScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();       // 触发移动事件的最短距离，如果小于这个距离就不触发移动控件
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        int action = e.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                xDistance = yDistance = 0f;
                downX = (int) e.getRawX();
                downY = (int) e.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float curX = e.getRawX();
                final float curY = e.getRawY();
                xDistance += Math.abs(curX - downX);
                yDistance += Math.abs(curY - downY);
                downX = curX;
                if (xDistance > yDistance) {        // 水平滑动距离大于竖直滑动距离时，不消费，给子view消费
                    return false;
                }
                if (Math.abs(curY - downY) > mTouchSlop) {      // 当竖直滑动距离超过mTouchSlop时，自己消费，解决scrollview嵌套recyclerview滑动卡顿的问题
                    return true;
                }
                downY = curY;
        }
        return super.onInterceptTouchEvent(e);
    }

    public interface ScrollViewListener{
        void onScrollChange(ScrollView scrollView, int x, int y, int oldx, int oldy);
    }

    private ScrollViewListener scrollViewListener = null;

    public void setScrollViewListener(ScrollViewListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (scrollViewListener != null) {
            scrollViewListener.onScrollChange(this, l, t, oldl, oldt);
        }
    }
}
