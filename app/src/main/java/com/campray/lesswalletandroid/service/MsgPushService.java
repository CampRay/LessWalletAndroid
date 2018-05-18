package com.campray.lesswalletandroid.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * 消息推送服务
 * Created by Phills on 4/23/2018.
 */

public class MsgPushService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
