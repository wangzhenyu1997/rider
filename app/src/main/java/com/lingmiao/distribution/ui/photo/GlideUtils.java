package com.lingmiao.distribution.ui.photo;

import android.content.Context;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.lingmiao.distribution.R;

import java.io.File;

/**
 * Created by lqx on 2016/9/14.
 * desc:GlideUtils
 */
public class GlideUtils {


    public static void setImageUrl(Context context, ImageView iv, String url) {
        if(context == null || iv == null) {
            return;
        }
        Glide.with(context).load(url)
                .apply(new RequestOptions().centerCrop())
                .into(iv);
    }

    public static void setImageUrl(ImageView iv, String url) {
        setImageUrl(iv.getContext(), iv, url);
    }

    public static void setImageUrl1(ImageView iv, String url) {
        RequestOptions options = new RequestOptions().centerCrop()
                .placeholder(R.color.color_F1F1F1);
        Glide.with(iv.getContext()).load(url)
                .apply(options)
                .into(iv);
    }

    public static void setImageUrl(ImageView iv, File file) {
        setImageUrl(iv.getContext(), iv, file);
    }

    public static void setImageUrl(Context context, ImageView iv, File file) {
        if(context == null || iv == null) {
            return;
        }
        Glide.with(context).load(file)
                .into(iv);
    }

    public static void setImageUrl(ImageView iv, File file, float corner) {
        Glide.with(iv.getContext()).load(file)
                .apply(new RequestOptions().transforms(new CenterCrop(), new RoundedCorners(ConvertUtils.dp2px(corner))))
                .into(iv);
    }

    public static void setImageUrl(ImageView iv, String url, float corner) {
        Glide.with(Utils.getApp()).load(url)
                .apply(new RequestOptions().transforms(new CenterCrop(), new RoundedCorners(ConvertUtils.dp2px(corner))))
                .into(iv);
    }

    public static void setCornerImageUrl(ImageView iv, String url, float corner) {
        Glide.with(Utils.getApp()).load(url)
                .apply(new RequestOptions().transforms(new CenterCrop(), new RoundedCorners(ConvertUtils.dp2px(corner))))
//                .error(R.mipmap.default_load)
//                .placeholder(R.mipmap.default_load)
                .into(iv);
    }


    public static void setCircleImageUrl(Context context, ImageView iv, String url) {
        Glide.with(context).load(url)
//                .error(R.mipmap.default_load)
//                .placeholder(R.mipmap.default_load)
                .into(iv);
    }

    public static void setCircleImageUrl(Fragment context, ImageView iv, String url) {
        Glide.with(context).load(url)
//                .error(R.mipmap.default_load)
//                .placeholder(R.mipmap.default_load)
                .into(iv);
    }


}
