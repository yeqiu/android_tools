package com.yeqiu.hydra.utils.image;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.yeqiu.hydra.HydraUtilsManager;
import com.yeqiu.hydra.ui.UiConfig;
import com.yeqiu.hydra.utils.APPInfoUtil;
import com.yeqiu.hydra.utils.LogUtils;
import com.yeqiu.hydra.utils.thread.ThreadUtil;

import java.io.File;
import java.io.FileOutputStream;

/**
 * @project：Xbzd
 * @author：小卷子
 * @date 2018/4/14
 * @describe：图片加载框架
 * @fix：
 */
public class ImageUtils {

    private RequestOptions options;


    /**
     * ImageView的默认模式，在该模式下，图片会被等比缩放到能够填充控件大小，并居中展示：
     */
    public static final int fitCenter = 1;

    /**
     * 使用此模式以完全展示图片的内容为目的。图片将被等比缩放到能够完整展示在ImageView中并居中，如果图片大小小于控件大小，那么就直接居中展示该图片：
     */
    public static final int centerInside = 2;

    /**
     * 在该模式下，图片会被等比缩放直到完全填充整个ImageView，并居中显示。该模式也是最常用的模式了。
     */
    public static final int centerCrop = 3;


    /**
     * 图片会被裁剪成圆形
     */
    public static final int circle = 4;


    /**
     * 任意条件不满足 返回true
     *
     * @param context
     * @param url
     * @param view
     * @return
     */
    private boolean check(Context context, String url, View view) {


        return (TextUtils.isEmpty(url) || context == null || view == null);

    }

    /**
     * 任意条件不满足 返回true
     *
     * @param url
     * @return
     */
    private boolean check(String url) {


        return (TextUtils.isEmpty(url));

    }


    /**
     * 任意条件不满足 返回true
     *
     * @param context
     * @param view
     * @return
     */
    private boolean check(Context context, View view) {

        return (context == null || view == null);

    }


    /**
     * 任意条件不满足 返回true
     *
     * @param context
     * @param imageView
     * @return
     */
    private boolean check(Context context, ImageView imageView) {


        return (context == null || imageView == null);

    }


    /**
     * 获取默认配置
     *
     * @return
     */
    private RequestOptions getdDefaultRequestOptions() {

        options = new RequestOptions();
        //占位图
        if (UiConfig.getInstance().getImgPlaceholder() != -1) {
            options.placeholder(UiConfig.getInstance().getImgPlaceholder());
        }
        //错误图
        if (UiConfig.getInstance().getImgError() != -1) {
            options.error(UiConfig.getInstance().getImgError());
        }

        options.diskCacheStrategy(DiskCacheStrategy.ALL);

        return options;
    }


    /**
     * 占位图
     *
     * @param imgId
     * @return
     */
    public ImageUtils setImgPlaceholder(int imgId) {

        getOptions().placeholder(imgId);
        return this;
    }

    /**
     * 错误图
     *
     * @param imgId
     * @return
     */
    public ImageUtils setImgError(int imgId) {

        getOptions().error(imgId);
        return this;
    }


    /**
     * 设置缓存策略
     *
     * @param diskCacheStrategy
     * @return
     */
    public ImageUtils setCacheStrategy(DiskCacheStrategy diskCacheStrategy) {

        getOptions().diskCacheStrategy(diskCacheStrategy);
        return this;
    }


    private RequestOptions getOptions() {
        return options == null ? getdDefaultRequestOptions() : options;
    }


    /**
     * 设置配置
     *
     * @param scaleType
     * @return
     */
    public ImageUtils setOptions(int scaleType) {

        switch (scaleType) {

            case fitCenter:
                getOptions().fitCenter();
                break;

            case centerInside:
                getOptions().centerInside();
                break;
            case centerCrop:
                getOptions().centerCrop();
                break;

            case circle:
                getOptions().circleCrop();
                break;


            default:
                break;
        }

        return this;
    }



    public ImageUtils setTransformation(BitmapTransformation bitmapTransformation){

        getOptions().transform(bitmapTransformation);

        return this;
    }



    public ImageUtils setListener() {


        return this;
    }


