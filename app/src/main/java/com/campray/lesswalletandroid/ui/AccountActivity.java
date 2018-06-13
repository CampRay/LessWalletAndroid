package com.campray.lesswalletandroid.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.campray.lesswalletandroid.LessWalletApplication;
import com.campray.lesswalletandroid.R;
import com.campray.lesswalletandroid.db.entity.Friend;
import com.campray.lesswalletandroid.db.entity.User;
import com.campray.lesswalletandroid.listener.OperationListener;
import com.campray.lesswalletandroid.model.FriendModel;
import com.campray.lesswalletandroid.model.UserModel;
import com.campray.lesswalletandroid.ui.base.MenuActivity;
import com.campray.lesswalletandroid.util.AppException;
import com.campray.lesswalletandroid.util.ImageLoaderFactory;
import com.campray.lesswalletandroid.util.ResourcesUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Phills on 11/2/2017.
 */

public class AccountActivity extends MenuActivity {
    @BindView(R.id.iv_left)
    ImageView iv_left;
    @BindView(R.id.tv_navi_title)
    TextView tv_navi_title;

    @BindView(R.id.iv_avatar)
    ImageView iv_avatar;

    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_mobile)
    TextView tv_mobile;
    @BindView(R.id.tv_email)
    TextView tv_email;
    @BindView(R.id.tv_country)
    TextView tv_country;
    @BindView(R.id.tv_logout)
    TextView tv_logout;

    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        tv_navi_title.setText(getResources().getString(ResourcesUtils.getStringId(getApplicationContext(),"account_title")));
        User user= LessWalletApplication.INSTANCE().getAccount();
        tv_email.setText(user.getEmail());
        tv_mobile.setText(user.getMobile());
        tv_country.setText(user.getCountry().getName());
        tv_name.setText(user.getFullName());
    }

    /**
     * 点击iv_left按钮的事件方法
     * @param view
     */
    @OnClick(R.id.iv_left)
    public void onBackClick(View view){
        startActivity(ProfileActivity.class,null,true);
    }

    /**
     * 点击tv_logout按钮的事件方法
     * @param view
     */
    @OnClick(R.id.tv_logout)
    public void onLogoutClick(View view){
        //添加好友
        UserModel.getInstance().logout();
        startActivity(LoginActivity.class,null,true);
    }

}
