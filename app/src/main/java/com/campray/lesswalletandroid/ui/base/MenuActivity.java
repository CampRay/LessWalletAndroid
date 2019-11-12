package com.campray.lesswalletandroid.ui.base;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.campray.lesswalletandroid.LessWalletApplication;
import com.campray.lesswalletandroid.R;
import com.campray.lesswalletandroid.db.entity.Country;
import com.campray.lesswalletandroid.db.entity.Friend;
import com.campray.lesswalletandroid.db.entity.History;
import com.campray.lesswalletandroid.db.entity.User;
import com.campray.lesswalletandroid.db.service.CountryDaoService;
import com.campray.lesswalletandroid.event.MessageEvent;
import com.campray.lesswalletandroid.listener.OperationListener;
import com.campray.lesswalletandroid.model.FriendModel;
import com.campray.lesswalletandroid.model.HistoryModel;
import com.campray.lesswalletandroid.model.UserModel;
import com.campray.lesswalletandroid.ui.AccountActivity;
import com.campray.lesswalletandroid.ui.CardPaymentActivity;
import com.campray.lesswalletandroid.ui.CollectPaymentActivity;
import com.campray.lesswalletandroid.ui.CouponPaymentActivity;
import com.campray.lesswalletandroid.ui.FriendActivity;
import com.campray.lesswalletandroid.ui.FriendsActivity;
import com.campray.lesswalletandroid.ui.MainActivity;
import com.campray.lesswalletandroid.ui.MessageActivity;
import com.campray.lesswalletandroid.ui.QRCodeCaptureActivity;
import com.campray.lesswalletandroid.ui.TicketPaymentActivity;
import com.campray.lesswalletandroid.util.AppException;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 有相同头部菜单页面的基类
 * @author :Phills
 * @project:BaseActivity
 * @date :2017-08-15 18:23
 */
public class MenuActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {
    private final static int TAKE_ALL_REQUEST_CODE=100;
    private final static int TAKE_CAMERA_REQUEST_CODE=200;

    private final static int SCANNIN_REQUEST_CODE = 111;
    private final static int SCANNIN_REQUEST_IMAGE = 112;

    @BindView(R.id.tv_username)
    TextView tv_username;
    @BindView(R.id.tv_point)
    TextView tv_point;


    /**
     * 点击扫描Home的事件方法
     * @param view
     */
    @OnClick(R.id.ib_home)
    public void onHomeClick(View view){
        startActivity(MainActivity.class,null,true);
    }

    /**
     * 点击扫描Home的事件方法
     * @param view
     */
    @OnClick(R.id.ib_my)
    public void onMyClick(View view){
        startActivity(AccountActivity.class,null,false);
    }

    /**
     * 点击扫描按钮的事件方法
     * @param view
     */
    @OnClick(R.id.ib_scan)
    public void onScanClick(View view){
        if (EasyPermissions.hasPermissions(this, Manifest.permission.CAMERA)) {
            //如果有权限,打开扫码页面
            openQRcodeScanActivity();
        } else {
            EasyPermissions.requestPermissions(this, "扫码需要请求摄像头权限",
                    TAKE_CAMERA_REQUEST_CODE, Manifest.permission.CAMERA);
        }
    }

    /**
     * 点击配置按钮的事件方法
     * @param view
     */
    @OnClick(R.id.ib_friends)
    public void onCollectClick(View view){
        startActivity(FriendsActivity.class,null,false);
    }

    /**
     * 点击通知消息按钮的事件方法
     * @param view
     */
    @OnClick(R.id.ib_received)
    public void onReceivedClick(View view){

        List<History> list=HistoryModel.getInstance().getUnread();
        if(list!=null&&list.size()>0) {
            for (History item : list) {
                item.setReaded(true);
                HistoryModel.getInstance().updateHistory(item);
            }
            EventBus.getDefault().post(new MessageEvent());
        }
        startActivity(MessageActivity.class,null,false);
    }

    /**
     * 打开扫码页面
     */
    private void openQRcodeScanActivity(){
        Intent intent = new Intent(getApplication(), QRCodeCaptureActivity.class);
        startActivityForResult(intent, SCANNIN_REQUEST_CODE);
//        this.startActivity(QRCodeCaptureActivity.class,null,false);
    }