    /**
     * 设置成圆角
     *
     * @param context
     * @param url
     * @param view
     */
    public void setBg(Context context, String url, final View view) {

        if (check(context, url, view)) {
            LogUtils.i("ImageUtils 传入的参数错误,请检查!!!");
            return;
        }

        Glide.with(context)
                .load(url)
                .apply(getOptions())
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable
                            Transition<? super Drawable> transition) {

                        if (Build.VERSION.SDK_INT >= 16) {
                            view.setBackground(resource);
                        } else {
                            view.setBackgroundDrawable(resource);
                        }

                    }
                });

    }


    /**
     * 加载图片 最后调用
     *
     * @param context
     * @param url
     * @param imageView
     */
    public void load(Context context, String url, ImageView imageView) {


        if (check(context, url, imageView)) {
            LogUtils.i("ImageUtils 传入的参数错误,请检查!!!");
            return;
        }

        Glide.with(context)
                .load(url)
                .apply(getOptions())
                .into(imageView);


    }



    /**
     * 添加圆角
     *
     * @param context
     * @param url
     * @param imageView
     */
    public void loadWithRound(Context context, String url, ImageView imageView, int round) {


        if (check(context, url, imageView)) {
            LogUtils.i("ImageUtils 传入的参数错误,请检查!!!");
            return;
        }
        RequestOptions requestOptions = getOptions().transforms(new CenterCrop(), new
                RoundedCorners(round));

        Glide.with(context)
                .load(url)
                .apply(requestOptions)
                .into(imageView);

    }


    //=========== 本地资源 ===========


    /**
     * 设置成圆角
     *
     * @param context
     * @param id
     * @param view
     */
    public void setBg(Context context, int id, final View view) {


        if (check(context, view)) {
            LogUtils.i("ImageUtils 传入的参数错误,请检查!!!");
            return;
        }

        Glide.with(context)
                .load(id)
                .apply(getOptions())
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable
                            Transition<? super Drawable> transition) {

                        if (Build.VERSION.SDK_INT >= 16) {
                            view.setBackground(resource);
                        } else {
                            view.setBackgroundDrawable(resource);
                        }

                    }
                });
    }


    /**
     * 加载图片 最后调用
     *
     * @param context
     * @param id
     * @param imageView
     */
    public void load(Context context, int id, ImageView imageView) {


        if (check(context, imageView)) {
            LogUtils.i("ImageUtils 传入的参数错误,请检查!!!");
            return;
        }

        Glide.with(context)
                .load(id)
                .apply(getOptions())
                .into(imageView);


    }


    /**
     * 添加圆角
     *
     * @param context
     * @param id
     * @param imageView
     */
    public void loadWithRound(Context context, int id, ImageView imageView, int round) {


        if (check(context, imageView)) {
            LogUtils.i("ImageUtils 传入的参数错误,请检查!!!");
            return;
        }
        RequestOptions requestOptions = getOptions().transforms(new CenterCrop(), new
                RoundedCorners(round));

        Glide.with(context)
                .load(id)
                .apply(requestOptions)
                .into(imageView);

    }


    //=========== 保存图片 ===========


    /**
     * 保存图片到图库
     *
     * @param url
     * @param savaListener
     */
    public void saveToGallery(final String url, final ImageSavaListener savaListener) {

        if (check(url)) {
            return;
        }

        String name = System.currentTimeMillis() + ".jpg";

        saveToGallery(url, name, savaListener);

    }

    /**
     * 保存图片到图库
     *
     * @param url
     * @param imgName
     * @param savaListener
     */
    public void saveToGallery(final String url, final String imgName, final ImageSavaListener
            savaListener) {

        if (check(url)) {
            return;
        }

        ThreadUtil.runOnChildThread(new Runnable() {
            @Override
            public void run() {
                try {
                    FutureTarget<Bitmap> submit = Glide.with(HydraUtilsManager.getInstance()
                            .getContext())
                            .asBitmap()
                            .load(url)
                            .submit();

                    final Bitmap bitmap = submit.get();

                    saveBitmapToGallery(bitmap, imgName, savaListener);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 保存biemap到图库
     *
     * @param bitmap
     * @param savaListener
     */
    public void saveBitmapToGallery(Bitmap bitmap, ImageSavaListener savaListener) {

        if (bitmap == null) {
            return;
        }

        String name = System.currentTimeMillis() + ".jpg";

        saveBitmapToGallery(bitmap, name, savaListener);

    }


    /**
     * 保存biemap到图库
     *
     * @param bitmap
     * @param imgName
     * @param savaListener
     */
    public void saveBitmapToGallery(Bitmap bitmap, String imgName, final ImageSavaListener
            savaListener) {


        File path = new File(Environment.getExternalStorageDirectory().getPath() +
                "/" + APPInfoUtil.getPackageNameLast());
        if (!path.exists()) {
            path.mkdirs();
        }

        final File imgFile = new File(path, imgName);

        FileOutputStream out;
        try {
            out = new FileOutputStream(imgFile);
            // 格式为 JPEG，照相机拍出的图片为JPEG格式的，PNG格式的不能显示在相册中
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)) {
                out.flush();
                out.close();
                // 插入图库
//                MediaStore.Images.Media.insertImage(context.getContentResolver(), imgFile
//                        .getAbsolutePath(), imgFile.getName(), null);
                // 发送广播，通知刷新图库的显示
                HydraUtilsManager.getInstance().getContext().sendBroadcast(new Intent(Intent
                        .ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse
                        ("file://" + imgFile.getAbsolutePath())));

                if (savaListener != null) {
                    ThreadUtil.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            savaListener.onSuccess(imgFile);
                        }
                    });

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (savaListener != null) {

                ThreadUtil.runOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        savaListener.onFail("sava erro");
                    }
                });
            }

        }
    }


}
