package com.online.factory.factoryonline.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.utils.DensityUtil;

import java.util.List;

/**
 * Modify By cwenhui on 2016.11.03
 */
public class DropDownMenu extends LinearLayout {

    //顶部菜单布局
    private LinearLayout tabMenuView;
    //底部容器，包含popupMenuViews，maskView
    private FrameLayout containerView;
    //弹出菜单父布局
    private FrameLayout popupMenuViews;
    //遮罩半透明View，点击可关闭DropDownMenu
    private View maskView;
    //tabMenuView里面选中的tab位置，-1表示未选中
    private int current_tab_position = -1;

    //分割线颜色
    private int dividerColor = 0xffcccccc;
    //tab选中颜色
    private int textSelectedColor = 0xff890c85;
    //tab未选中颜色
    private int textUnselectedColor = 0xff111111;
    //遮罩颜色
    private int maskColor = 0x88888888;
    //tab字体大小
    private int menuTextSize = 14;

    //tab选中图标
    private int menuSelectedIcon;
    //tab未选中图标
    private int menuUnselectedIcon;


    public DropDownMenu(Context context) {
        this(context, null);
    }

    public DropDownMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DropDownMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setOrientation(VERTICAL);

        //为DropDownMenu添加自定义属性
        int menuBackgroundColor = 0xffffffff;
        int underlineColor = 0xffcccccc;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DropDownMenu);
        underlineColor = a.getColor(R.styleable.DropDownMenu_underlineColor, underlineColor);
        dividerColor = a.getColor(R.styleable.DropDownMenu_dividerColor, dividerColor);
        textSelectedColor = a.getColor(R.styleable.DropDownMenu_textSelectedColor, textSelectedColor);
        textUnselectedColor = a.getColor(R.styleable.DropDownMenu_textUnselectedColor, textUnselectedColor);
        menuBackgroundColor = a.getColor(R.styleable.DropDownMenu_menuBackgroundColor, menuBackgroundColor);
        maskColor = a.getColor(R.styleable.DropDownMenu_maskColor, maskColor);
        menuTextSize = a.getDimensionPixelSize(R.styleable.DropDownMenu_menuTextSize, menuTextSize);
        menuSelectedIcon = a.getResourceId(R.styleable.DropDownMenu_menuSelectedIcon, menuSelectedIcon);
        menuUnselectedIcon = a.getResourceId(R.styleable.DropDownMenu_menuUnselectedIcon, menuUnselectedIcon);
        a.recycle();

        //初始化tabMenuView并添加到tabMenuView
        tabMenuView = new LinearLayout(context);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tabMenuView.setOrientation(HORIZONTAL);
        tabMenuView.setBackgroundColor(menuBackgroundColor);
        tabMenuView.setLayoutParams(params);
        addView(tabMenuView, 0);

        //为tabMenuView添加下划线
        View underLine = new View(getContext());
        underLine.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));    //1px
        underLine.setBackgroundColor(underlineColor);
        addView(underLine, 1);

        //初始化containerView并将其添加到DropDownMenu
        containerView = new FrameLayout(context);
        containerView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        addView(containerView, 2);

        containerView.setBackgroundColor(Color.TRANSPARENT);
        setBackgroundColor(Color.TRANSPARENT);
    }

    /**
     * 初始化DropDownMenu
     *
     * @param tabTexts
     * @param popupViews //     * @param contentView
     */
    public void setDropDownMenu(@NonNull List<String> tabTexts, @NonNull List<View> popupViews) {
        if (tabTexts.size() != popupViews.size()) {
            throw new IllegalArgumentException("params not match, tabTexts.size() should be equal popupViews.size()");
        }

        for (int i = 0; i < tabTexts.size(); i++) {
            addTab(tabTexts, i);
        }

        maskView = new View(getContext());
        maskView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        maskView.setBackgroundColor(maskColor);
        maskView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                closeMenu();
            }
        });
        containerView.addView(maskView, 0);
        maskView.setVisibility(GONE);

        popupMenuViews = new FrameLayout(getContext());
        popupMenuViews.setVisibility(GONE);
        containerView.addView(popupMenuViews, 1);

        for (int i = 0; i < popupViews.size(); i++) {
            popupViews.get(i).setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            popupMenuViews.addView(popupViews.get(i), i);
        }

    }

    private void addTab(@NonNull List<String> tabTexts, int i) {
        Context context = getContext();
        final LinearLayout tabContainer = new LinearLayout(context);
        tabContainer.setGravity(Gravity.CENTER);
        tabContainer.setLayoutParams(new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.f));

        final TextView tabTxt = new TextView(context);
        tabTxt.setSingleLine();
        tabTxt.setEllipsize(TextUtils.TruncateAt.END);
        tabTxt.setTextSize(TypedValue.COMPLEX_UNIT_PX, menuTextSize);
        tabTxt.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tabTxt.setTextColor(textUnselectedColor);
        tabTxt.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(context, menuUnselectedIcon), null);
        tabTxt.setText(tabTexts.get(i));
        tabTxt.setPadding(DensityUtil.dip2px(context, 5), DensityUtil.dip2px(context, 12), DensityUtil.dip2px(context, 5), DensityUtil.dip2px(context, 12));

        tabContainer.addView(tabTxt);

        //添加点击事件
        tabContainer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                switchMenu(tabContainer);
            }
        });
        tabMenuView.addView(tabContainer);
        //添加分割线
        if (i < tabTexts.size() - 1) {
            View view = new View(context);
            LayoutParams layoutParams = new LayoutParams(DensityUtil.dip2px(context, 0.5f), ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.setMargins(0, DensityUtil.dip2px(context, 15), 0, DensityUtil.dip2px(context, 15));
            view.setLayoutParams(layoutParams);

            view.setBackgroundColor(dividerColor);
            tabMenuView.addView(view);
        }
    }

    /**
     * 改变tab文字
     *
     * @param text
     */
    public void setTabText(String text) {
        if (current_tab_position != -1) {
            ViewGroup tabContainer = (ViewGroup) tabMenuView.getChildAt(current_tab_position);
            TextView tabTxt = (TextView) tabContainer.getChildAt(0);
            tabTxt.setText(text);
        }
    }

    public void setTabClickable(boolean clickable) {
        for (int i = 0; i < tabMenuView.getChildCount(); i = i + 2) {
            tabMenuView.getChildAt(i).setClickable(clickable);
        }
    }

    /**
     * 关闭菜单
     */
    public void closeMenu() {
        if (current_tab_position != -1) {
            ViewGroup tabContainer = (ViewGroup) tabMenuView.getChildAt(current_tab_position);
            TextView tabTxt = (TextView) tabContainer.getChildAt(0);
            tabTxt.setTextColor(textUnselectedColor);
            tabTxt.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(getContext(), menuUnselectedIcon), null);
            popupMenuViews.setVisibility(View.GONE);
            popupMenuViews.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.drop_down_menu_out));
            maskView.setVisibility(GONE);
            maskView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dark_fade_out));
            current_tab_position = -1;
        }

    }

    /**
     * DropDownMenu是否处于可见状态
     *
     * @return
     */
    public boolean isShowing() {
        return current_tab_position != -1;
    }

    /**
     * 切换菜单
     *
     * @param target
     */
    private void switchMenu(View target) {
        for (int i = 0; i < tabMenuView.getChildCount(); i = i + 2) {
            if (target == tabMenuView.getChildAt(i)) {
                if (current_tab_position == i) {
                    closeMenu();
                } else {
                    if (current_tab_position == -1) {
                        popupMenuViews.setVisibility(View.VISIBLE);
                        popupMenuViews.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.drop_down_menu_in));
                        maskView.setVisibility(VISIBLE);
                        maskView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dark_fade_in));
                        popupMenuViews.getChildAt(i / 2).setVisibility(View.VISIBLE);
                    } else {
                        popupMenuViews.getChildAt(i / 2).setVisibility(View.VISIBLE);
                    }
                    current_tab_position = i;
                    ViewGroup tabContainer = (ViewGroup) tabMenuView.getChildAt(i);
                    TextView tabTxt = (TextView) tabContainer.getChildAt(0);
                    tabTxt.setTextColor(textSelectedColor);
                    tabTxt.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(getContext(), menuSelectedIcon), null);
                }
            } else {
                ViewGroup tabContainer = (ViewGroup) tabMenuView.getChildAt(i);
                TextView tabTxt = (TextView) tabContainer.getChildAt(0);
                tabTxt.setTextColor(textUnselectedColor);
                tabTxt.setCompoundDrawablesWithIntrinsicBounds(null, null,
                        ContextCompat.getDrawable(getContext(), menuUnselectedIcon), null);
                popupMenuViews.getChildAt(i / 2).setVisibility(View.GONE);
            }
        }
    }

}
