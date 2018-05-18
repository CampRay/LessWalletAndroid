package com.campray.lesswalletandroid.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import com.campray.lesswalletandroid.R;
import com.campray.lesswalletandroid.db.entity.Country;
import com.campray.lesswalletandroid.db.entity.User;
import com.campray.lesswalletandroid.listener.OperationListener;
import com.campray.lesswalletandroid.model.CountryModel;
import com.campray.lesswalletandroid.model.UserModel;
import com.campray.lesswalletandroid.ui.base.ParentWithNaviActivity;
import com.campray.lesswalletandroid.util.AppException;
import com.campray.lesswalletandroid.util.ResourcesUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**注册界面
 * @author :Phills
 * @project:RegisterActivity
 * @date :2016-01-15-18:23
 */
public class RegisterActivity extends ParentWithNaviActivity {
    @BindView(R.id.et_fname)
    EditText et_fname;
    @BindView(R.id.et_lname)
    EditText et_lname;
    @BindView(R.id.et_mobile)
    EditText et_mobile;
    @BindView(R.id.et_email)
    EditText et_email;
    @BindView(R.id.et_address)
    EditText et_address;
    @BindView(R.id.et_password)
    EditText et_password;
    @BindView(R.id.btn_register)
    Button btn_register;

    @BindView(R.id.et_password_again)
    EditText et_password_again;
    @BindView(R.id.sp_country)
    Spinner sp_country;
    //国家下拉列表数据集合
    private List<Map<String, Object>> dropdownDataList=new ArrayList<Map<String, Object>>();
    //国家选择框显示数据集合
    private List<String> displayDataList=new ArrayList<String>();

    @Override
    protected String title() {
        return getResources().getString(R.string.register);
    }

    //设置国家下拉列表数据源
    private void setCountryDisplayData() {
        List<Country> countryList= CountryModel.getInstance().getAllCountries();
        if(countryList!=null) {
            for (Country bean : countryList) {
                displayDataList.add(bean.getName());
                Map<String, Object> map = new HashMap<>();
                map.put("name", bean.getName());
                map.put("code", bean.getTwoLetterIsoCode());
                dropdownDataList.add(map);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initNaviView();
        setCountryDisplayData();

        // 声明一个ArrayAdapter用于存放简单数据
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.item_spinner_text,
//                displayDataList);
        //adapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
        //下拉列表数据的适配器
        SimpleAdapter simpleAdapter=new SimpleAdapter(this,dropdownDataList,R.layout.item_spinner_dropdown,
                new String[]{"name","code"},new int[]{R.id.ctv_country,R.id.tv_countrycode});

        sp_country.setAdapter(simpleAdapter);
    }

    @OnClick(R.id.btn_register)
    public void onRegisterClick(View view){
        String firstname=et_fname.getText().toString();
        String lastname=et_lname.getText().toString();
        String mobile=et_mobile.getText().toString();
        String email=et_email.getText().toString();
        String address=et_address.getText().toString();
        String password=et_password.getText().toString();
        String passwordagain=et_password_again.getText().toString();
        Map<String,String> selectedItem=(Map<String,String>)sp_country.getSelectedItem();
        String countryCode=selectedItem.get("code");
        if (TextUtils.isEmpty(firstname)) {
            toast(getResources().getString(ResourcesUtils.getStringId(this.getApplicationContext(),"E_2012")));
            return;
        }
        if (TextUtils.isEmpty(lastname)) {
            toast(getResources().getString(ResourcesUtils.getStringId(this.getApplicationContext(),"E_2013")));
            return;
        }

        if (TextUtils.isEmpty(password)) {
            toast(getResources().getString(ResourcesUtils.getStringId(this.getApplicationContext(),"E_2007")));
            return;
        }
        if (TextUtils.isEmpty(passwordagain)) {
            toast(getResources().getString(ResourcesUtils.getStringId(this.getApplicationContext(),"E_2008")));
            return;
        }
        if (!password.equals(passwordagain)) {
            toast(getResources().getString(ResourcesUtils.getStringId(this.getApplicationContext(),"E_2009")));
            return;
        }
        if (TextUtils.isEmpty(mobile)) {
            toast(getResources().getString(ResourcesUtils.getStringId(this.getApplicationContext(),"E_2014")));
            return;
        }
        if (TextUtils.isEmpty(email)) {
            toast(getResources().getString(ResourcesUtils.getStringId(this.getApplicationContext(),"E_2006")));
            return;
        }

        //远程注册用户并登录
        UserModel.getInstance().register(mobile,email, password,firstname,lastname,address,countryCode,new OperationListener<User>() {
            @Override
            public void done(User user, AppException exe) {
                if (exe == null) {
                    startActivity(MainActivity.class, null, true);
                } else {
                    toast(exe.toString(getApplicationContext()));
                }
            }
        });
    }



}
