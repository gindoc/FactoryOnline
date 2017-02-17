package com.online.factory.factoryonline.utils;

import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.widget.ImageView;

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.data.remote.Consts;
import com.online.factory.factoryonline.models.User;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import org.apache.commons.codec.binary.Base64;

import java.io.File;

/**
 * Created by louiszgm on 2016/10/7.
 */

public class DataBindingProperty {

    @BindingAdapter({"user"})
    public static void setUserPhoto(ImageView imageView, User user) {
        if (user == null) {
            Picasso.with(imageView.getContext()).load(R.drawable.avatar_user).into(imageView);
            return;
        }
        int avatar = user.getType() == Consts.TYPE_USER ? R.drawable.avatar_user : R.drawable.avtar_agent;
        if (!TextUtils.isEmpty(user.getAvatar())) {
            String decodedUrl = new String(Base64.decodeBase64(user.getAvatar().getBytes()));
            RequestCreator requestCreator = Picasso.with(imageView.getContext())
                    .load(decodedUrl)
                    .error(avatar)
                    .placeholder(avatar);
            if (imageView.getMeasuredWidth() != 0) {
                requestCreator.resize(imageView.getMeasuredWidth(), imageView.getMeasuredHeight());
            }
            requestCreator.into(imageView);
        } else {
            Picasso.with(imageView.getContext()).load(avatar).into(imageView);
        }
    }
    @BindingAdapter({"imageUrl"})
    public static void setImageScr(ImageView imageView, String url) {
        int id = imageView.getId();
        switch (id) {
            case R.id.iv_publish_people:
                if (!TextUtils.isEmpty(url)) {
                    loadImageFromUrl(imageView, url, R.drawable.publisher);
                }else {
                    Picasso.with(imageView.getContext()).load(R.drawable.publisher).into(imageView);
                }
                break;
            default:
                if (!TextUtils.isEmpty(url)) {
                    loadImageFromUrl(imageView, url, R.drawable.ic_no_pic);
                } else {
                    Picasso.with(imageView.getContext()).load(R.drawable.ic_no_pic).into(imageView);
                }
                break;
        }
    }

    private static void loadImageFromUrl(ImageView imageView, String url, int defaultDrawable) {
        String decodedUrl = new String(Base64.decodeBase64(url.getBytes()));
        RequestCreator requestCreator = Picasso.with(imageView.getContext())
                .load(decodedUrl)
                .error(defaultDrawable)
                .placeholder(defaultDrawable);
        if (imageView.getMeasuredWidth() != 0) {
            requestCreator.resize(imageView.getMeasuredWidth(), imageView.getMeasuredHeight());
        }
        requestCreator.into(imageView);
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
    public static void setImageId(final ImageView imageView, final int imageId) {
//        imageView.setImageResource(imageId);
        imageView.post(new Runnable() {
            @Override
            public void run() {
                int imageWidth = imageView.getMeasuredWidth();
                Picasso.with(imageView.getContext())
                        .load(imageId)
                        .resize(imageWidth,imageWidth)
                        .placeholder(R.drawable.ic_no_pic)
                        .config(Bitmap.Config.RGB_565)
                        .into(imageView);
            }
        });

    }

    @BindingAdapter({"isRefresh"})
    public static void isRefresh(SwipeRefreshLayout swipeRefreshLayout, boolean isLoad) {
        swipeRefreshLayout.setRefreshing(isLoad);
    }
}
