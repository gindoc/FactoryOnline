package com.online.factory.factoryonline.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
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
    private ImageView publishHistory, redeem;
    private TextView collection, visitHistory, feedback, rolePick, setting;
    private Path pathDog = new Path();

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

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.parseColor("#f5f5f5"));

        int height = getHeight();
        int width = getWidth();
        int halfScreenWidth = width / 2;
        int point2Y = height * 65 / 284;
        int point3Y = height * 110 / 284;
        int point4Y = height * 65 / 284;
        int point5Y = height * 146 / 568;       //548

        int point8Y = height * 330 / 568;

        float hexPoint1Y = point3Y;
        float hexPoint2X = halfScreenWidth + 25 / 320f * width;
        float hexPoint2Y = height * 110 / 284f - 14 / 568f * height;
        float hexPoint3X = hexPoint2X;
        float hexPoint3Y = height * 110 / 284f - 40 / 568f * height;
        float hexPoint4Y = height * 110 / 284f - 55 / 568f * height;
        float hexPoint5X = halfScreenWidth - 25 / 320f * width;
        float hexPoint5Y = height * 110 / 284f - 40 / 568f * height;
        float hexPoint6X = halfScreenWidth - 25 / 320f * width;
        float hexPoint6Y = height * 110 / 284f - 14 / 568f * height;

        float dogPoint1X = width;
        float dogPoint1Y = height * 148 / 568f;
        float dogPoint2X = width * 239 / 320f;
        float dogPoint2Y = height * 195 / 568f;
        float dogPoint3X = dogPoint2X;
        float dogPoint3Y = height * 266 / 568f;
        float dogPoint4X = width;
        float dogPoint4Y = height * 314 / 568f;

        float dogLine1Point1X = width * 245 / 320f;
        float dogLine1Point1Y = height * 194 / 568f;
        float dogLine1Point2X = dogLine1Point1X;
        float dogLine1Point2Y = height * 260 / 568f;
        float dogLine1Point3X = width;
        float dogLine1Point3Y = height * 305 / 568f;
        float[] dogLine1 = {dogLine1Point1X, dogLine1Point1Y, dogLine1Point2X, dogLine1Point2Y,
                dogLine1Point2X, dogLine1Point2Y, dogLine1Point3X, dogLine1Point3Y};

        float dogLine2Point1X = width * 251 / 320f;
        float dogLine2Point1Y = height * 190 / 568f;
        float dogLine2Point2X = dogLine2Point1X;
        float dogLine2Point2Y = height * 256 / 568f;
        float dogLine2Point3X = width;
        float dogLine2Point3Y = height * 296 / 568f;
        float[] dogLine2 = {dogLine2Point1X, dogLine2Point1Y, dogLine2Point2X, dogLine2Point2Y,
                dogLine2Point2X, dogLine2Point2Y, dogLine2Point3X, dogLine2Point3Y};


        float greenLinePoint1X = width * 253 / 320f;
        float greenLinePoint1Y = height * 204 / 568f;
        float greenLinePoint2X = greenLinePoint1X;
        float greenLinePoint2Y = height * 219 / 568f;

        float blueLinePoint1X = width * 247 / 320f;
        float blueLinePoint1Y = height * 221 / 568f;
        float blueLinePoint2X = blueLinePoint1X;
        float blueLinePoint2Y = height * 236 / 568f;

        float redLinePoint1X = width * 240 / 320f;
        float redLinePoint1Y = height * 236 / 568f;
        float redLinePoint2X = redLinePoint1X;
        float redLinePoint2Y = height * 252 / 568f;

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

        pathDog.moveTo(dogPoint1X, dogPoint1Y);
        pathDog.lineTo(dogPoint2X, dogPoint2Y);
        pathDog.lineTo(dogPoint3X, dogPoint3Y);
        pathDog.lineTo(dogPoint4X, dogPoint4Y);
        pathDog.close();

        Paint dogLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        dogLinePaint.setColor(Color.parseColor("#ebebeb"));
        dogLinePaint.setStyle(Paint.Style.STROKE);
        dogLinePaint.setStrokeWidth(getResources().getDimensionPixelOffset(R.dimen.x2));

        canvas.drawPath(path1, paint);
        canvas.drawPath(path2, paint);
        canvas.drawPath(pathDog, paint);
        canvas.drawPath(path3, paint2);

        canvas.drawLines(dogLine1, dogLinePaint);
        canvas.drawLines(dogLine2, dogLinePaint);
        dogLinePaint.setColor(Color.GREEN);
        canvas.drawLine(greenLinePoint1X, greenLinePoint1Y, greenLinePoint2X, greenLinePoint2Y, dogLinePaint);
        dogLinePaint.setColor(Color.BLUE);
        canvas.drawLine(blueLinePoint1X, blueLinePoint1Y, blueLinePoint2X, blueLinePoint2Y, dogLinePaint);
        dogLinePaint.setColor(Color.RED);
        canvas.drawLine(redLinePoint1X, redLinePoint1Y, redLinePoint2X, redLinePoint2Y, dogLinePaint);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        float width = getWidth();
        float height = getHeight();
        int collectionY = (int) (height * 252 / 568f);
        int collectionX = (int) (width * 51 / 320f);
        int visitHistoryX = collectionX;
        int visitHistoryY = (int) (height * 347 / 568f);
        int feedbackX = (int) (width * 216 / 320f);
        int feedbackY = visitHistoryY;
        int rolePickY = (int) (height * 443 / 568f);
        int rolePickX = visitHistoryX;
        int settingX = feedbackX;
        int settingY = rolePickY;
        int publishHistoryL = (int) (width/2-publishHistory.getWidth()/2);
        int publishHistoryR = (int) (width/2+publishHistory.getWidth()/2);
        int publishHistoryT = (int) (height * 180 / 568f);
        int publishHistoryB = (int) (height * 206 / 568f);
        int dogL = (int) (width * 256 / 320f);
        int dogT = (int) (height * 148 / 568f);
        int dogR = (int) (width * 313 / 320f);
        int dogB = (int) (height * 265 / 568f);

        collection.layout(collectionX, collectionY, collectionX + collection.getMeasuredWidth(), collectionY + collection.getMeasuredHeight());
        visitHistory.layout(visitHistoryX, visitHistoryY, visitHistoryX + visitHistory.getWidth(), visitHistoryY + visitHistory.getHeight());
        feedback.layout(feedbackX, feedbackY, feedbackX + feedback.getWidth(), feedbackY + feedback.getHeight());
        rolePick.layout(rolePickX, rolePickY, rolePickX + rolePick.getWidth(), rolePickY + rolePick.getHeight());
        setting.layout(settingX, settingY, settingX + setting.getWidth(), settingY + setting.getHeight());
        publishHistory.layout(publishHistoryL, publishHistoryT, publishHistoryR, publishHistoryB);
        redeem.layout(dogL, dogT, dogR, dogB);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        collection = (TextView) findViewById(R.id.tv_collection);
        visitHistory = (TextView) findViewById(R.id.tv_visit_history);
        feedback = (TextView) findViewById(R.id.tv_feedback);
        rolePick = (TextView) findViewById(R.id.tv_role_pick);
        setting = (TextView) findViewById(R.id.tv_setting);
        publishHistory = (ImageView) findViewById(R.id.iv_publish_history);
        redeem = (ImageView) findViewById(R.id.iv_redeem);

        measureChild(collection, widthMeasureSpec, heightMeasureSpec);
        measureChild(visitHistory, widthMeasureSpec, heightMeasureSpec);
        measureChild(feedback, widthMeasureSpec, heightMeasureSpec);
        measureChild(rolePick, widthMeasureSpec, heightMeasureSpec);
        measureChild(setting, widthMeasureSpec, heightMeasureSpec);
        measureChild(publishHistory, widthMeasureSpec, heightMeasureSpec);
        measureChild(redeem, widthMeasureSpec, heightMeasureSpec);

    }

}
