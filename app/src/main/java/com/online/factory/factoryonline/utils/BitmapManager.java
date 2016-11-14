package com.online.factory.factoryonline.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

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
		options.inSampleSize = calculateInSampleSize(options, 480, 800);

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
}