package com.online.factory.factoryonline.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.online.factory.factoryonline.models.exception.ValidateException;

/**
 * 作者: GIndoc
 * 日期: 2016/12/14 19:15
 * 作用:
 */

public class CommunicationUtil {
    public static void call(Context context, String phoneNum) {
        try {
            Validate.validatePhoneNum(phoneNum);
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + phoneNum));
            context.startActivity(intent);
        } catch (ValidateException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public static void sendSms(Context context, String phoneNum) {
        try {
            Validate.validatePhoneNum(phoneNum);
            Uri uri = Uri.parse("smsto:" + phoneNum);
            Intent sendIntent = new Intent(Intent.ACTION_SENDTO, uri);
            context.startActivity(sendIntent);
        } catch (ValidateException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