    /**
     * 上面的startActivityForResult方法调用后的返回结果处理方法
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            //处理扫码结果
            case SCANNIN_REQUEST_CODE:
                if (null != data) {
                    Bundle bundle = data.getExtras();
                    if (bundle == null) {
                        return;
                    }
                    //解析扫码结果数据(二维码数据结构是：product+":"+商品类型ID+":"+商口ID)
                    String result = bundle.getString("result");
                    try {
                        //String text=EncryptionUtil.desECBDecrypt(result,"lesswalletCampRay@201404");
                        String text=new String(Base64.decode(result,Base64.DEFAULT),"UTF-8");
                        //Toast.makeText(this, "解析结果:" + text, Toast.LENGTH_LONG).show();
                        String[] strArr=text.split(":");
                        if(strArr.length>0) {
                            String model=strArr[0];
                            if ("product".equals(model)) {
                                try {
                                    int tid = Integer.parseInt(strArr[1]);
                                    int pid = Integer.parseInt(strArr[2]);
                                    bundle.clear();
                                    bundle.putLong("pid", pid);
                                    bundle.putLong("tid", tid);
                                    switch (tid) {
                                        case 1://优惠卷
                                            this.startActivity(CouponPaymentActivity.class,bundle,false);
                                            break;
                                        case 2://电子票
                                            this.startActivity(TicketPaymentActivity.class,bundle,false);
                                            break;
                                        case 3://会员卡
                                            this.startActivity(CardPaymentActivity.class,bundle,false);
                                            break;
                                    }
                                }
                                catch (Exception e){}
                            }
                            else if("collect".equals(model)){
                                long uid = Long.parseLong(strArr[1]);
                                String currencyCode = strArr[2];
                                String amount = strArr[3];
                                bundle.clear();
                                bundle.putLong("uid", uid);
                                if(!TextUtils.isEmpty(currencyCode)&&!TextUtils.isEmpty(amount)){
                                    bundle.putString("currency", currencyCode);
                                    bundle.putString("amount", amount);
                                }
                                this.startActivity(CollectPaymentActivity.class,bundle,true);
                            }
                            else if("user".equals(model)){
                                long uid = Long.parseLong(strArr[1]);
                                Friend friend=FriendModel.getInstance().getFriendById(uid);
                                if(friend==null) {
                                    UserModel.getInstance().searchUserById(uid, new OperationListener<User>() {
                                        @Override
                                        public void done(User obj, AppException exception) {
                                            if (obj != null) {
                                                Bundle bund = new Bundle();
                                                bund.putString("userName", obj.getUserName());
                                                bund.putString("mobile", obj.getMobile());
                                                bund.putString("email", obj.getEmail());
                                                bund.putString("firstName", obj.getFirstName());
                                                bund.putString("lastName", obj.getLastName());
                                                bund.putString("avatarUrl", obj.getAvatarUrl());
                                                Country country = CountryDaoService.getInstance(getApplicationContext()).getCountry(obj.getCountryId());
                                                bund.putString("country", country.getName());
                                                startActivity(FriendActivity.class, bund, true);
                                            }
                                        }
                                    });
                                }
                                else{
                                    Bundle bund = new Bundle();
                                    bund.putLong("id", friend.getId());
                                    bund.putString("userName", friend.getUserName());
                                    bund.putString("mobile", friend.getMobile());
                                    bund.putString("email", friend.getEmail());
                                    bund.putString("firstName", friend.getFirstName());
                                    bund.putString("lastName", friend.getLastName());
                                    bund.putString("avatarUrl", friend.getAvatarUrl());
                                    Country country = CountryDaoService.getInstance(getApplicationContext()).getCountry(friend.getCountryId());
                                    bund.putString("country", country.getName());
                                    startActivity(FriendActivity.class, bund, true);
                                }

                            }
                        }
                    } catch (Exception e) {
                        Toast.makeText(this, "无效的二维码:" + result, Toast.LENGTH_LONG).show();
                    }

                }
                break;
            case SCANNIN_REQUEST_IMAGE://选择系统图片并解析
                break;
            default:
        }
    }

    /**初始化主面视图
     */
    @Override
    protected void initView() {
        User user= LessWalletApplication.INSTANCE().getAccount();
        tv_username.setText(user.getUserName());
    }

    /**
     * 初始化权限事件
     */
    protected void initPermissions(){
        String[] perms = {Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE};
        if (!EasyPermissions.hasPermissions(this, perms)) {
            //没有权限则向用户申请权限
            EasyPermissions.requestPermissions(this, "扫码需要摄像头权限",
                    TAKE_ALL_REQUEST_CODE, perms);
        }
    }

    /**
     * 向用户询问申请权限结果处理
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    /**
     * 授权成功
     * @param requestCode
     * @param perms
     */
    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if(TAKE_CAMERA_REQUEST_CODE==requestCode){
            //打开扫码页面
            openQRcodeScanActivity();
        }
        else if(TAKE_ALL_REQUEST_CODE==requestCode){
            Toast.makeText(this, "Permissions granted successfully", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 拒绝授权
     * @param requestCode
     * @param perms
     */
    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if(TAKE_CAMERA_REQUEST_CODE==requestCode){
            new AppSettingsDialog.Builder(this)
                    .setTitle(getString(R.string.dialog_permission_title))
                    .setRationale(getString(R.string.dialog_camera_permission))
                    .setPositiveButton(getString(R.string.button_ok))
                    .setNegativeButton(getString(R.string.button_cancel))
                    .setRequestCode(TAKE_CAMERA_REQUEST_CODE)
                    .build()
                    .show();
        }
        else if(TAKE_ALL_REQUEST_CODE==requestCode){
            if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
                Toast.makeText(this, "您拒绝授予指定的权限，某些功能将不能正常工作", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    /**
     * 注册消息接收事件，接收EventBus通過EventBus.getDefault().post(T)方法发出的事件通知，onMessageEvent(T)方法就会自动接收并处理些事件通知
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event){
        checkRedPoint();
    }

    private void checkRedPoint(){
        int count = HistoryModel.getInstance().getUnreadCount();
        if(count>0){
            tv_point.setText(count+"");
            tv_point.setVisibility(View.VISIBLE);
        }else{
            tv_point.setVisibility(View.INVISIBLE);
        }
    }
}
