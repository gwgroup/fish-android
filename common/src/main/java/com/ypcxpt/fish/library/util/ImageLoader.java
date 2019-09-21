package com.ypcxpt.fish.library.util;

import android.support.annotation.DrawableRes;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import com.ypcxpt.fish.library.BaseApp;

/**
 * 加载图片.
 */
public class ImageLoader {

    public static void loadImage(String url, ImageView imageView) {
        Glide.with(BaseApp.getApp())
                .load(url)
                .into(imageView);
    }

    public static void loadImage(String url, ImageView imageView, @DrawableRes int error) {
        Glide.with(BaseApp.getApp())
                .load(url)
                .apply(createOptions(0, error))
                .into(imageView);
    }

    public static void loadImage(String url, ImageView imageView, @DrawableRes int placeHolder, @DrawableRes int error) {
        Glide.with(BaseApp.getApp())
                .load(url)
                .apply(createOptions(placeHolder, error))
                .into(imageView);
    }

    private static RequestOptions createOptions(int placeHolder, int error) {
        RequestOptions requestOptions = new RequestOptions()
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH);

        if (placeHolder > 0) {
            requestOptions.placeholder(placeHolder);
        }

        if (error > 0) {
            requestOptions.error(error);
        }

        return requestOptions;
    }

}
