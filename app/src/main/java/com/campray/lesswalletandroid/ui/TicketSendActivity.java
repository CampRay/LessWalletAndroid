package com.campray.lesswalletandroid.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.campray.lesswalletandroid.R;
import com.campray.lesswalletandroid.db.entity.Coupon;
import com.campray.lesswalletandroid.db.entity.CouponStyle;
import com.campray.lesswalletandroid.db.entity.Friend;
import com.campray.lesswalletandroid.db.entity.UserAttr;
import com.campray.lesswalletandroid.db.entity.UserAttrValue;
import com.campray.lesswalletandroid.db.entity.UserValue;
import com.campray.lesswalletandroid.db.service.FriendDaoService;
import com.campray.lesswalletandroid.listener.OperationListener;
import com.campray.lesswalletandroid.model.CouponModel;
import com.campray.lesswalletandroid.model.CurrencyModel;
import com.campray.lesswalletandroid.model.ProductModel;
import com.campray.lesswalletandroid.ui.base.MenuActivity;
import com.campray.lesswalletandroid.util.AppException;
import com.campray.lesswalletandroid.util.ResourcesUtils;
import com.campray.lesswalletandroid.view.InnerCornerView;
import com.google.gson.JsonObject;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Phills on 11/2/2017.
 */

public class TicketSendActivity extends MenuActivity {
    @BindView(R.id.iv_left)
    ImageView iv_left;
    @BindView(R.id.tv_navi_title)
    TextView tv_navi_title;

    //Ticket控件
    @BindView(R.id.iv_ticket_img)
    ImageView iv_ticket_img;
    @BindView(R.id.ll_top_layout)
    LinearLayout ll_top_layout;
    @BindView(R.id.icv_tleft)
    InnerCornerView icv_tleft;
    @BindView(R.id.icv_tright)
    InnerCornerView icv_tright;
    @BindView(R.id.tv_top_bg)
    TextView tv_top_bg;
    @BindView(R.id.ll_form)
    LinearLayout ll_form;

    @BindView(R.id.tv_uservalue)
    TextView tv_uservalue;

    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_merchant)
    TextView tv_merchant;
    @BindView(R.id.tv_shortdesc)
    TextView tv_shortdesc;
    @BindView(R.id.tv_expired)
    TextView tv_expired;
    @BindView(R.id.tv_number)
    TextView tv_number;
    @BindView(R.id.tv_price)
    TextView tv_price;
    @BindView(R.id.sp_friend)
    Spinner sp_friend;
    //要送出的卡卷ID
    private long couponId;
    //好友下拉列表数据集合
    private List<Map<String, Object>> dropdownDataList=new ArrayList<Map<String, Object>>();
    //设置好友下拉列表数据源
    private void setFriendsDisplayData() {
        List<Friend> friendList = FriendDaoService.getInstance(this).getAllFriends();
        if(friendList!=null) {
            for (Friend friend : friendList) {
                Map<String, Object> listItem = new HashMap<String, Object>();
                listItem.put("avator", TextUtils.isEmpty(friend.getAvatorPath()) ? R.mipmap.icon_account : friend.getAvatorPath());
                listItem.put("fullname", friend.getFullName());
                listItem.put("id", friend.getId());
                dropdownDataList.add(listItem);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_send);
        tv_navi_title.setText("FRIEND LISTS");
        couponId=this.getBundle().getLong("coupon_id");
        loadData(couponId);
        setFriendsDisplayData();
        SimpleAdapter simpleAdapter=new SimpleAdapter(this,dropdownDataList,R.layout.item_spinner_friend,new String[]{"avator","fullname","id"},new int[]{R.id.iv_avatar,R.id.tv_name,R.id.tv_id});
        simpleAdapter.setDropDownViewResource(R.layout.item_spinner_friend);
        sp_friend.setAdapter(simpleAdapter);
        sp_friend.requestFocus();
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
     * 点击tv_give送出按钮的事件方法
     * @param view
     */
    @OnClick(R.id.tv_give)
    public void onGiveClick(View view){
        Map<String,Object> selectedItem=(Map<String,Object>)sp_friend.getSelectedItem();
        long friendId=(long)selectedItem.get("id");
        //调用服务接口送出coupon给好友
        CouponModel.getInstance().sentCouponsToFriend(couponId,friendId,new OperationListener<Coupon>() {
            @Override
            public void done(Coupon coupon, AppException exe) {
                if (exe == null) {
                    toast(getResources().getString(ResourcesUtils.getStringId(getApplicationContext(),"coupon_sent_success")));
                    startActivity(MainActivity.class, null, true);
                } else {
                    toast(exe.toString(getApplicationContext()));
                }
            }
        });
    }

    private void loadData(long couponId){
        Coupon coupon=CouponModel.getInstance().getCouponById(couponId);
        CouponStyle couponStyle=coupon.getCouponStyle();
        if (couponStyle != null) {
            //设置背景色
            int topColor=Color.parseColor(couponStyle.getBgColor());
            try {
                LayerDrawable drawable = (LayerDrawable) ll_top_layout.getBackground();
                GradientDrawable item = (GradientDrawable)drawable.findDrawableByLayerId(R.id.top_item);
                item.setColor(topColor);
                icv_tleft.setCornerColor(topColor);
                icv_tright.setCornerColor(topColor);
                tv_top_bg.setBackgroundColor(topColor);
            }
            catch (Exception e){
                e.printStackTrace();
            }

            if (!TextUtils.isEmpty(couponStyle.getPictureUrl())) {
                //Picasso.with(this).load(couponStyle.getPictureUrl()).memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE).into(iv_ticket_img);
                Picasso.get().load(couponStyle.getPictureUrl()).memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE).into(iv_ticket_img);
            }
            else if (!TextUtils.isEmpty(couponStyle.getLogoUrl())){
//                Picasso.with(this).load(couponStyle.getLogoUrl()).memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE).into(iv_ticket_img);
                Picasso.get().load(couponStyle.getLogoUrl()).memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE).into(iv_ticket_img);
            }
        }

        if(coupon.getProduct().getPrice()>0){
            tv_price.setText(coupon.getProduct().getPriceStr());
        }
        else{
            tv_price.setText(getResources().getString(R.string.coupon_free));
        }

        tv_title.setText(coupon.getProduct().getTitle());
        tv_merchant.setText(coupon.getProduct().getMerchant().getName());
        tv_shortdesc.setText(coupon.getProduct().getShortDesc());
        String expired = (TextUtils.isEmpty(coupon.getStartTimeLocal()) ? "" : coupon.getStartTimeLocal()) + (TextUtils.isEmpty(coupon.getEndTimeLocal()) ? " -" : " - " + coupon.getEndTimeLocal());
        tv_expired.setText(expired);
        tv_number.setText(coupon.getCid());
        tv_uservalue.setText(Html.fromHtml(coupon.getUserValuesInfo()));
    }

}
