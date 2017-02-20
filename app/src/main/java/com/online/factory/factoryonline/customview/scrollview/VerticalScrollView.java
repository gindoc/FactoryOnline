package com.online.factory.factoryonline.customview.scrollview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.models.News;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cwenhui on 2016/11/2.
 */
public class VerticalScrollView extends ViewSwitcher implements ViewSwitcher.ViewFactory {
    /** 滚动开始标志 **/
    private static final int FLAG_START = 1001;
    /** 滚动停止标志 **/
    private static final int FLAG_STOP = 1002;
    /** 滚动时间间隔 **/
    private int scrollDuration = 3000;
    /** 动画时间 **/
    private int animationDuration = 300;

    /** 第一条消息的下标 **/
    private int firstMsgIndex = -2;
    /** 第二条消息的下标 **/
    private int secondMsgIndex = -1;

    private float mTextSize = 14;
    private int mPadding = 5;
    private int mTextColor = Color.BLACK;

    private Handler mHandler;
    private List<News> news;

    private OnItemClickListener itemClickListener;
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    public VerticalScrollView(Context context) {
        this(context, null);
    }

    public VerticalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
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
                            firstMsgIndex += 2;
                            secondMsgIndex += 2;

                            View view = getNextView();
                            TextView firstTag = (TextView) view.findViewById(R.id.tv_tag_first);
                            TextView firstMsg = (TextView) view.findViewById(R.id.tv_msg_first);
                            TextView secondTag = (TextView) view.findViewById(R.id.tv_tag_second);
                            TextView secondMsg = (TextView) view.findViewById(R.id.tv_msg_second);

                            News n1 = news.get(firstMsgIndex % news.size());
                            News n2 = news.get(secondMsgIndex % news.size());

                            String[] news1 = n1.getTitle().split(",");
                            if (news1.length > 1) {
                                firstTag.setText(news1[0]);
                                firstMsg.setText(news1[1]);
                            }else {
                                firstMsg.setText(n1.getTitle());
                            }
                            String[] news2 = n2.getTitle().split(",");
                            if (news2.length > 1) {
                                secondTag.setText(news2[0]);
                                secondMsg.setText(news2[1]);
                            }else {
                                secondMsg.setText(n2.getTitle());
                            }

                            view.setClickable(true);

                            view.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (itemClickListener != null && news.size() > 0 && firstMsgIndex >=0 && secondMsgIndex>=0) {
                                        Toast.makeText(mContext, "该功能暂未开放，敬请期待", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                            showNext();
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
        firstMsgIndex = -2;
        secondMsgIndex = -1;
    }
    public void startAutoScroll() {
        mHandler.sendEmptyMessage(FLAG_START);
    }

    public void stopAutoScroll() {
        mHandler.sendEmptyMessage(FLAG_STOP);
    }

    @Override
    public View makeView() {
        View view = mLayoutInflater.inflate(R.layout.layout_vertical_scroll_view, null);

        return view;
    }

    interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
