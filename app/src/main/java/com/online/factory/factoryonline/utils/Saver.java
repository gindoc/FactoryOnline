package com.online.factory.factoryonline.utils;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

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
//	private CookieStore mCookies;

    public static SharedPreferences getIntance() {
        return sharePref;
    }

//	public CookieStore getCookie() {
//		return mCookies;
//	}

    public static void initSaver(Context context) {
        sharePref = context.getSharedPreferences("saveinfo", Context.MODE_PRIVATE);
    }

//	public static void saveCookies(String cookies) {
//		Editor edit = sharePref.edit();
//		edit.putString("cookies", cookies);
//		edit.commit();
//	}

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

    public static void savePublish(Publish publish) {
        Editor edit = sharePref.edit();
        // 创建字节输出流
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            // 创建对象输出流，并封装字节流
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            // 将对象写入字节流
            oos.writeObject(publish);
            // 将字节流编码成base64的字符窜
            String oAuth_Base64 = new String(Base64.encodeBase64(baos
                    .toByteArray()));
            edit.putString("publish", oAuth_Base64);

            edit.commit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Publish getPublish() {
        Publish publish = null;
        String base64Publish = sharePref.getString("publish", "");
        byte[] base64 = Base64.decodeBase64(base64Publish.getBytes());      //读取字节
        ByteArrayInputStream bais = new ByteArrayInputStream(base64);        //封装到字节流
        try {
            //再次封装
            ObjectInputStream bis = new ObjectInputStream(bais);
            try {
                //读取对象
                publish = (Publish) bis.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return publish;
    }

}
