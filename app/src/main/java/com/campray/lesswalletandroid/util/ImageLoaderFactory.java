package com.campray.lesswalletandroid.util;

/**
 * UniversalImageLoader图片加载器的工厂类(实现单例)
 * Created by Administrator on 2017/11/24.
 */
public class ImageLoaderFactory {

    private static volatile ILoader sInstance;

    private ImageLoaderFactory() {}

    public static ILoader getLoader() {
        if (sInstance == null) {
            synchronized (ImageLoaderFactory.class) {
                if (sInstance == null) {
                    sInstance = new UniversalImageLoader();
                }
            }
        }
        return sInstance;
    }
}
