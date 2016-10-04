package com.online.factory.factoryonline.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.models.News;

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
    /** 滚动时间间隔 **/
    private int scrollDuration = 3000;
    /** 动画时间 **/
    private int animationDuration = 300;

    /** 当前item下标 **/
    private int currentItem = -1;

    private float mTextSize = 14;

    private int mPadding = 5;

    private int mTextColor = Color.BLACK;

    private Handler mHandler;
    private List<News> news;

    private OnItemClickListener itemClickListener;
    private Context mContext;

    public ScrollTextView(Context context) {
        this(context, null);
    }

    public ScrollTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ScrollTextView);
        mTextSize = a.getDimension(R.styleable.ScrollTextView_textSize, 24);
        mPadding = (int) a.getDimension(R.styleable.ScrollTextView_padding, 20);
        scrollDuration = a.getInteger(R.styleable.ScrollTextView_scrollDuration, 3000);
        animationDuration = a.getInteger(R.styleable.ScrollTextView_animDuration, 300);
        mTextColor = a.getColor(R.styleable.ScrollTextView_textColor, Color.BLACK);
        a.recycle();
        init();
    }

    private void init() {
        setFocusable(true);
        if (news == null) {
            news = new ArrayList<News>();
            News n = new News("暂时没有通知公告");
            news.add(n);
        }

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case FLAG_START:
                        if (news.size() > 0) {
                            currentItem++;
                            News n = news.get(currentItem % news.size());
                            setText(n.getTitle());
                        }
                        mHandler.sendEmptyMessageDelayed(FLAG_START, scrollDuration);
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

    public List<News> getNews() {
        return news;
    }

    public void setNews(List<News> news) {
        this.news.clear();
        this.news.addAll(news);
        currentItem = -1;
    }

    public void startAutoScroll() {
        mHandler.sendEmptyMessage(FLAG_START);
    }

    public void stopAutoScroll() {
        mHandler.sendEmptyMessage(FLAG_STOP);
    }

    @Override
    public View makeView() {
        TextView t = new TextView(mContext);
        t.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        t.setMaxLines(1);
        t.setPadding(mPadding, mPadding, mPadding, mPadding);
        t.setTextColor(mTextColor);
        t.setTextSize(TypedValue.COMPLEX_UNIT_SP, mTextSize);

        t.setClickable(true);

        t.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null && news.size() > 0 && currentItem != -1) {
                    itemClickListener.onItemClick(currentItem % news.size());
                }
            }
        });
        return t;
    }
    interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
