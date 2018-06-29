package com.campray.lesswalletandroid.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.campray.lesswalletandroid.LessWalletApplication;
import com.campray.lesswalletandroid.R;
import com.campray.lesswalletandroid.db.entity.Country;
import com.campray.lesswalletandroid.db.entity.Currency;
import com.campray.lesswalletandroid.db.entity.Language;
import com.campray.lesswalletandroid.db.entity.ProductAttribute;
import com.campray.lesswalletandroid.db.entity.Slider;
import com.campray.lesswalletandroid.db.entity.User;
import com.campray.lesswalletandroid.listener.OperationListener;
import com.campray.lesswalletandroid.model.CountryModel;
import com.campray.lesswalletandroid.model.CurrencyModel;
import com.campray.lesswalletandroid.model.LanguageModel;
import com.campray.lesswalletandroid.model.ProductAttributeModel;
import com.campray.lesswalletandroid.model.SliderModel;
import com.campray.lesswalletandroid.model.UserModel;
import com.campray.lesswalletandroid.service.MsgPushService;
import com.campray.lesswalletandroid.ui.base.BaseActivity;
import com.campray.lesswalletandroid.util.AppException;

import java.util.List;

/**启动界面
 * @author :smile
 * @project:SplashActivity
 * @date :2016-01-15-18:23
 */
public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //运行后台服务
        startService();
        Handler handler =new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                User user= LessWalletApplication.INSTANCE().getAccount();
                //获取最新的广告轮播图片
                SliderModel.getInstance().getAllSlidersFromServer(new OperationListener<List<Slider>>() {
                    @Override
                    public void done(List<Slider> obj, AppException exception) {}
                });

                //第一次启用App时下载所有初始数据
                if(user == null){
                    //下载国家数据
                    CountryModel.getInstance().getAllCountriesFromServer(new OperationListener<List<Country>>(){
                        @Override
                        public void done(List<Country> obj, AppException exception) {}
                    });
                    //下载语言数据
                    LanguageModel.getInstance().getAllLanguagesFromServer(new OperationListener<List<Language>>(){
                        @Override
                        public void done(List<Language> obj, AppException exception) {}
                    });
                    //下载货币数据
                    CurrencyModel.getInstance().getAllCurrenciesFromServer(new OperationListener<List<Currency>>(){
                        @Override
                        public void done(List<Currency> obj, AppException exception) {}
                    });
                    //下载商品属性数据
                    ProductAttributeModel.getInstance().getAllProdAttrFromServer(new OperationListener<List<ProductAttribute>>(){
                        @Override
                        public void done(List<ProductAttribute> obj, AppException exception) {}
                    });
                    startActivity(LoginActivity.class,null,true);
                }
                else if (!user.getRemember()) {//不是自动登入
                    startActivity(LoginActivity.class,null,true);
                }else{//自动登入
                    startActivity(MainActivity.class,null,true);
                }
            }
        },1000);

    }

    /**
     * 启动消息同步Service
     */
    private void startService(){
        Intent intent = new Intent(this, MsgPushService.class);
        startService(intent);
    }
}
