package com.online.factory.factoryonline.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ImageView;

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.utils.BitmapManager;
import com.online.factory.factoryonline.utils.DensityUtil;

/**
 * 作者: GIndoc
 * 日期: 2017/1/12 9:23
 * 作用:
 */

public class RoundImage extends ImageView {
    private int type;
    public static final int TYPE_CIRCLE = 0;
    public static final int TYPE_ROUND = 1;
    private int mRadius;
    private int mWidth;
    private Paint mPaint;
    private int defaultColor = 0xFFFFFFFF;
    // 如果只有其中一个有值，则只画一个圆形边框
    private int mBorderOutsideColor = 0;
    private int mBorderInsideColor = 0;
    private int mBorderInsideThickness = 0;
    private int mBorderOutsideThickness = 0;
    /**
     * 3x3 矩阵，主要用于缩小放大
     */
    private Matrix mMatrix;

    public RoundImage(Context context) {
        this(context, null);
    }

    public RoundImage(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundImage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RoundImage);
        type = ta.getInt(R.styleable.RoundImage_type, TYPE_CIRCLE);     // 默认是圆形图片
        mRadius = ta.getDimensionPixelSize(R.styleable.RoundImage_borderRadius, DensityUtil.dip2px(context, 10));
        mBorderInsideThickness = ta.getDimensionPixelSize( R.styleable.RoundImage_border_inside_thickness, 0);
        mBorderOutsideThickness = ta.getDimensionPixelSize( R.styleable.RoundImage_border_outside_thickness, 0);
        mBorderOutsideColor = ta.getColor(R.styleable.RoundImage_border_outside_color1, defaultColor);
        mBorderInsideColor = ta.getColor(R.styleable.RoundImage_border_inside_color1, defaultColor);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mMatrix = new Matrix();

        ta.recycle();
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setmRadius(int mRadius) {
        this.mRadius = mRadius;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        /**
         * 如果类型是圆形，则强制改变view的宽高一致，以小值为准
         */
        if (type == TYPE_CIRCLE) {
            mWidth = Math.min(getMeasuredHeight(), getMeasuredWidth());
            mRadius = mWidth/2;
            setMeasuredDimension(mWidth, mWidth);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable drawable = getDrawable();
        if (drawable == null)
            return;
        if (getWidth() == 0 || getHeight() == 0)
            return;
        this.measure(0, 0);
        if (drawable.getClass() == NinePatchDrawable.class)
            return;
        if (type == TYPE_ROUND){
            drawRectBorder(canvas, mBorderOutsideColor, 0, 0, getWidth(), getHeight());     //画外层矩形
            drawRectBorder(canvas, mBorderInsideColor, mBorderOutsideThickness, mBorderOutsideThickness,
                    getWidth()-mBorderOutsideThickness, getHeight()-mBorderOutsideThickness);      //画内层矩形
            setUpShader();
            drawRectContent(canvas);
        }else {
            drawCircleBorder(canvas, mBorderOutsideColor, mRadius);
            drawCircleBorder(canvas, mBorderInsideColor, mRadius-mBorderOutsideThickness);
            setUpShader();
            drawCircleContent(canvas);
        }
    }

    /**
     * 画圆形图片的图像
     * @param canvas
     */
    private void drawCircleContent(Canvas canvas) {
        int radius = 0;
        if (mBorderOutsideColor != defaultColor && mBorderInsideColor != defaultColor) {
            radius = mRadius - mBorderOutsideThickness-mBorderInsideThickness;
        }else if (mBorderInsideColor == defaultColor && mBorderOutsideColor != defaultColor) {
            radius = mRadius - mBorderOutsideThickness;
        }else if (mBorderInsideColor != defaultColor){
            radius = mRadius - mBorderInsideThickness;
        }else {
            radius = mRadius;
        }
        canvas.drawCircle(mRadius, mRadius, radius, mPaint);
    }

    /**
     * 画矩形图片的图像
     * @param canvas
     */
    private void drawRectContent(Canvas canvas) {
        RectF rectF = new RectF();
        if (mBorderOutsideColor != defaultColor && mBorderInsideColor != defaultColor) {
            rectF.set(mBorderOutsideThickness+mBorderInsideThickness,
                    mBorderOutsideThickness+mBorderInsideThickness,
                    getWidth()-mBorderOutsideThickness-mBorderInsideThickness,
                    getHeight()-mBorderOutsideThickness-mBorderInsideThickness);
        }else if (mBorderInsideColor == defaultColor && mBorderOutsideColor != defaultColor) {
            rectF.set(mBorderOutsideThickness, mBorderOutsideThickness, getWidth() - mBorderOutsideThickness, getHeight() - mBorderOutsideThickness);
        }else if (mBorderInsideColor != defaultColor){
            rectF.set(mBorderInsideThickness, mBorderInsideThickness, getWidth() - mBorderInsideThickness, getHeight() - mBorderInsideThickness);
        }else {
            rectF.set(0, 0, getWidth(), getHeight());
        }
        canvas.drawRoundRect(rectF, mRadius, mRadius,mPaint);
    }

    /**
     * 画圆形边框
     * @param canvas
     * @param color     边框颜色
     * @param radius
     */
    private void drawCircleBorder(Canvas canvas, int color, int radius) {
        Paint paint = getBorderPaint(color);
        canvas.drawCircle(mRadius, mRadius, radius, paint);
    }

    /**
     * 画圆角矩形边框
     * @param canvas
     * @param color         边框颜色
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    private void drawRectBorder(Canvas canvas, int color, int left, int top, int right, int bottom) {
        Paint paint = getBorderPaint(color);
        RectF rectF = new RectF(left, top, right, bottom);
        canvas.drawRoundRect(rectF, mRadius, mRadius, paint);
    }

    /**
     * 获取边框画笔
     * @param color     边框颜色
     * @return
     */
    @NonNull
    private Paint getBorderPaint(int color) {
        Paint paint = new Paint();
        /* 去锯齿 */
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
        return paint;
    }

    /**
     * 为绘画图像的paint设置BitmapShader，用于放大或缩小图片
     */
    private void setUpShader() {
        Drawable drawable = getDrawable();
        if (drawable == null)
            return;

        Bitmap bmp = BitmapManager.drawable2Bitmap(drawable);
        // 将bmp作为着色器，就是在指定区域内绘制bmp
        BitmapShader mBitmapShader = new BitmapShader(bmp, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        float scale = 1.0f;
        if (type == TYPE_CIRCLE) {
            // 拿到bitmap宽或高的小值,这样scale才是较大的值（放大倍数较大才能填满view）
            int bitmapSize = Math.min(bmp.getWidth(), bmp.getHeight());
            scale = mWidth * 1.0f / bitmapSize;
        }else if (type == TYPE_ROUND) {
            // 如果图片的宽或者高与view的宽高不匹配，计算出需要缩放的比例；
            // 缩放后的图片的宽高，一定要大于我们view的宽高；所以我们这里取大值；
            //因为要填满整个view
//            scale = Math.max(getWidth() * 1.0f / bmp.getWidth(), getHeight() * 1.0f / bmp.getHeight());
            scale = Math.min(getWidth() * 1.0f / bmp.getWidth(), getHeight() * 1.0f / bmp.getHeight());
        }
        // shader的变换矩阵，我们这里主要用于放大或者缩小
        mMatrix.setScale(scale, scale);
        // 设置变换矩阵
        mBitmapShader.setLocalMatrix(mMatrix);
        // 设置shader
        mPaint.setShader(mBitmapShader);
    }

}
