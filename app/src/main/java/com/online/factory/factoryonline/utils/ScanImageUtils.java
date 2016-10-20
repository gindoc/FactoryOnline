package com.online.factory.factoryonline.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.online.factory.factoryonline.models.ImageFloderBean;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import timber.log.Timber;

/**
 * Created by cwenhui on 2016/10/20.
 */

public class ScanImageUtils {
    /**
     * 临时的辅助类，用于防止同一个文件夹的多次扫描
     **/
    private static HashSet<String> mDirPaths = new HashSet<String>();
    /**
     * 扫描拿到所有的图片文件夹
     **/
    private static List<ImageFloderBean> mImageFloderBeens = new ArrayList<ImageFloderBean>();
    /**
     * 存储图片数量最多的文件夹中的图片数量
     **/
    private static int maxPicsSize;
    /**
     * 图片数量最多的文件夹
     **/
    private static File maxImgDir;
//    private static ContentResolver mContentResolver;
//    private static Cursor mCursor;


    /**
     * 获得最大图片数量的文件夹
     * 使用前请先调用scanImages
     *
     * @return
     */
    public static File getMaxImgDir() {
        return maxImgDir;
    }

    static int totalCount = 0;

    public static int getTotalCount() {
        return totalCount;
    }

    public static void scanImages(Context context) {
        mImageFloderBeens.clear();
        String firstImage = null;

        Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver mContentResolver = context.getContentResolver();

        // 只查询jpeg和png的图片
        Cursor mCursor = mContentResolver.query(mImageUri, null,
                MediaStore.Images.Media.MIME_TYPE + "=? or "
                        + MediaStore.Images.Media.MIME_TYPE + "=?",
                new String[]{"image/jpeg", "image/png"},
                MediaStore.Images.Media.DATE_MODIFIED);

        Timber.e("imageCount  :  " + mCursor.getCount());
        while (mCursor.moveToNext()) {
            // 获取图片的路径
            String path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA));

            Timber.e("path  :  " + path);
            // 拿到第一张图片的路径
            if (firstImage == null)
                firstImage = path;
            // 获取该图片的父路径名
            File parentFile = new File(path).getParentFile();
            if (parentFile == null)
                continue;
            String dirPath = parentFile.getAbsolutePath();
            ImageFloderBean imageFloderBean = null;
            // 利用一个HashSet防止多次扫描同一个文件夹（不加这个判断，图片多起来还是相当恐怖的~~）
            if (mDirPaths.contains(dirPath)) {
                continue;
            } else {
                mDirPaths.add(dirPath);
                // 初始化imageFloder
                imageFloderBean = new ImageFloderBean();
                imageFloderBean.setDir(dirPath);
                imageFloderBean.setFirstImagePath(path);
            }

            int picSize = parentFile.list(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String filename) {
                    if (filename.endsWith(".jpg") || filename.endsWith(".png") || filename.endsWith(".jpeg"))
                        return true;
                    return false;
                }
            }).length;
            totalCount += picSize;

            imageFloderBean.setCount(picSize);
            mImageFloderBeens.add(imageFloderBean);

            if (picSize > maxPicsSize) {
                maxPicsSize = picSize;
                maxImgDir = parentFile;
            }
        }
        mCursor.close();

        // 扫描完成，辅助的HashSet也就可以释放内存了
        mDirPaths.clear();
    }
}
