package com.online.factory.factoryonline.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextSwitcher;
import android.widget.ViewSwitcher;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cwenhui on 2016.02.23
 */
public class ScrollTextView extends TextSwitcher implements ViewSwitcher.ViewFactory {
    /** 滚动开始标志 **/
    private static final int FLAG_START = 1001;
    /** 滚动停止标志 **/
    private static final int FLAG_STOP = 1002;

    private int scrollDuration = 1500;
    private int animationDuration = 300;

    /** 当前item下标 **/
    private int currentItem = -1;

    private Handler mHandler;
    private List<String> news;

    private OnItemClickListener itemClickListener;
    private Context mContext;

    public ScrollTextView(Context context) {
        this(context, null);
    }

    public ScrollTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    private void init() {
        setFocusable(true);
        if (news == null) {
            news = new ArrayList<String>();
            String s = "暂时没有通知公告";
            news.add(s);
        }

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case FLAG_START:
                        if (news.size() > 0) {
                            currentItem++;
                            setText(news.get(currentItem % news.size()));
                        }
                        mHandler.sendEmptyMessageAtTime(FLAG_START, scrollDuration);
                        break;

                    case FLAG_STOP:
                        mHandler.removeMessages(FLAG_START);
                        break;
                }
            }
        };

        setFactory(this);
        Animation in = new TranslateAnimation(0, 0, 300, 0);
        in.setDuration(animationDuration);
        in.setInterpolator(new AccelerateInterpolator());
        Animation out = new TranslateAnimation(0, 0, 0, -300);
        out.setDuration(animationDuration);
        out.setInterpolator(new AccelerateInterpolator());
        setInAnimation(in);
        setOutAnimation(out);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }

    public List<String> getNews() {
        return news;
    }

    public void setNews(List<String> news) {
        this.news = news;
    }


    @Override
    public View makeView() {
        return null;
    }

    interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
