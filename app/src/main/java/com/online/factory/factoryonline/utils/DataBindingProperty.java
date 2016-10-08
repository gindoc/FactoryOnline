package com.online.factory.factoryonline.utils;

import android.databinding.BindingAdapter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by louiszgm on 2016/10/7.
 */

public class DataBindingProperty {

    @BindingAdapter({"imageUrl"})
    public static void setImageScr(ImageView imageView , String url){
        Picasso.with(imageView.getContext()).load(url).into(imageView);
    }
    @BindingAdapter({"isRefresh"})
    public static void isRefresh(SwipeRefreshLayout swipeRefreshLayout , boolean isLoad){
        swipeRefreshLayout.setRefreshing(isLoad);
    }
}
