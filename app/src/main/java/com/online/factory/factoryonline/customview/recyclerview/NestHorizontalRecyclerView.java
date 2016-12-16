package com.online.factory.factoryonline.customview.recyclerview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 作者: GIndoc
 * 日期: 2016/12/16 11:12
 * 作用:
 */

public class NestHorizontalRecyclerView extends SuperRecyclerView {
    private float xDistance, yDistance, xLast, yLast;
    public NestHorizontalRecyclerView(Context context) {
        super(context);
    }

    public NestHorizontalRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NestHorizontalRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDistance = yDistance = 0f;
                xLast = e.getX();
                yLast = e.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float curX = e.getX();
                final float curY = e.getY();

                xDistance += Math.abs(curX - xLast);
                yDistance += Math.abs(curY - yLast);
                xLast = curX;
                yLast = curY;

                if (yDistance > xDistance) {
                    return false;
                }
        }
        return super.onInterceptTouchEvent(e);
    }
}
