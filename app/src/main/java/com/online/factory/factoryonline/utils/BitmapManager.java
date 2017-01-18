package com.online.factory.factoryonline.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.UUID;

/**
 * 作者: GIndoc
 * 日期: 2016/11/14 11:55
 * 作用: 图片管理类
 */
public class BitmapManager {
	/**
	 * 计算样本容量，用于压缩图片；使用前需先将inJustDecodeBounds设置为true，使用再设置为false
	 * @param options		包含图片的宽和高
	 * @param reqWidth		期待的宽
	 * @param reqHeight		期待的高
	 * @return				返回样本容量
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		int height = options.outHeight;
		int width = options.outWidth;
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {
			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;
	}

	/**
	 * 得到压缩后图片的byte[]
	 * @param 				filePath 文件路径
	 * @return				返回压缩后图片的byte[]
	 */
	public static byte[] getSmallBitmapByte(String filePath) {
		Bitmap bit = getSmallBitmap(filePath);
		String name = UUID.randomUUID().toString();
		if (bit != null) { 						// 如果bit大小为0会报空指针
			return bitmapToByte(bit);
		} else
			return null;
	}

	/**
	 * 压缩图片
	 * @param filePath		文件路径
	 * @return				返回bitmap
	 */
	public static Bitmap getSmallBitmap(String filePath) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, 320, 480);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		Bitmap bit = BitmapFactory.decodeFile(filePath, options);
		return bit;
	}

	/**
	 * 压缩图片
	 * @param filePath			文件路径
	 * @param reqWidth			期望的宽
	 * @param reqHeight			期望的高
	 * @return					压缩后的Bitmap
	 */
	public static Bitmap compressImage(String filePath, int reqWidth, int reqHeight) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, opts);
		int width = opts.outWidth;
		int height = opts.outHeight;
		opts.inSampleSize = 1;
		while(width > reqWidth || height > reqHeight) {
			opts.inSampleSize *= 2;				//inSampleSize为偶数时才有效
			width /= 2;
			height /= 2;
		}
		opts.inJustDecodeBounds = false;
		Bitmap image = BitmapFactory.decodeFile(filePath, opts);
		return image;
	}
	
	public static Bitmap compressImage(InputStream is, int reqWidth, int reqHeight){
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(is, null, opts);
		int width = opts.outWidth;
		int height = opts.outHeight;
		opts.inSampleSize = 1;
		while(width > reqWidth || height > reqHeight) {
			opts.inSampleSize *= 2;				//inSampleSize为偶数时才有效
			width /= 2;
			height /= 2;
		}
		opts.inJustDecodeBounds = false;
		Bitmap image = BitmapFactory.decodeStream(is, null, opts);
		return image;
	}

	/**
	 * 将Bitmap转化为字节流
	 * @param bmp		待转化Bitmap
	 * @return			Bitmap转化后对应的字节流
	 */
	public static byte[] bitmapToByte(Bitmap bmp) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();		// 初始化一个流对象
		bmp.compress(Bitmap.CompressFormat.JPEG, 100, output);					// 把bitmap100%高质量压缩 到output对象里
		bmp.recycle();													// 自由选择是否进行回收
		byte[] result = output.toByteArray();							// 转换成功了
		try {
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static  Bitmap drawable2Bitmap(Drawable drawable) {
		if (drawable instanceof BitmapDrawable) {
			BitmapDrawable bd = (BitmapDrawable) drawable;
			return bd.getBitmap();
		}
		int w = drawable.getIntrinsicWidth();
		int h = drawable.getIntrinsicHeight();
		Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, w, h);
		drawable.draw(canvas);
		return bitmap;
	}

	public static Bitmap screenShot(Activity activity) {
		int[] widthAndHeight = WindowUtil.getScreenWidthAndHeight(activity);
		View view = activity.getWindow().getDecorView();
		view.setDrawingCacheEnabled(true);      //启用绘图缓存
		//调用下面这个方法非常重要，如果没有调用这个方法，得到的bitmap为null
		view.measure(View.MeasureSpec.makeMeasureSpec(widthAndHeight[0], View.MeasureSpec.EXACTLY),
				View.MeasureSpec.makeMeasureSpec(widthAndHeight[1], View.MeasureSpec.EXACTLY));
		//这个方法也非常重要，设置布局的尺寸和位置
		view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
		view.buildDrawingCache();               //创建位图
		Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache()); //创建一个DrawingCache的拷贝，因为DrawingCache得到的位图在禁用后会被回收
		view.setDrawingCacheEnabled(false);
		return bitmap;
	}
}