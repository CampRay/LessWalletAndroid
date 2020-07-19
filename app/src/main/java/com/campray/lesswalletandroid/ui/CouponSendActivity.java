package com.campray.lesswalletandroid.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.campray.lesswalletandroid.R;
import com.campray.lesswalletandroid.db.entity.Coupon;
import com.campray.lesswalletandroid.db.entity.CouponStyle;
import com.campray.lesswalletandroid.db.entity.Friend;
import com.campray.lesswalletandroid.db.service.FriendDaoService;
import com.campray.lesswalletandroid.listener.OperationListener;
import com.campray.lesswalletandroid.model.CouponModel;
import com.campray.lesswalletandroid.ui.base.MenuActivity;
import com.campray.lesswalletandroid.util.AppException;
import com.campray.lesswalletandroid.util.ResourcesUtils;
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

public class CouponSendActivity extends MenuActivity {
    @BindView(R.id.iv_left)
    ImageView iv_left;
    @BindView(R.id.tv_navi_title)
    TextView tv_navi_title;
    @BindView(R.id.gl_coupon_top)
    GridLayout gl_coupon_top;
    @BindView(R.id.iv_coupon_shading)
    ImageView iv_coupon_shading;
    @BindView(R.id.iv_coupon_logo)
    ImageView iv_coupon_logo;
    @BindView(R.id.iv_coupon_img)
    ImageView iv_coupon_img;
    @BindView(R.id.tv_validity)
    TextView tv_validity;
    @BindView(R.id.tv_benefit)
    TextView tv_benefit;
    @BindView(R.id.tv_benefit_value)
    TextView tv_benefit_value;
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
        for (Friend friend:friendList){
            Map<String, Object> listItem = new HashMap<String, Object>();
            listItem.put("avator", TextUtils.isEmpty(friend.getAvatorPath())?R.mipmap.icon_account:friend.getAvatorPath());
            listItem.put("fullname", friend.getFullName());
            listItem.put("id", friend.getId());
            dropdownDataList.add(listItem);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_send);
        tv_navi_title.setText("FRIEND LISTS");
        couponId=this.getBundle().getLong("coupon_id");
        loadData(couponId);
        setFriendsDisplayData();
        SimpleAdapter simpleAdapter=new SimpleAdapter(this,dropdownDataList,R.layout.item_spinner_friend,new String[]{"avator","fullname","id"},new int[]{R.id.iv_avatar,R.id.tv_name,R.id.tv_id});
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
            if(couponStyle.getValidityDay()>0) {
                tv_validity.setText(couponStyle.getValidityDay()+getResources().getString(R.string.coupon_validity_day) );
            }else if(couponStyle.getValidityMonth()>0) {
                tv_validity.setText(couponStyle.getValidityMonth()+getResources().getString(R.string.coupon_validity_month) );
            }else if(couponStyle.getValidityYear()>0) {
                tv_validity.setText(couponStyle.getValidityYear()+getResources().getString(R.string.coupon_validity_year) );
            }
            else{
                tv_validity.setText(getResources().getString(R.string.coupon_validity_nolimit) );
            }

            if (!TextUtils.isEmpty(couponStyle.getBenefitFree())) {
                tv_benefit.setText( R.string.coupon_benefit_free);
                tv_benefit_value.setText(couponStyle.getBenefitFree());
            } else if (!TextUtils.isEmpty(couponStyle.getBenefitCash())) {
                tv_benefit.setText( R.string.coupon_benefit_precash);
                tv_benefit_value.setText( couponStyle.getBenefitCash());
            } else if (!TextUtils.isEmpty(couponStyle.getBenefitDiscount())) {
                tv_benefit.setText( R.string.coupon_benefit_discount);
                tv_benefit_value.setText(couponStyle.getBenefitDiscount());
            } else if (!TextUtils.isEmpty(couponStyle.getBenefitCustomized())) {
                tv_benefit.setText( R.string.coupon_benefit_customized);
                tv_benefit_value.setText(couponStyle.getBenefitCustomized());
            }

            gl_coupon_top.setBackgroundColor(Color.parseColor(couponStyle.getBgColor()));
            if (!TextUtils.isEmpty(couponStyle.getShadingUrl())) {
//                Picasso.with(this).load(couponStyle.getShadingUrl()).memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE).into(iv_coupon_shading);
                Picasso.get().load(couponStyle.getShadingUrl()).memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE).into(iv_coupon_shading);
            } else {
                iv_coupon_shading.setVisibility(View.GONE);
            }
            if (!TextUtils.isEmpty(couponStyle.getPictureUrl())) {
//                Picasso.with(this).load(couponStyle.getPictureUrl()).memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE).into(iv_coupon_img);
                Picasso.get().load(couponStyle.getPictureUrl()).memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE).into(iv_coupon_img);
            } else {
                iv_coupon_img.setVisibility(View.GONE);
            }
            if (!TextUtils.isEmpty(couponStyle.getLogoUrl())) {
//                Picasso.with(this).load(couponStyle.getLogoUrl()).memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE).into(iv_coupon_logo);
                Picasso.get().load(couponStyle.getLogoUrl()).memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE).into(iv_coupon_logo);
            } else {
                iv_coupon_logo.setVisibility(View.GONE);
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

    }

}
