package com.campray.lesswalletandroid.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.campray.lesswalletandroid.R;
import com.campray.lesswalletandroid.ui.base.MenuActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 打开个人资料页面
 * Created by Phills on 4/8/2018.
 */

public class ProfileActivity extends MenuActivity {
    @BindView(R.id.ll_account)
    LinearLayout ll_account;
    @BindView(R.id.ll_friends)
    LinearLayout ll_friends;
    @BindView(R.id.ll_skin)
    LinearLayout ll_skin;
    @BindView(R.id.ll_setting)
    LinearLayout ll_setting;
    @BindView(R.id.ll_history)
    LinearLayout ll_history;
    @BindView(R.id.ll_trash)
    LinearLayout ll_trash;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
    }

    /**
     * 点击acount按钮的事件方法
     * @param view
     */
    @OnClick(R.id.ll_account)
    public void onAccountClick(View view){
        startActivity(AccountActivity.class,null,true);
    }

    /**
     * 点击friends按钮的事件方法
     * @param view
     */
    @OnClick(R.id.ll_friends)
    public void onFriendsClick(View view){
        startActivity(FriendsActivity.class,null,true);
    }

    /**
     * 点击skin按钮的事件方法
     * @param view
     */
    @OnClick(R.id.ll_skin)
    public void onSkinClick(View view){
        startActivity(AccountActivity.class,null,true);
    }

    /**
     * 点击setting按钮的事件方法
     * @param view
     */
    @OnClick(R.id.ll_setting)
    public void onSettingClick(View view){
        startActivity(AccountActivity.class,null,true);
    }

    /**
     * 点击history按钮的事件方法
     * @param view
     */
    @OnClick(R.id.ll_history)
    public void onHistoryClick(View view){
        startActivity(CouponActivity.class,null,true);
    }

    /**
     * 点击trash按钮的事件方法
     * @param view
     */
    @OnClick(R.id.ll_trash)
    public void onTrashClick(View view){
        startActivity(AccountActivity.class,null,true);
    }
}
