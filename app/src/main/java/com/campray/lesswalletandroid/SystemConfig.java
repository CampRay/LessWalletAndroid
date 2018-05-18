package com.campray.lesswalletandroid;

import android.os.Environment;

/**
 * @author :Phills
 * @project:Config
 * @date :2016-01-15-18:23
 */
public class SystemConfig {

    //是否是debug模式
    public static final boolean DEBUG=true;


    public static final int REQUESTCODE_TAKE_CAMERA = 0x000001;//拍照
    public static final int REQUESTCODE_TAKE_LOCAL = 0x000002;//本地图片
    public static final int REQUESTCODE_TAKE_VEDIO = 0x000003;//錄像

    /**
     * 存放发送图片的目录
     */
    public static String PICTURE_PATH = Environment.getExternalStorageDirectory()	+ "/lesswallet/image/";

}
