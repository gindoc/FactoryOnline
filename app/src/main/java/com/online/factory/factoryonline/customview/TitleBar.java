package com.online.factory.factoryonline.customview;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.online.factory.factoryonline.R;

/**
 * 作者: GIndoc
 * 日期: 2017/2/14 15:53
 * 作用:
 */

public class TitleBar extends FrameLayout {
    private Resources resources;
    private int defaultColor;
    private TextView tvTitle;
    private TextView tvLeft;
    private TextView tvRight;

    public TitleBar(Context context) {
        this(context, null);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        CharSequence titleText, leftText, rightText;
        int titleTextColor, leftTextColor, rightTextColor;
        float titleTextSize, leftTextSize, rightTextSize;
        Drawable leftTextDrawable, rightTextDrawable;
        int leftDrawablePadding, rightDrawablePadding;
        boolean titleVisible, leftVisible, rightVisible;

        resources = getResources();
        defaultColor = ResourcesCompat.getColor(resources, R.color.colorPrimary, null);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TitleBar);

        titleText = typedArray.getText(R.styleable.TitleBar_title_name);
        titleTextColor = typedArray.getColor(R.styleable.TitleBar_title_text_color, defaultColor);
        titleTextSize = typedArray.getDimension(R.styleable.TitleBar_title_text_size,0);
        titleVisible = typedArray.getBoolean(R.styleable.TitleBar_title_visible, false);

        leftText = typedArray.getString(R.styleable.TitleBar_left_text);
        leftTextColor = typedArray.getColor(R.styleable.TitleBar_left_text_color, defaultColor );
        leftTextSize = typedArray.getDimension(R.styleable.TitleBar_left_text_size, 0);
        leftDrawablePadding = typedArray.getDimensionPixelOffset(R.styleable.TitleBar_left_drawable_padding, 0);
        leftTextDrawable = typedArray.getDrawable(R.styleable.TitleBar_left_text_drawable);
        leftVisible = typedArray.getBoolean(R.styleable.TitleBar_left_text_visible, false);

        rightText = typedArray.getText(R.styleable.TitleBar_right_text);
        rightTextColor = typedArray.getColor(R.styleable.TitleBar_right_text_color, defaultColor);
        rightTextSize = typedArray.getDimension(R.styleable.TitleBar_right_text_size, 0);
        rightDrawablePadding = typedArray.getDimensionPixelOffset(R.styleable.TitleBar_right_drawable_padding, 0);
        rightTextDrawable = typedArray.getDrawable(R.styleable.TitleBar_right_text_drawable);
        rightVisible = typedArray.getBoolean(R.styleable.TitleBar_right_text_visible, false);

        typedArray.recycle();

        LayoutInflater.from(context).inflate(R.layout.layout_titlebar, this);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvLeft = (TextView) findViewById(R.id.tv_left);
        tvRight = (TextView) findViewById(R.id.tv_right);

        tvTitle.setText(titleText);
        tvTitle.setTextColor(titleTextColor);
        tvTitle.setTextSize(titleTextSize);
        tvTitle.setVisibility(titleVisible ? VISIBLE : GONE);

        tvLeft.setText(leftText);
        tvLeft.setTextColor(leftTextColor);
        tvLeft.setTextSize(leftTextSize);
        tvLeft.setCompoundDrawablePadding(leftDrawablePadding);
        tvLeft.setCompoundDrawablesWithIntrinsicBounds(leftTextDrawable, null, null, null);
        tvLeft.setVisibility(leftVisible ? VISIBLE : GONE);

        tvRight.setText(rightText);
        tvRight.setTextColor(rightTextColor);
        tvRight.setTextSize(rightTextSize);
        tvRight.setCompoundDrawablePadding(rightDrawablePadding);
        tvRight.setCompoundDrawablesWithIntrinsicBounds(null, null, rightTextDrawable, null);
        tvRight.setVisibility(rightVisible ? VISIBLE : GONE);

        tvLeft.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null) {
                    clickListener.onLeftButtonClickListener(v);
                }
            }
        });

        tvRight.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null) {
                    clickListener.onRightButtonClickListener(v);
                }
            }
        });
    }

    public interface OnTitleBarClickListener{
        void onLeftButtonClickListener(View view);
        void onRightButtonClickListener(View view);
    }

    private OnTitleBarClickListener clickListener;

    public void setOnTitleBarClickListener(OnTitleBarClickListener clickListener) {
        this.clickListener = clickListener;
    }

}
