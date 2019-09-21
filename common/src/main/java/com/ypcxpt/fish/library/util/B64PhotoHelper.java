package com.ypcxpt.fish.library.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Base64;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.ByteArrayOutputStream;

public class B64PhotoHelper {

    public static final int DEFAULT_WIDTH = 120;

    public static final int IMAGE_QUALITY = 100;

    public static String photoToB64Str(Activity activity, String path) {
        try {
            Bitmap bitmap = Glide.with(activity)
                    .asBitmap()
                    .load(path)
                    .apply(new RequestOptions().centerCrop())
                    .submit(DEFAULT_WIDTH, DEFAULT_WIDTH)
                    .get();

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, IMAGE_QUALITY, bos);
            byte[] byteArray = bos.toByteArray();

            String b64Str = Base64.encodeToString(byteArray, Base64.NO_WRAP);
            return b64Str;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void load(Activity activity, String b64Str, ImageView iv) {
        if (b64Str.contains(",")) {
            b64Str = b64Str.split(",")[1];
        }
        byte[] imageBytes = Base64.decode(b64Str, Base64.DEFAULT);
        Glide.with(activity)
                .asBitmap()
                .load(imageBytes)
                .into(iv);

//        RequestOptions options = new RequestOptions()
//                .placeholder(R.mipmap.ic_default_avatar_male)                //加载成功之前占位图
//                .error(R.mipmap.ic_launcher)                    //加载错误之后的错误图
//                .dontAnimate();
//                .override(400, 400)                                //指定图片的尺寸
////指定图片的缩放类型为fitCenter （等比例缩放图片，宽或者是高等于ImageView的宽或者是高。）
//                .fitCenter()
////指定图片的缩放类型为centerCrop （等比例缩放图片，直到图片的狂高都大于等于ImageView的宽度，然后截取中间的显示。）
//                .centerCrop()
//                .circleCrop()//指定图片的缩放类型为centerCrop （圆形）
//                .skipMemoryCache(true)                            //跳过内存缓存
//                .diskCacheStrategy(DiskCacheStrategy.ALL)        //缓存所有版本的图像
//                .diskCacheStrategy(DiskCacheStrategy.NONE)        //跳过磁盘缓存
//                .diskCacheStrategy(DiskCacheStrategy.DATA)        //只缓存原来分辨率的图片
//                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);    //只缓存最终的图片
    }

}
