package com.online.factory.factoryonline.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * 作者: GIndoc
 * 日期: 2017/1/17 18:18
 * 作用:
 */

public class WindowUtil {
    public static int getStatusHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static int[] getScreenWidthAndHeight(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);
        int[] widthAndHeight = {dm.widthPixels, dm.heightPixels};
        return widthAndHeight;
    }
}
