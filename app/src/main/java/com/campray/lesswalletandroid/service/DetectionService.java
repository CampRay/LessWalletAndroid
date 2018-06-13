package com.campray.lesswalletandroid.service;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

/**
 * 窗口状态探测服务
 * (主要用于检测当前应用窗口是否在最前)
 * 优势：
 * 不需要权限请求
 * 可以用来判断任意应用甚至 Activity, PopupWindow, Dialog 对象是否处于前台
 * 是一个稳定的方法，可以长期使用的可能很大
 *
 * 劣势：
 * 需要要用户开启辅助功能
 * 辅助功能会伴随应用被“强行停止”而剥夺
 *
 * 需要在res/xml/下创建detection_service_config.xml属性配置文件
 * Created by Phills on 5/21/2018.
 */

public class DetectionService extends AccessibilityService {
    final static String TAG = "DetectionService";

    static String foregroundPackageName;

    /**
     * 重载辅助功能事件回调函数，对窗口状态变化事件进行处理
     * @param event
     */
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            /*
             * 如果 与 DetectionService 相同进程，直接比较 foregroundPackageName 的值即可
             * 如果在不同进程，可以利用 Intent 或 bind service 进行通信
             */
            foregroundPackageName = event.getPackageName().toString();

            /*
             * 基于以下还可以做很多事情，比如判断当前界面是否是 Activity，是否系统应用等，与主题无关就不再展开。
             */
            //ComponentName cName = new ComponentName(event.getPackageName().toString(),event.getClassName().toString());
        }
    }

    @Override
    public void onInterrupt() {

    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY_COMPATIBILITY;// 根据需要返回不同的语义值
    }

    /**
     * 方法：使用 Android AccessibilityService 探测窗口变化，跟据系统回传的参数获取 前台对象 的包名与类名
     *
     * @param packageName 需要检查是否位于栈顶的App的包名
     */
    public static boolean isForegroundPkg(String packageName) {
        return packageName.equals(foregroundPackageName);
    }

    // 此方法用来判断当前应用的辅助功能服务是否开启
    public static boolean isAccessibilitySettingsOn(Context context) {
        int accessibilityEnabled = 0;
        try {
            accessibilityEnabled = Settings.Secure.getInt(context.getContentResolver(),
                    android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException e) {
            Log.i(TAG, e.getMessage());
        }

        if (accessibilityEnabled == 1) {
            String services = Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (services != null) {
                return services.toLowerCase().contains(context.getPackageName().toLowerCase());
            }
        }
        return false;
    }


    /**
     * 方法： 打开手机的无障碍配置页面，让用户手动开启当前应用的无障碍功能,否则此窗口状态探测服务类将不起作用
     */
    private void initAccessibility() {
        // 判断辅助功能是否开启
        if (!DetectionService.isAccessibilitySettingsOn(this.getApplicationContext())) {
            // 引导至辅助功能设置页面
            startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
        } else {
            // 执行辅助功能服务相关操作
        }
    }
}
