package com.online.factory.factoryonline.utils;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.annotation.NonNull;

import com.online.factory.factoryonline.data.local.SharePreferenceKey;
import com.online.factory.factoryonline.models.post.Publish;

import org.apache.commons.codec.binary.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

public class Saver {
    private static SharedPreferences sharePref;

    public static SharedPreferences getIntance() {
        return sharePref;
    }

    public static void initSaver(Context context) {
        sharePref = context.getSharedPreferences("saveinfo", Context.MODE_PRIVATE);
    }

    public static boolean getLoginState() {
        return sharePref.getBoolean("isLogin", false);
    }

    public static void setLoginState(boolean loginState) {
        Editor edit = sharePref.edit();
        edit.putBoolean("isLogin", loginState);
        edit.commit();
    }

    public static void saveLogin(SharedPreferences sharePref, String user, String password) {
        Editor edit = sharePref.edit();
        edit.putString("user", user);
        edit.putString("password", password);
        edit.commit();
    }

    public static String getLoginUser() {
        return sharePref.getString("user", "");
    }

    public static String getLoginPassword() {
        return sharePref.getString("password", "");
    }

    public static <T extends Object>void saveSerializableObject(T object , String key) {
        Editor edit = sharePref.edit();
        edit.putString(key, EnCodeUtil.objectEncode(object));
        edit.commit();
    }
    public static <T extends Object>T getSerializableObject(String key) {
        String base64Publish = sharePref.getString(key, "");
        return EnCodeUtil.objectDecode(base64Publish);
    }

    public static void setToken(String token) {
        Editor editor = sharePref.edit();
        editor.putString("token", token);
        editor.commit();
    }

    public static String getToken() {
        return sharePref.getString("token", "");
    }

}
