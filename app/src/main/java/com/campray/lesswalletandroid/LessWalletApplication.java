package com.campray.lesswalletandroid;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;

import com.campray.lesswalletandroid.db.entity.User;
import com.campray.lesswalletandroid.model.UserModel;
import com.campray.lesswalletandroid.util.Util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;


/**
 * @author :Phills
 * @project:LessWalletApplication
 * @date :2017-08-20
 */
public class LessWalletApplication extends Application{
    private User account;//当前登录用户
    private List<Long> attentions=new ArrayList<Long>();

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
        }
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

    public List<Long> getAttentions() {
        return attentions;
    }

    public void setAttentions(List<Long> attentions) {
        this.attentions = attentions;
    }

    /**
     *  获取应用自身的数据目录( mnt/sdcard/Android/data/< package name >/files/ )
     *  并添加.nomedia文件目录（图片文件不会被手机相册扫描到）
     * （有扩展存储空间时获取扩展空间的应用目录，没有就获取手机内存的应用目录）
     * @return
     */
    public File getPrivateDir(){
        //有扩展存储空间
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return LessWalletApplication.INSTANCE().getExternalFilesDir(".nomedia");
        }
        else{
            //获取内部存储空间的目录
            File file=LessWalletApplication.INSTANCE().getFilesDir();
            File nomediaFile=new File(file,".nomedia");
            if(!nomediaFile.exists()){
                nomediaFile.mkdir();
            }
            return nomediaFile;
        }
    }

    /**
     *  获取手机公开的图片目录( mnt/sdcard/Pictures/ )
     * @return
     */
    public File getPublicPicDir(){
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
    }
}
