package com.lingmiao.distribution.imageloader;

import android.app.Activity;
import android.widget.ImageView;

import com.lingmiao.distribution.util.GlideUtil;
import com.lzy.imagepicker.loader.ImageLoader;

/**
 *  ImageLoader
 *  @author yandaocheng <br/>
 *	加载图片
 *	2018-05-03
 *	修改者，修改日期，修改内容
 */
public class GlideImageLoader implements ImageLoader {

    @Override
    public void displayImage(Activity activity, String path, ImageView imageView, int width, int height) {
        GlideUtil.load(activity,path,imageView,GlideUtil.getOption());
//
//        Glide.with(activity)                             //配置上下文
//                .load(Uri.fromFile(new File(path)))      //设置图片路径(fix #8,文件名包含%符号 无法识别和显示)
//                .error(R.mipmap.default_image)           //设置错误图片
//                .placeholder(R.mipmap.default_image)     //设置占位图片
//                .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存全尺寸
//                .into(imageView);
    }

    @Override
    public void displayImagePreview(Activity activity, String path, ImageView imageView, int width, int height) {
        GlideUtil.load(activity,path,imageView,GlideUtil.getOption());
//        Glide.with(activity)                             //配置上下文
//                .load(Uri.fromFile(new File(path)))      //设置图片路径(fix #8,文件名包含%符号 无法识别和显示)
//                .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存全尺寸
//                .into(imageView);
    }

    @Override
    public void clearMemoryCache() {
    }
}
