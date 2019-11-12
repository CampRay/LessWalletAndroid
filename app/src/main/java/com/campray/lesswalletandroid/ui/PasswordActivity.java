package com.campray.lesswalletandroid.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import android.widget.EditText;

import com.campray.lesswalletandroid.R;
import com.campray.lesswalletandroid.db.entity.User;
import com.campray.lesswalletandroid.listener.OperationListener;
import com.campray.lesswalletandroid.model.UserModel;
import com.campray.lesswalletandroid.ui.base.ParentWithNaviActivity;
import com.campray.lesswalletandroid.util.AppException;
import com.campray.lesswalletandroid.util.ResourcesUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**修改密码界面
 * @author :Phills
 * @project:PasswordActivity
 * @date :2018-08-27-18:23
 */
public class PasswordActivity extends ParentWithNaviActivity {
    @BindView(R.id.et_oldpassword)
    EditText et_oldpassword;
    @BindView(R.id.et_password)
    EditText et_password;
    @BindView(R.id.et_password_again)
    EditText et_password_again;

    @Override
    protected String title() {
        return getResources().getString(R.string.account_password);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        initNaviView();
    }

    @OnClick(R.id.btn_confirm)
    public void onRegisterClick(View view){
        String oldpassword=et_oldpassword.getText().toString();
        String password=et_password.getText().toString();
        String passwordagain=et_password_again.getText().toString();

        if (TextUtils.isEmpty(oldpassword)) {
            toast(getResources().getString(ResourcesUtils.getStringId(this.getApplicationContext(),"E_2007")));
            return;
        }
        if (TextUtils.isEmpty(password)) {
            toast(getResources().getString(ResourcesUtils.getStringId(this.getApplicationContext(),"E_2008")));
            return;
        }
        else if(password.length()<6){
            toast(getResources().getString(ResourcesUtils.getStringId(this.getApplicationContext(),"account_password_mark")));
            return;
        }
        if (TextUtils.isEmpty(passwordagain)) {
            toast(getResources().getString(ResourcesUtils.getStringId(this.getApplicationContext(),"E_2016")));
            return;
        }
        if (!password.equals(passwordagain)) {
            toast(getResources().getString(ResourcesUtils.getStringId(this.getApplicationContext(),"E_2009")));
            return;
        }

        //修改
        UserModel.getInstance().editUser(null, password,new OperationListener<User>() {
            @Override
            public void done(User user, AppException exe) {
                if (exe == null) {
                    toast(getResources().getString(ResourcesUtils.getStringId(PasswordActivity.this.getApplicationContext(),"account_password_success")));
                } else {
                    toast(exe.toString(getApplicationContext()));
                }
            }
        });
    }



}
