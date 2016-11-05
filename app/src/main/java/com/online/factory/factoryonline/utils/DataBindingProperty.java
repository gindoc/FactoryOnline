package com.online.factory.factoryonline.utils;

import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.ImageView;

import com.online.factory.factoryonline.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * Created by louiszgm on 2016/10/7.
 */

public class DataBindingProperty {

    @BindingAdapter({"imageUrl"})
    public static void setImageScr(ImageView imageView, String url) {
        int id = imageView.getId();
        switch (id) {
            case R.id.iv_head_photo:
                Picasso.with(imageView.getContext()).load(url).placeholder(R.drawable.boy).into(imageView);
                break;
            default:
                Picasso.with(imageView.getContext()).load(url).into(imageView);
                break;
        }
    }

    @BindingAdapter({"fileUrl"})
    public static void setImageSrc(ImageView imageView, String url) {
        File file = new File(url);
        int imageWidth = imageView.getMeasuredWidth();
        Picasso.with(imageView.getContext())
                .load(file)
                .resize(imageWidth, imageWidth)
                .placeholder(R.drawable.ic_no_pic)
                .config(Bitmap.Config.RGB_565)
                .into(imageView);
    }

    @BindingAdapter({"imageId"})
    public static void setImageId(ImageView imageView, int imageId) {
        imageView.setImageResource(imageId);
    }

    @BindingAdapter({"isRefresh"})
    public static void isRefresh(SwipeRefreshLayout swipeRefreshLayout, boolean isLoad) {
        swipeRefreshLayout.setRefreshing(isLoad);
    }
}
