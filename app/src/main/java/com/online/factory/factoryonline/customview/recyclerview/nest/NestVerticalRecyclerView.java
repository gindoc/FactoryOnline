package com.online.factory.factoryonline.customview.recyclerview.nest;

import android.content.Context;
import android.util.AttributeSet;

import com.online.factory.factoryonline.customview.recyclerview.SuperRecyclerView;

/**
 * 作者: GIndoc
 * 日期: 2016/12/16 11:01
 * 作用:
 */

public class NestVerticalRecyclerView extends SuperRecyclerView {
    public NestVerticalRecyclerView(Context context) {
        this(context, null);
    }

    public NestVerticalRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NestVerticalRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
    /* do nothing */
    }
}
