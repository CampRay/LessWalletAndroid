package com.campray.lesswalletandroid.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.campray.lesswalletandroid.service.MsgPushService;

/**
 * 手机开机启动成功后的广播接收器
 * Created by Phills on 4/23/2018.
 */

public class BootCompleteReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent service = new Intent(context, MsgPushService.class);
        context.startService(service);

    }
}
