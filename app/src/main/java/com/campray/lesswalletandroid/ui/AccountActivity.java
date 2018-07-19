package com.campray.lesswalletandroid.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.Base64;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.campray.lesswalletandroid.LessWalletApplication;
import com.campray.lesswalletandroid.R;
import com.campray.lesswalletandroid.db.entity.User;
import com.campray.lesswalletandroid.db.service.UserDaoService;
import com.campray.lesswalletandroid.listener.OperationListener;
import com.campray.lesswalletandroid.model.UserModel;
import com.campray.lesswalletandroid.qrcode.encode.QRCodeEncoder;
import com.campray.lesswalletandroid.ui.base.MenuActivity;
import com.campray.lesswalletandroid.util.AppException;
import com.campray.lesswalletandroid.util.ResourcesUtils;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Phills on 5/2/2018.
 */

public class AccountActivity extends MenuActivity {
    @BindView(R.id.iv_left)
    ImageView iv_left;
    @BindView(R.id.tv_navi_title)
    TextView tv_navi_title;
    @BindView(R.id.tv_right)
    TextView tv_right;

    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_mobile)
    TextView tv_mobile;
    @BindView(R.id.tv_email)
    TextView tv_email;
    @BindView(R.id.tv_country)
    TextView tv_country;
    @BindView(R.id.tv_points)
    TextView tv_points;

    @BindView(R.id.iv_avatar)
    ImageView iv_avatar;
    @BindView(R.id.iv_qrcode)
    ImageView iv_qrcode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        //tv_right.setText(getResources().getString(ResourcesUtils.getStringId(getApplicationContext(),"logout")));
        tv_right.setText("…");
        tv_navi_title.setText(getResources().getString(ResourcesUtils.getStringId(getApplicationContext(),"account_title")));
        User user= LessWalletApplication.INSTANCE().getAccount();
        tv_email.setText(user.getEmail());
        tv_mobile.setText(user.getMobile());
        tv_country.setText(user.getCountry().getName());
        tv_name.setText(user.getFullName());
        tv_points.setText(user.getPoints()+"");
        Picasso.with(this).load(user.getAvatorPath()).placeholder(R.mipmap.icon_account).memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE).into(iv_avatar);

        String userStr="user:"+ user.getId();
        Bitmap qrCodeBitmap= null;
        try {
            qrCodeBitmap = QRCodeEncoder.encodeAsBitmap(Base64.encodeToString(userStr.getBytes("UTF-8"),Base64.DEFAULT), BarcodeFormat.QR_CODE,120);
            iv_qrcode.setImageBitmap(qrCodeBitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //从服务器重新查询用户信息,获取积分
        //查询用户积分
        UserModel.getInstance().getUserPoints(user.getId(), new OperationListener<User>() {
            @Override
            public void done(User obj, AppException exception) {
                if(obj!=null) {
                    tv_points.setText(obj.getPoints());
                }
            }
        });
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
    @OnClick(R.id.tv_right)
    public void onLogoutClick(View view){
        //创建弹出式菜单对象（最低版本11）
        PopupMenu popup = new PopupMenu(this, view);
        //获取菜单填充器
        MenuInflater inflater = popup.getMenuInflater();
        //填充菜单
        inflater.inflate(R.menu.popup_menu_profile, popup.getMenu());
        //绑定菜单项的点击事件
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_setting:
                        startActivity(SettingsActivity.class,null,false);
                        break;
                    case R.id.item_logout:
                        UserModel.getInstance().logout();
                        startActivity(LoginActivity.class,null,true);
                        break;
                    default:
                        break;
                    }
                return false;
            }
        });
        popup.show();
    }

}
