package com.campray.lesswalletandroid.service;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.support.annotation.Nullable;
import android.util.Log;

import static android.content.ContentValues.TAG;

/**
 * 自定义的IntentService
 * IntentService会在后台线程会运行任务队列，所以不用担心多线程问题
 * Created by Phills on 4/23/2018.
 */

public class MyIntentService extends IntentService {

    public MyIntentService() {
        super("MyIntentService");
    }

    public MyIntentService(String name) {
        super(name);
    }

    /**
     * 处理按时间排序的Intent队列,所有请求处理完成后，
     * 自动停止Servide,无需手动调用stopSelf()方法
     * @param intent
     */
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
       //获取传入的参数
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            //String val=bundle.getString("key", "0");
            //在此执行相应的工作
        }
    }
}

