package com.campray.lesswalletandroid.receiver;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * 网络连接状态的广播接收器
 * Created by Phills on 4/23/2018.
 */

public class NetWorkStateReceiver extends BroadcastReceiver {

    @SuppressLint("WrongConstant")
    @Override
    public void onReceive(Context context, Intent intent) {
        //网络中断时显示消息
        if (!isNetworkAvailable(context)) {
            Toast.makeText(context, "network disconnected!", 0).show();
        }
    }

    /**
     * 网络是否可用
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager mgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] info = mgr.getAllNetworkInfo();
        if (info != null) {
            for (int i = 0; i < info.length; i++) {
                if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }

}
