package com.campray.lesswalletandroid.util;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.ImageDownloader;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;



/**
 * 使用UIL图片框架加载图片，后续方便扩展其他图片框架，比如glide或fresco
 * Created by Administrator on 2016/5/24.
 */
public class UniversalImageLoader implements ILoader{

    public UniversalImageLoader(){}

    /**
     * 对文件路径进行封装,得到可以URL
     * @param path 例如："/mnt/sdcard/image.png"
     * @return
     */
    public String getUrlFromFile(String path){
        return ImageDownloader.Scheme.FILE.wrap(path);
    }

    /**
     * 对资源文件进行封装,得到可以URL
     * @param path 例如："R.drawable.image"
     * @return
     */
    public String getUrlFromDrawable(String path){
        return ImageDownloader.Scheme.DRAWABLE.wrap(path);
    }

    /**
     * 对Assets中的文件进行封装,得到可以URL
     * @param path 例如："image.png"
     * @return
     */
    public String getUrlFromAssets(String path){
        return ImageDownloader.Scheme.ASSETS.wrap(path);
    }

    /**
     * 加载图片成圆形(主要是头像)
     * @param iv 显示图片的View
     * @param url 图片路径 Image URI (i.e. "http://site.com/image.png", "file:///mnt/sdcard/image.png")
     * @param defaultRes 默认的图片资源
     */
    @Override
    public void loadAvator(ImageView iv, String url, int defaultRes) {
        if(!TextUtils.isEmpty(url)){
            display(iv,url,true,defaultRes,null);
        } else {
            iv.setImageResource(defaultRes);
        }
    }

    /**
     * 加载图片，添加监听器
     * @param iv 显示图片的View
     * @param url 图片路径 Image URI (i.e. "http://site.com/image.png", "file:///mnt/sdcard/image.png")
     * @param defaultRes 默认的图片资源
     * @param listener
     */
    @Override
    public void load(ImageView iv, String url, int defaultRes,ImageLoadingListener listener) {
        if(!TextUtils.isEmpty(url)){
            display(iv,url.trim(),false,defaultRes,listener);
        } else {
            iv.setImageResource(defaultRes);
        }
    }

    /**
     * 展示图片
     * @param iv 要显示图片的
     * @param url Image URI (i.e. "http://site.com/image.png", "file:///mnt/sdcard/image.png")
     * @param defaultRes 默认显示的图片
     * @param listener
     */
    private void display(ImageView iv,String url,boolean isCircle,int defaultRes,ImageLoadingListener listener){
        if(!url.equals(iv.getTag())){//增加tag标记，减少UIL的display次数
            iv.setTag(url);
            //不直接display imageview改为ImageAware，解决ListView滚动时重复加载图片
            ImageAware imageAware = new ImageViewAware(iv, false);
            ImageLoader.getInstance().displayImage(url, imageAware, DisplayConfig.getDefaultOptions(isCircle,defaultRes),listener);
        }
    }

    /**
     * 初始化ImageLoader
     * @param context
     */
    public static void initImageLoader(Context context) {
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPoolSize(3);
        config.memoryCache(new WeakMemoryCache());
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
//        config.writeDebugLogs(); // Remove for release app
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }
}
