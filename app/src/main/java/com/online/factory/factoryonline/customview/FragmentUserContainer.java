package com.online.factory.factoryonline.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.online.factory.factoryonline.R;

/**
 * 作者: GIndoc
 * 日期: 2017/1/16 11:24
 * 作用:
 */

public class FragmentUserContainer extends RelativeLayout {
    private Path path1 = new Path(), path2 = new Path(), path3 = new Path();
    private Paint paint, paint2;
    private ImageView imageView;
    private int point2Y;
    private int point3Y;
    private int halfScreenWidth;
    private int point4Y;
    private int point5Y;
    private int point8Y;
    private float hexPoint1Y, hexPoint2Y, hexPoint3Y, hexPoint4Y, hexPoint5Y, hexPoint6Y;
    private float hexPoint1X, hexPoint2X, hexPoint3X, hexPoint4X, hexPoint5X, hexPoint6X;

    public FragmentUserContainer(Context context) {
        this(context, null);
    }

    public FragmentUserContainer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FragmentUserContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);

        paint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint2.setColor(Color.GREEN);
        paint2.setStyle(Paint.Style.FILL);

        renderPublishHistory(context);
    }

    /**
     * 给发布历史设置监听和图像，并添加到容器中
     * @param context
     */
    private void renderPublishHistory(Context context) {
        imageView = new ImageView(context);
        imageView.setImageResource(R.drawable.ic_publish_pen);
        int padding = getResources().getDimensionPixelOffset(R.dimen.x13);
        imageView.setPadding(padding, padding, padding, padding);
        addView(imageView);
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onPublishHistoryClickListener != null) {
                    onPublishHistoryClickListener.onClick(v);
                }
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.parseColor("#f5f5f5"));

        int height = getHeight();
        int width = getWidth();
        halfScreenWidth = width / 2;
        point2Y = height * 65 / 284;
        point3Y = height * 110 / 284;
        point4Y = height * 65 / 284;
        point5Y = height * 146 / 568;       //548

        point8Y = height * 330 / 568;

        hexPoint1Y = point3Y;
        hexPoint2X = halfScreenWidth + 25 / 320f * width;
        hexPoint2Y = height * 110 / 284f - 14 / 568f * height;
        hexPoint3X = hexPoint2X;
        hexPoint3Y = height * 110 / 284f - 40 / 568f * height;
        hexPoint4Y = height * 110 / 284f - 55 / 568f * height;
        hexPoint5X = halfScreenWidth - 25 / 320f * width;
        hexPoint5Y = height * 110 / 284f - 40 / 568f * height;
        hexPoint6X = halfScreenWidth - 25 / 320f * width;
        hexPoint6Y = height * 110 / 284f - 14 / 568f * height;

        path1.moveTo(0, 0);
        path1.lineTo(0, point2Y);
        path1.lineTo(halfScreenWidth, point3Y);
        path1.lineTo(width, point4Y);
        path1.lineTo(width, 0);
        path1.close();

        path2.moveTo(0, point5Y);
        path2.lineTo(0, height);
        path2.lineTo(width, height);
        path2.lineTo(width, point8Y);
        path2.close();

        path3.moveTo(halfScreenWidth,  hexPoint1Y);
        path3.lineTo(hexPoint2X, hexPoint2Y);
        path3.lineTo(hexPoint3X, hexPoint3Y);
        path3.lineTo(halfScreenWidth, hexPoint4Y);
        path3.lineTo(hexPoint5X, hexPoint5Y);
        path3.lineTo(hexPoint6X, hexPoint6Y);
        path3.close();

        canvas.drawPath(path1, paint);
        canvas.drawPath(path2, paint);
        canvas.drawPath(path3, paint2);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        imageView.layout(getWidth() / 2 - imageView.getWidth() / 2, (int) (getHeight() * 110 / 284f - 27.5 / 568f * getHeight() - imageView.getHeight() / 2),
                getWidth() / 2 + imageView.getWidth() / 2, (int) (getHeight() * 110 / 284f - 27.5 / 568f * getHeight() + imageView.getHeight() / 2));
    }

    interface onPublishHistoryClickListener{
        void onClick(View view);
    }

    private onPublishHistoryClickListener onPublishHistoryClickListener;

    public void setOnPublishHistoryClickListener(FragmentUserContainer.onPublishHistoryClickListener onPublishHistoryClickListener) {
        this.onPublishHistoryClickListener = onPublishHistoryClickListener;
    }
}
