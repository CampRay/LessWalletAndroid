package com.campray.lesswalletandroid.util;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.campray.lesswalletandroid.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.youth.banner.loader.ImageLoader;

/**
 * Created by Phills on 6/20/2018.
 */

public class SliderImageLoader extends ImageLoader {

    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        /**
         注意：
         1.图片加载器由自己选择，这里不限制，只是提供几种使用方法
         2.返回的图片路径为Object类型，由于不能确定你到底使用的那种图片加载器，
         传输的到的是什么格式，那么这种就使用Object接收和返回，你只需要强转成你传输的类型就行，
         切记不要胡乱强转！
         */
        Uri uri = Uri.parse((String) path);
        //Picasso 加载图片简单用法
        Picasso.with(context).load(uri).resize(540,256).centerCrop().placeholder(R.mipmap.banner).memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE).into(imageView);
    }
}
