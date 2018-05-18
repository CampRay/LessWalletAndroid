package com.campray.lesswalletandroid.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.campray.lesswalletandroid.LessWalletApplication;
import com.campray.lesswalletandroid.R;
import com.campray.lesswalletandroid.adapter.FriendAdapter;
import com.campray.lesswalletandroid.db.entity.Country;
import com.campray.lesswalletandroid.db.entity.Friend;
import com.campray.lesswalletandroid.db.entity.User;
import com.campray.lesswalletandroid.db.service.CountryDaoService;
import com.campray.lesswalletandroid.db.service.FriendDaoService;
import com.campray.lesswalletandroid.listener.OperationListener;
import com.campray.lesswalletandroid.model.FriendModel;
import com.campray.lesswalletandroid.model.UserModel;
import com.campray.lesswalletandroid.ui.base.MenuActivity;
import com.campray.lesswalletandroid.util.AppException;
import com.campray.lesswalletandroid.util.ResourcesUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnItemClick;

/**
 * 好友列表页
 * Created by Phills on 4/2/2018.
 */

public class FriendsActivity extends MenuActivity {
    @BindView(R.id.tv_navi_title)
    TextView tv_navi_title;
    @BindView(R.id.tv_right)
    TextView tv_right;
    @BindView(R.id.rl_friends)
    RelativeLayout rl_friends;
    @BindView(R.id.et_username)
    EditText et_username;
    @BindView(R.id.btn_search)
    Button btn_search;

    @BindView(R.id.lv_friends)
    ListView lv_friends;
    //显示的好友对象集合
    private List<Friend> friendList=new ArrayList<Friend>();
    //搜索到的用户对象集合
    private List<Friend> searchList=new ArrayList<Friend>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        tv_navi_title.setText(getResources().getString(ResourcesUtils.getStringId(getApplicationContext(),"friend_list")));
        tv_right.setText("+");
        //显示好友数据列表
        showFriendsList();
    }

    /**
     * 显示好友列表
     */
    private void showFriendsList(){
        LessWalletApplication app=LessWalletApplication.INSTANCE();
        if(app!=null) {
            //获取当前登录用户
            User user=app.getAccount();
            if(user!=null){
                //创建一个list集合，list集合元素是Map
                List<Map<String, Object>> listItems = new ArrayList<Map<String,Object>>();
                friendList = FriendDaoService.getInstance(this).getAllFriends();
                for (Friend friend:friendList){
                    Map<String, Object> listItem = new HashMap<String, Object>();
                    listItem.put("avator", TextUtils.isEmpty(friend.getAvatorPath())?R.mipmap.icon_account:friend.getAvatorPath());
                    listItem.put("fullname", friend.getFullName());
                    listItem.put("email", friend.getEmail());
                    listItem.put("mobile", friend.getMobile());
                    listItems.add(listItem);
                }

                SimpleAdapter simpleAdapter=new SimpleAdapter(this,listItems,R.layout.item_friend,new String[]{"avator","fullname","email","mobile"},new int[]{R.id.iv_avatar,R.id.tv_name,R.id.tv_email,R.id.tv_mobile});
                //FriendAdapter friendAdapter = new FriendAdapter(FriendsActivity.this, R.layout.item_friend, friendList);
                lv_friends.setAdapter(simpleAdapter);
            }
            else{//没有登录则打开登录页面
                startActivity(LoginActivity.class,null,true);
            }
        }
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
     * 点击tv_right按钮的弹出菜单的事件方法
     * @param view
     */
    @OnClick(R.id.tv_right)
    public void onAddClick(View view){
        et_username.setText("");
        rl_friends.setVisibility(View.VISIBLE);
//        //定义锚定到tv_right按钮的弹出式菜单(如果点击更多后想使用弹出式菜单模式)
//        PopupMenu popupMenu = new PopupMenu(this, view);
//        MenuInflater menuInflater = popupMenu.getMenuInflater();
//        //使用自定义的popup_menu_friend_add菜单
//        menuInflater.inflate(R.menu.popup_menu_friend_add, popupMenu.getMenu());
//        popupMenu.show();
//        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                switch (item.getItemId()) {
//                    case R.id.by_name:
//                        return true;
//                    case R.id.by_qrcode:
//                        return true;
//                    default:
//                        return false;
//                }
//            }
//        });


    }

    /**
     * 点击btn_search按钮的事件方法
     * @param view
     */
    @OnClick(R.id.btn_search)
    public void onSearchClick(View view){
        String searchTxt=et_username.getText().toString();
        if(TextUtils.isEmpty(searchTxt)){
            this.toast(getResources().getString(ResourcesUtils.getStringId(this.getApplicationContext(),"friends_search_error1")));
        }
        else{
            hideSoftInputView();
            Friend friend=FriendModel.getInstance().searchFriend(searchTxt);
            if(friend==null) {
                //根据用户名或手机号查找用户
                FriendModel.getInstance().searchUser(searchTxt, new OperationListener<Friend>() {
                    @Override
                    public void done(Friend friend, AppException exe) {
                        if (exe == null) {
                            Bundle bundle = new Bundle();
                            bundle.putString("userName", friend.getUserName());
                            bundle.putString("mobile", friend.getMobile());
                            bundle.putString("email", friend.getEmail());
                            bundle.putString("firstName", friend.getFirstName());
                            bundle.putString("lastName", friend.getLastName());
                            bundle.putString("avatarUrl", friend.getAvatarUrl());
                            Country country = CountryDaoService.getInstance(getApplicationContext()).getCountry(friend.getCountryId());
                            bundle.putString("country", country.getName());
                            startActivity(FriendActivity.class, bundle, true);
                        } else {
                            toast(exe.toString(getApplicationContext()));
                        }
                    }
                });
            }
            else{
                Bundle bundle = new Bundle();
                bundle.putLong("id", friend.getId());
                bundle.putString("userName", friend.getUserName());
                bundle.putString("mobile", friend.getMobile());
                bundle.putString("email", friend.getEmail());
                bundle.putString("firstName", friend.getFirstName());
                bundle.putString("lastName", friend.getLastName());
                bundle.putString("avatarUrl", friend.getAvatarUrl());
                Country country = CountryDaoService.getInstance(getApplicationContext()).getCountry(friend.getCountryId());
                bundle.putString("country", country.getName());
                startActivity(FriendActivity.class, bundle, true);
            }
            rl_friends.setVisibility(View.GONE);
        }
    }

    /**
     * 点击btn_search按钮的事件方法
     * @param view
     */
    @OnClick(R.id.rl_friends)
    public void onCancelClick(View view){
        hideSoftInputView();
        rl_friends.setVisibility(View.GONE);
    }


    /**
     * 点击好友列表中的某一个对象
     * @param position
     */
    @OnItemClick(R.id.lv_friends)
    public void onFriendClick(int position){
        toast(friendList.get(position).getUserName());
    }

}
