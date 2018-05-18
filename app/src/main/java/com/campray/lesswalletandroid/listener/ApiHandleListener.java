package com.campray.lesswalletandroid.listener;

import com.campray.lesswalletandroid.util.AppException;

/**
 * Created by Phills on 9/5/2017.
 * 访问服务器API接口事件侦听器
 */

public abstract class ApiHandleListener<T> implements IListener {
    //当前进度：index是进度百分比数
    public void onProgress(int index){};
    //API访问完成
    public abstract void done(T obj, AppException exception);
}
