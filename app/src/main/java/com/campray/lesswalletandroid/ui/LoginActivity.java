package com.campray.lesswalletandroid.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.campray.lesswalletandroid.LessWalletApplication;
import com.campray.lesswalletandroid.R;
import com.campray.lesswalletandroid.db.entity.Coupon;
import com.campray.lesswalletandroid.db.entity.Friend;
import com.campray.lesswalletandroid.db.entity.User;
import com.campray.lesswalletandroid.event.LoginedEvent;
import com.campray.lesswalletandroid.listener.OperationListener;
import com.campray.lesswalletandroid.model.CouponModel;
import com.campray.lesswalletandroid.model.FriendModel;
import com.campray.lesswalletandroid.model.UserModel;
import com.campray.lesswalletandroid.ui.base.BaseActivity;
import com.campray.lesswalletandroid.util.AppException;
import com.campray.lesswalletandroid.util.Util;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


/**登陆界面
 * @author :Phills
 * @project:LoginActivity
 * @date :2016-01-15-18:23
 */
public class LoginActivity extends BaseActivity {

    @BindView(R.id.et_username)
    EditText et_username;
    @BindView(R.id.et_password)
    EditText et_password;
    @BindView(R.id.cb_remember)
    CheckBox cb_remember;
    @BindView(R.id.btn_login)
    Button btn_login;
    @BindView(R.id.tv_register)
    TextView tv_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        User userBean=LessWalletApplication.INSTANCE().getAccount();
        if(userBean!=null){
            et_username.setText(userBean.getUserName());
            cb_remember.setChecked(userBean.getRemember());
        }
    }

    @OnClick(R.id.btn_login)
    public void onLoginClick(View view){
        UserModel.getInstance().login(et_username.getText().toString(), et_password.getText().toString(), cb_remember.isChecked(),new OperationListener<User>() {
            @Override
            public void done(User user, AppException exe) {
            if (exe == null) {
                LessWalletApplication.INSTANCE().setAccount(user);
//                //获取当前用户在此App登录次数
//                int loginNum=Util.getIntValue(LoginActivity.this,user.getUserName());
//                //如果用户是第一次登录，则需从服务器获取所有的卡卷数据
//                if(loginNum==0){
                    CouponModel.getInstance().getAllCouponsFromServer(new OperationListener<List<Coupon>>(){
                        @Override
                        public void done(List<Coupon> obj, AppException exception) {}
                    });
                    FriendModel.getInstance().getAllFriends(new OperationListener<List<Friend>>() {
                        @Override
                        public void done(List<Friend> obj, AppException exception) {}
                    });
//                }
//                loginNum++;
//                Util.putIntValue(LoginActivity.this,user.getUserName(),loginNum);
                startActivity(MainActivity.class, null, true);
            } else {
                toast(exe.toString(getApplicationContext()));
            }
            }
        });
    }

    @OnClick(R.id.tv_register)
    public void onRegisterClick(View view){
        startActivity(RegisterActivity.class, null, false);
    }

    @Subscribe
    public void onEventMainThread(LoginedEvent event){
        finish();
    }
}
