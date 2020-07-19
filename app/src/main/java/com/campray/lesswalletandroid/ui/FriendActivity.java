package com.campray.lesswalletandroid.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.campray.lesswalletandroid.R;
import com.campray.lesswalletandroid.db.entity.Coupon;
import com.campray.lesswalletandroid.db.entity.Friend;
import com.campray.lesswalletandroid.listener.OperationListener;
import com.campray.lesswalletandroid.model.CouponModel;
import com.campray.lesswalletandroid.model.FriendModel;
import com.campray.lesswalletandroid.model.UserModel;
import com.campray.lesswalletandroid.ui.base.MenuActivity;
import com.campray.lesswalletandroid.util.AppException;
import com.campray.lesswalletandroid.util.ResourcesUtils;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Phills on 11/2/2017.
 */

public class FriendActivity extends MenuActivity {
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
    @BindView(R.id.tv_add)
    TextView tv_add;

    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);
        long friendId=this.getBundle().getLong("id");
        //如果参数好友ID有值，表示当前是要显示好友信息
        if(friendId>0){
            tv_add.setVisibility(View.GONE);
            tv_navi_title.setText(getResources().getString(ResourcesUtils.getStringId(getApplicationContext(),"friend_title")));
        }
        else{//否则表示是要添加好友
            tv_add.setVisibility(View.VISIBLE);
            tv_navi_title.setText(getResources().getString(ResourcesUtils.getStringId(getApplicationContext(),"friend_title_add")));
            userName=this.getBundle().getString("userName");
            String mobile=this.getBundle().getString("mobile");
            String email=this.getBundle().getString("email");
            String firstName=this.getBundle().getString("firstName");
            String lastName=this.getBundle().getString("lastName");
            String avatarUrl=this.getBundle().getString("avatarUrl");
            String country=this.getBundle().getString("country");
            //Picasso.with(this).load(avatarUrl).placeholder(R.mipmap.icon_account).memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE).into(iv_avatar);
            Picasso.get().load(avatarUrl).placeholder(R.mipmap.icon_account).memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE).into(iv_avatar);
            tv_email.setText(email);
            tv_mobile.setText(mobile);
            tv_country.setText(country);

            //全是字母的正则表达式
            String regex = "^[A-Za-z]+$";
            Pattern p = Pattern.compile(regex);
            //判断Str是否匹配，返回匹配结果
           if(p.matcher(firstName).find()&&p.matcher(lastName).find()){
               tv_name.setText(firstName+" "+lastName);
           }
           else{
               tv_name.setText(lastName+" "+firstName);
           }
        }

    }

    /**
     * 点击iv_left按钮的事件方法
     * @param view
     */
    @OnClick(R.id.iv_left)
    public void onBackClick(View view){
        this.finish();
    }

    /**
     * 点击tv_add按钮的事件方法
     * @param view
     */
    @OnClick(R.id.tv_add)
    public void onAddClick(View view){
        //添加好友
        FriendModel.getInstance().addFriend(userName,new OperationListener<Friend>() {
            @Override
            public void done(Friend friend, AppException exe) {
                if (exe == null) {
                    toast(getResources().getString(ResourcesUtils.getStringId(getApplicationContext(),"friend_add_success")));
                    startActivity(FriendsActivity.class,null,true);
                } else {
                    toast(exe.toString(getApplicationContext()));
                }
            }
        });
        this.finish();
    }

}
