package com.online.factory.factoryonline.customview.scrollview;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Scroller;

import com.online.factory.factoryonline.R;

import timber.log.Timber;

public class RefreshScrollView extends ScrollView {

    private final static int SCROLL_DURATION = 400;
    private final static float OFFSET_RADIO = 1.8f;
    private int headerHeight = 0;
    private boolean enableRefresh = true;
    private boolean refreshing = false;
    private float lastX, lastY;
    private Scroller scroller = null;
    private OnRefreshScrollViewListener listener = null;
    private LinearLayout scrollContainer = null;
//    private ScrollViewHeader headerView = null;
    private ScrollViewFrameHeader headerView = null;
    private View searchView;
    private int mTouchSlop;
    private float xDistance, yDistance;     // 水平/竖直滑动距离

    public RefreshScrollView(Context context) {
        this(context, null);
    }

    public RefreshScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 初始化view
     */
    private void initView(Context context) {
        scroller = new Scroller(context);
        headerView = new ScrollViewFrameHeader(context);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();       // 触发移动事件的最短距离，如果小于这个距离就不触发移动控件
        LinearLayout.LayoutParams headerViewParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //scrollview只允许嵌套一个子布局
        scrollContainer = new LinearLayout(context);
        scrollContainer.addView(headerView, headerViewParams);
        scrollContainer.setOrientation(LinearLayout.VERTICAL);
        addView(scrollContainer);
        //提前获取headerView的高度
        headerView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {

                    @SuppressWarnings("deprecation")
                    @Override
                    public void onGlobalLayout() {
                        headerHeight = headerView.getHeight();
                        headerView.updateMargin(-headerHeight);
                        headerView.getViewTreeObserver()
                                .removeGlobalOnLayoutListener(this);
                    }
                });
    }

    /**
     * 设置内容区域
     *
     * @param context
     */
    public void setupContainer(Context context, View containerView) {
        scrollContainer.addView(containerView);
    }

    public void setupSearchView(View searchView) {
        this.searchView = searchView;
    }

    /**
     * 设置scroll是否可以刷新
     *
     * @param enableRefresh
     */
    public void setEnableRefresh(boolean enableRefresh) {
        this.enableRefresh = enableRefresh;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        int action = e.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                xDistance = yDistance = 0f;
                lastX = e.getRawX();
                lastY = e.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float curX = e.getRawX();
                final float curY = e.getRawY();
                xDistance += Math.abs(curX - lastX);
                yDistance += Math.abs(curY - lastY);
                lastX = curX;
                if (xDistance > yDistance) {        // 水平滑动距离大于竖直滑动距离时，不消费，给子view消费
                    return false;
                }
                if (Math.abs(curY - lastY) > mTouchSlop) {      // 当竖直滑动距离超过mTouchSlop时，自己消费，解决scrollview嵌套recyclerview滑动卡顿的问题
                    return true;
                }
                lastY = curY;
        }
        return super.onInterceptTouchEvent(e);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
        case MotionEvent.ACTION_DOWN:
            lastY = (int) ev.getY();
            break;
        case MotionEvent.ACTION_MOVE:
            int deltY = (int) (ev.getY() - lastY);
            lastY = (int) ev.getY();
            Timber.d("getScrollY:" + getScrollY());
//            if (getScrollY() == 0 && deltY > 0 && deltY < searchView.getHeight()) {
//                return false;
//            }
            if (getScrollY() == 0
                    && (deltY > 0 || headerView.getTopMargin() > -headerHeight)) {
                updateHeader(deltY/OFFSET_RADIO);
                return true;
            }
            break;
        default:
            //这里没有使用action_up的原因是，可能会受到viewpager的影响接收到action_cacel事件
            Timber.d("ev.getAction: " +ev.getAction());
            if (getScrollY() == 0) {
                Timber.d("topMargin():" + headerView.getTopMargin());
                if (headerView.getTopMargin() > 0 && enableRefresh && !refreshing) {
                    refreshing = true;
                    headerView.setState(ScrollViewHeader.STATE_REFRESHING);
//                    new Handler().postDelayed(new Runnable() {
//
//                        @Override
//                        public void run() {
//                            if(listener != null) {
//                                listener.onRefresh();
//                                refreshing = false;
////                                ShowUtils.shortShow("更新成功");
//                                resetHeaderView();
//                            }
//                        }
//                    }, 3000);
                    startRefresh();
                }
//                Timber.d("resetHeaderView...");
                resetHeaderView();
            }
            break;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 更新headerview的高度,同时更改状态
     *
     * @param deltY
     */
    public void updateHeader(float deltY) {
        int currentMargin = (int) (headerView.getTopMargin() + deltY);
        headerView.updateMargin(currentMargin);
        if(enableRefresh && !refreshing) {
            if (currentMargin > 0) {
                headerView.setState(ScrollViewHeader.STATE_READY);
            } else {
                headerView.setState(ScrollViewHeader.STATE_NORMAL);
            }
        }
    }

    /**
     * 重置headerview的高度
     */
    public void resetHeaderView() {
        int margin = headerView.getTopMargin();
        if(margin == -headerHeight) {
            return ;
        }
        if(margin < 0 && refreshing) {
            //当前已经在刷新，又重新进行拖动,但未拖满,不进行操作
            return ;
        }
        int finalMargin = 0;
        if(margin <= 0 && !refreshing) {
            finalMargin = headerHeight;
        }
        Timber.d("margin: " + margin);
        Timber.d("finalMargin: " + finalMargin);
        //松开刷新，或者下拉刷新，又松手，没有触发刷新
        scroller.startScroll(0, -margin, 0, finalMargin + margin, SCROLL_DURATION);

        invalidate();
    }

    /**
     * 开始刷新
     */
    public void startRefresh() {
        refreshing = true;
        headerView.setState(ScrollViewHeader.STATE_REFRESHING);
        if(listener != null) {
            Timber.d("xxx: " + headerHeight);
            scroller.startScroll(0, 0, 0, headerHeight, SCROLL_DURATION);
            invalidate();
            listener.onRefresh();
        }
    }

    /**
     * 停止刷新
     */
    public void stopRefresh() {
        if(refreshing) {
            refreshing = false;
            resetHeaderView();
        }
    }

    @Override
    public void computeScroll() {
        if(scroller.computeScrollOffset()) {
            Timber.d("getCurrY: " + scroller.getCurrY());
            headerView.updateMargin(-scroller.getCurrY());  
            //继续重绘  
            postInvalidate();  
        }  
        super.computeScroll();  
    }  
      
    public void setOnRefreshScrollViewListener(OnRefreshScrollViewListener listener) {  
        this.listener = listener;  
    }  
      
    public interface OnRefreshScrollViewListener {  
        public void onRefresh();  
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        scrollChangeListener.onScrollChange(this, l, t, oldl, oldt);
    }

    private ScrollChangeListener scrollChangeListener;

    public void setScrollChangeListener(ScrollChangeListener scrollChangeListener) {
        this.scrollChangeListener = scrollChangeListener;
    }

    public interface ScrollChangeListener{
        void onScrollChange(ScrollView scrollView, int l, int t, int oldl, int oldt);
    }
}