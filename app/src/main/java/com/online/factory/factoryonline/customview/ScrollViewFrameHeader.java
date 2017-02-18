package com.online.factory.factoryonline.customview;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.online.factory.factoryonline.R;

public class ScrollViewFrameHeader extends RelativeLayout {

    public final static int STATE_NORMAL = 0;
    public final static int STATE_READY = 1;
    public final static int STATE_REFRESHING = 2;
    private int topMargin = 0;
    private int state = STATE_NORMAL;
    private ImageView refreshImage = null;
    private AnimationDrawable animationDrawable = null;

    public ScrollViewFrameHeader(Context context) {
        super(context);
        if(!isInEditMode())
            initView(context);
    }

    public ScrollViewFrameHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        if(!isInEditMode())
            initView(context);
    }

    public ScrollViewFrameHeader(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);  
        if(!isInEditMode())
            initView(context);  
    }  
  
    /** 
     * 初始化相关的view 
     */  
    public void initView(Context context) {  
        setPadding(10, 25, 10, 25);
        View view = LayoutInflater.from(context).inflate(R.layout.scrollview_frame_header, this, true);
        refreshImage = (ImageView) view.findViewById(R.id.image);
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        refreshImage.setImageResource(R.drawable.loading_frame);
        animationDrawable = (AnimationDrawable) refreshImage.getDrawable();
    }

    /**
     * 设置scrollviewHeader的状态 
     * @param state 
     */  
    public void setState(int state) {  
        if(this.state == state) {  
            return ;  
        }  
        switch (state) {  
        case STATE_NORMAL:
            if (animationDrawable != null) {
                animationDrawable.stop();
            }
            break;
        case STATE_READY:
            if (animationDrawable != null) {
                animationDrawable.stop();
            }
            break;  
        case STATE_REFRESHING:
            if (animationDrawable != null) {
                animationDrawable.start();
            }
            break;  
        default:  
            break;  
        }  
        this.state = state;  
    }  
      
    /** 
     * 更新header的margin 
     * @param margin 
     */  
    public void updateMargin(int margin) {  
        //这里用Linearlayout的原因是Headerview的父控件是scrollcontainer是一个linearlayout   
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) this.getLayoutParams();
        params.topMargin = margin;  
        topMargin = margin;  
        setLayoutParams(params);  
    }  
      
    /** 
     * 获取header的margin 
     * @return 
     */  
    public int getTopMargin() {  
        return topMargin;  
    }  
} 