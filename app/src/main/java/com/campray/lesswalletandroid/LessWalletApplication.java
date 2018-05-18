package com.campray.lesswalletandroid;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.campray.lesswalletandroid.bean.UserBean;
import com.campray.lesswalletandroid.db.entity.User;
import com.campray.lesswalletandroid.model.UserModel;
import com.campray.lesswalletandroid.util.UniversalImageLoader;
import com.campray.lesswalletandroid.util.Util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;


/**
 * @author :Phills
 * @project:LessWalletApplication
 * @date :2017-08-20
 */
public class LessWalletApplication extends Application{
    private User account;//当前登录用户

    private static LessWalletApplication INSTANCE;
    public static LessWalletApplication INSTANCE(){
        return INSTANCE;
    }
    private void setInstance(LessWalletApplication app) {
        setLessWalletApplication(app);
    }
    private static void setLessWalletApplication(LessWalletApplication a) {
        LessWalletApplication.INSTANCE = a;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setInstance(this);
        //只有主进程运行的时候才需要初始化
        if (getApplicationInfo().packageName.equals(getMyProcessName())){
            //初始化
//            BmobIM.init(this);
//            //注册消息接收器
//            BmobIM.registerDefaultMessageHandler(new DemoMessageHandler(this));
        }
        //图片加载工具初始化
        UniversalImageLoader.initImageLoader(this);
    }

    /**
     * 获取当前运行的进程名
     * @return
     */
    public static String getMyProcessName() {
        try {
            File file = new File("/proc/" + android.os.Process.myPid() + "/" + "cmdline");
            BufferedReader mBufferedReader = new BufferedReader(new FileReader(file));
            String processName = mBufferedReader.readLine().trim();
            mBufferedReader.close();
            return processName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public User getAccount() {
        if(account==null){
            long userId=Util.getLongValue(this,"userId",0);
            if(userId!=0){
                account=UserModel.getInstance().getUserById(userId);
            }
        }
        return account;
    }

    public void setAccount(User account) {
        this.account = account;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if(account!=null&&account.getRemember()) {
            Util.putLongValue(this,"userId", account.getId());
        }
        else{
            Util.RemoveValue(this,"userId");
        }
    }
}
