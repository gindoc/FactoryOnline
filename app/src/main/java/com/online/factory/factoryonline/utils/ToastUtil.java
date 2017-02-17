package com.online.factory.factoryonline.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {
    private static String oldMsg;
    protected static Toast toast   = null;
    private static long oneTime=0;
    private static long twoTime=0;
    public static void show(Context mContext, String msg) {
        if(toast==null){
            toast =Toast.makeText(mContext.getApplicationContext(), msg, Toast.LENGTH_SHORT);       //利用ApplicationContext，防止内存泄漏
            toast.show();
            oneTime=System.currentTimeMillis();
        }else{
            twoTime=System.currentTimeMillis();
            if(msg.equals(oldMsg)){
                if(twoTime-oneTime>Toast.LENGTH_SHORT){
                    toast.show();
                }
            }else{
                oldMsg = msg;
                toast.setText(msg);
                toast.show();
            }
        }
        oneTime=twoTime;
    }
}