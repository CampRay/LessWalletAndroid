package com.campray.lesswalletandroid.ui;

import android.Manifest;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.campray.lesswalletandroid.LessWalletApplication;
import com.campray.lesswalletandroid.R;
import com.campray.lesswalletandroid.bean.CouponBean;
import com.campray.lesswalletandroid.bean.UserBean;
import com.campray.lesswalletandroid.listener.OperationListener;
import com.campray.lesswalletandroid.model.CouponModel;
import com.campray.lesswalletandroid.ui.base.BaseActivity;
import com.campray.lesswalletandroid.ui.base.MenuActivity;
import com.campray.lesswalletandroid.util.AppException;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by Phills on 11/2/2017.
 */

public class MainActivity extends MenuActivity {
    @BindView(R.id.ib_wicoupon)
    ImageButton ib_wicoupon;
    @BindView(R.id.ib_wicard)
    ImageButton ib_wicard;
    @BindView(R.id.ib_witicket)
    ImageButton ib_witicket;



    @BindView(R.id.iv_alipay)
    ImageView iv_alipay;
    @BindView(R.id.iv_wechat)
    ImageView iv_wechat;

    @BindView(R.id.ib_addpay)
    ImageButton ib_addpay;
    @BindView(R.id.ib_web)
    ImageButton ib_web;
    @BindView(R.id.ib_coin)
    ImageButton ib_coin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.initPermissions();
    }

    /**
     * 点击wicoupon按钮的事件方法
     * @param view
     */
    @OnClick(R.id.ib_wicoupon)
    public void onWicouponClick(View view){
        Bundle bundle=new Bundle();
        bundle.putInt("type_id", 1);
        startActivity(CouponActivity.class,bundle,true);
    }
    /**
     * 点击wicard按钮的事件方法
     * @param view
     */
    @OnClick(R.id.ib_wicard)
    public void onWicardClick(View view){
        Bundle bundle=new Bundle();
        bundle.putInt("type_id", 2);
        startActivity(CouponActivity.class,bundle,true);
    }
    /**
     * 点击witicket按钮的事件方法
     * @param view
     */
    @OnClick(R.id.ib_witicket)
    public void onWiticketClick(View view){
        Bundle bundle=new Bundle();
        bundle.putInt("type_id", 3);
        startActivity(CouponActivity.class,bundle,true);
    }

    /**
     * 点击Alipay按钮的事件方法
     * @param view
     */
    @OnClick(R.id.iv_alipay)
    public void onAlipayClick(View view){
        Bundle bundle=new Bundle();
        bundle.putInt("type_id", 1);
        startActivity(CouponActivity.class,bundle,true);
    }

    /**
     * 点击Wechat按钮的事件方法
     * @param view
     */
    @OnClick(R.id.iv_wechat)
    public void onWechatClick(View view){
        Bundle bundle=new Bundle();
        bundle.putInt("type_id", 1);
        startActivity(CouponActivity.class,bundle,true);
    }

    /**
     * 点击AddPay按钮的事件方法
     * @param view
     */
    @OnClick(R.id.ib_addpay)
    public void onAddPayClick(View view){
        Bundle bundle=new Bundle();
        bundle.putInt("type_id", 1);
        startActivity(CouponActivity.class,bundle,true);
    }

    /**
     * 点击Web按钮的事件方法
     * @param view
     */
    @OnClick(R.id.ib_web)
    public void onWebClick(View view){
        Bundle bundle=new Bundle();
        bundle.putInt("type_id", 1);
        startActivity(CouponActivity.class,bundle,true);
    }

    /**
     * 点击Coin按钮的事件方法
     * @param view
     */
    @OnClick(R.id.ib_coin)
    public void onCoinClick(View view){
        Bundle bundle=new Bundle();
        bundle.putInt("type_id", 1);
        startActivity(CouponActivity.class,bundle,true);
    }

}
