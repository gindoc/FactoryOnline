package com.online.factory.factoryonline.utils;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

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

//	public static String save

}
