package com.campray.lesswalletandroid.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.campray.lesswalletandroid.LessWalletApplication;
import com.campray.lesswalletandroid.R;
import com.campray.lesswalletandroid.db.entity.Coupon;
import com.campray.lesswalletandroid.db.entity.History;
import com.campray.lesswalletandroid.db.entity.User;
import com.campray.lesswalletandroid.event.MessageEvent;
import com.campray.lesswalletandroid.listener.OperationListener;
import com.campray.lesswalletandroid.model.CouponModel;
import com.campray.lesswalletandroid.model.HistoryModel;
import com.campray.lesswalletandroid.ui.MainActivity;
import com.campray.lesswalletandroid.util.AppException;
import com.campray.lesswalletandroid.util.ResourcesUtils;
import com.campray.lesswalletandroid.util.Util;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import static android.content.ContentValues.TAG;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

/**
 * 消息同步服务
 * Created by Phills on 4/23/2018.
 */

public class MsgPushService extends Service {

    private Looper myServiceLooper;
    private ServiceHandler myServiceHandler;

    /**
     * 工作线程
     */
    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        /**
         * 处理消息
         * (在此方法中，我们将最终执行某些我们想要的后台工作)
         * @param msg
         */
        @Override
        public void handleMessage(Message msg) {
            try{
                Thread.sleep(5000);
                while (true){
                    User user=LessWalletApplication.INSTANCE().getAccount();
                    if(user!=null) {
                        //从服务端获取用户所有最新的未读日志
                        HistoryModel.getInstance().getAllUnreadedHistories(new OperationListener<List<History>>() {
                            @Override
                            public void done(List<History> list, AppException exe) {
                                if (exe == null) {
                                    //如果有未读记录
                                    if(list!=null&&list.size()>0) {
                                        EventBus.getDefault().post(new MessageEvent());
                                        //如果当前APP没有在前台运行，则在通知栏显示消息
                                        if (!DetectionService.isForegroundPkg("com.campray.lesswalletandroid")) {
                                            for (History history : list) {
                                                String title = "";
                                                switch (history.getType()) {
                                                    case 137://发送Coupon给好友成功消息
                                                        break;
                                                    case 138://收到好友送来Coupon的消息
                                                        //从服务端下载Coupon
                                                        CouponModel.getInstance().getCouponFromServer(Long.parseLong(history.getComment()), new OperationListener<Coupon>() {
                                                            @Override
                                                            public void done(Coupon obj, AppException exception) {}
                                                        });
                                                        title = getResources().getString(ResourcesUtils.getStringId(getApplicationContext(), "notification_coupon_received"));
                                                        String text = getResources().getString(ResourcesUtils.getStringId(getApplicationContext(), "notification_coupon_received_text"));
                                                        showNotification(LessWalletApplication.INSTANCE(), history.getId().intValue(), title, text);
                                                        break;
                                                    default:
                                                        title = "";
                                                }

                                            }
                                        }
                                    }
                                }
                            }
                        });
                    }
                    Thread.sleep(5000);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            //根据传来的消息startId参数，停止service
            stopSelf(msg.arg1);
        }

    }

    @Override
    public void onCreate() {

        Log.i(TAG, "onCreate called.");
        HandlerThread thread=new HandlerThread("ServiceStartArguments", Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();
        myServiceLooper=thread.getLooper();
        myServiceHandler=new ServiceHandler(myServiceLooper);
        super.onCreate();
    }

    /**
     * context.startService()模式时调用
     * 在Android2.0时系统引进了onStartCommand方法取代onStart方法，
     * 为了兼容以前的程序，在onStartCommand方法中其实调用了onStart方
     * @param intent
     * @param flags
     * @param startId
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand called.");
        //对于每个主进程发来的start请求，发送一个Message对象到工作线程，并传递startId值,
        //这样当我们完成一个工作线程时就知道该停止哪个请求服务
        Message msg=myServiceHandler.obtainMessage();
        msg.arg1=startId;
        myServiceHandler.sendMessage(msg);
        //return super.onStartCommand(intent,flags, startId);

        //若执行完onStartCommand()方法后，返回TART_STICKY值，系统就kill了service，
        // 不要再重新创建service，除非系统回传了一个pending intent。
        // 这避免了在不必要的时候运行service， 您的应用也可以restart任何未完成的操作
        return START_STICKY;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.i(TAG, "onStart called.");
    }

    /**
     * context.bindService()模式时调用
     * @param intent
     * @return
     */
    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind called.");
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(TAG, "onUnbind called.");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy called.");
    }

    /**
     * 发送通知栏消息
     * @param context
     * @param id 通知消息ID
     * @param title 标题
     * @param text 通知文本内容
     */
    private void showNotification(Context context, int id, String title, String text) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(R.mipmap.ic_launcher);//设置通知小ICON
        builder.setContentTitle(title);//设置通知栏标题
        builder.setContentText(text);//设置通知栏内容
        builder.setAutoCancel(true);//设置这个标志当用户单击面板就可以让通知将自动取消
        builder.setOnlyAlertOnce(true);
        //向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
        //Notification.DEFAULT_VIBRATE  添加默认震动提醒  需要VIBRATE权限
        //Notification.DEFAULT_SOUND    添加默认声音提醒
        //Notification.DEFAULT_LIGHTS   添加默认三色灯提醒
        //Notification.DEFAULT_ALL      添加默认以上3种全部提醒
        builder.setDefaults(Notification.DEFAULT_SOUND);
        //VISIBILITY_PUBLIC 只有在没有锁屏时会显示通知
        //VISIBILITY_PRIVATE 任何情况都会显示通知
        //VISIBILITY_SECRET 在安全锁和没有锁屏的情况下显示通知
        builder.setVisibility(Notification.VISIBILITY_PUBLIC);
        builder.setPriority(Notification.PRIORITY_DEFAULT);//设置该通知优先级
        builder.setCategory(Notification.CATEGORY_MESSAGE);//设置该通知类别

        // Creates an explicit intent for an Activity in your app
        //自定义打开的界面
        Intent resultIntent = new Intent(context, MainActivity.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //requestCode 想要区分不同的通知,可以设置此值
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        //builder.setContentIntent(contentIntent);//设置通知栏点击意图
        builder.setFullScreenIntent(contentIntent,true);//6.0特有的顶部悬停式通知栏
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(id, builder.build());//发送通知
    }

}

