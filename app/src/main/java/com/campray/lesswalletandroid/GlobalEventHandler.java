package com.campray.lesswalletandroid;

import android.content.Context;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Phills on 9/5/2017.
 * 自定义的全局Event事件处理器，例如离线后消息事件处理
 */
public class GlobalEventHandler {
    private Context context;

    public GlobalEventHandler(Context context) {
        this.context = context;
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onOfflineReceive(String offlineMessageEvent) {

    }
}
