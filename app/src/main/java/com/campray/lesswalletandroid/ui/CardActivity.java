package com.campray.lesswalletandroid.ui;


import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.campray.lesswalletandroid.LessWalletApplication;
import com.campray.lesswalletandroid.R;
import com.campray.lesswalletandroid.db.entity.Coupon;
import com.campray.lesswalletandroid.db.entity.User;
import com.campray.lesswalletandroid.event.RefreshEvent;
import com.campray.lesswalletandroid.listener.OperationListener;
import com.campray.lesswalletandroid.model.CouponModel;
import com.campray.lesswalletandroid.model.UserModel;
import com.campray.lesswalletandroid.qrcode.encode.QRCodeEncoder;
import com.campray.lesswalletandroid.ui.base.MenuActivity;
import com.campray.lesswalletandroid.util.AppException;
import com.campray.lesswalletandroid.view.RoundImageView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 电子卡的页面Activity处理类
 * Created by Phills on 11/2/2017.
 */

public class CardActivity extends MenuActivity {
    @BindView(R.id.rl_card_top)
    RelativeLayout rl_card_top;
    @BindView(R.id.iv_card_shading)
    RoundImageView iv_card_shading;
    @BindView(R.id.iv_card_img)
    RoundImageView iv_card_img;
    @BindView(R.id.iv_card_logo)
    ImageView iv_card_logo;
    @BindView(R.id.tv_benefit)
    TextView tv_benefit;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_number)
    TextView tv_number;
    @BindView(R.id.tv_merchant)
    TextView tv_merchant;
    @BindView(R.id.tv_points)
    TextView tv_points;

    @BindView(R.id.iv_qrcode)
    ImageView iv_qrcode;


    private long couponId=0;
    private int typeId=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
        typeId=this.getBundle().getInt("type_id");
        showCard(typeId);

    }
    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * 显示会员卡内容
     */
    private void showCard(int typeId){
        List<Coupon> list=CouponModel.getInstance().getAllCouponByType(typeId);
        int index=0;
        int maxLevel=0;
        //找出最高级别的会员卡
        for (int i=0;i<list.size();i++) {
            Coupon card=list.get(i);
            if (card.getCouponStyle() != null) {
                int level=card.getCouponStyle().getCardLevel();
                if(level>maxLevel){
                    index=i;
                    maxLevel=level;
                }
            }
        }
        if(list!=null&&list.size()>=1){
            Coupon card=list.get(index);
            couponId=card.getOrderId();
            tv_title.setText(card.getProduct().getTitle());
            tv_number.setText(card.getCid());
            tv_merchant.setText(card.getProduct().getMerchant().getName());
            tv_benefit.setText("");
            User user=LessWalletApplication.INSTANCE().getAccount();
            tv_points.setText(user.getPoints()+"");
            if (card.getCouponStyle() != null) {
                if (!TextUtils.isEmpty(card.getCouponStyle().getBgColor())) {
                    int bgColor= Color.parseColor(card.getCouponStyle().getBgColor());
                    GradientDrawable drawable = (GradientDrawable) rl_card_top.getBackground();
                    drawable.setColor(bgColor);
                }
                if (!TextUtils.isEmpty(card.getCouponStyle().getShadingUrl())) {
                    Picasso.with(this).load(card.getCouponStyle().getShadingUrl()).memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE).into(iv_card_shading);
                }
                if (!TextUtils.isEmpty(card.getCouponStyle().getPictureUrl())) {
                    //iv_card_img.setImageBitmap(BitmapFactory.decodeFile(card.getCouponStyle().getPictureUrl()));
                    Picasso.with(this).load(card.getCouponStyle().getPictureUrl()).memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE).into(iv_card_img);
                }
                if (!TextUtils.isEmpty(card.getCouponStyle().getLogoUrl())) {
                    Picasso.with(this).load(card.getCouponStyle().getLogoUrl()).memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE).into(iv_card_logo);
                }
            }

            try{
                String couponStr="card:"+card.getOrderId();
                Bitmap qrCodeBitmap= QRCodeEncoder.encodeAsBitmap(Base64.encodeToString(couponStr.getBytes("UTF-8"),Base64.DEFAULT), BarcodeFormat.QR_CODE,200);
                iv_qrcode.setImageBitmap(qrCodeBitmap);
            } catch (Exception e) {
                toast("Failed to load QR Code.");
            }
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
    }

    /**
     * 点击Card的事件方法
     * @param view
     */
    @OnClick(R.id.ll_card_layout)
    public void onCardClick(View view){
        Bundle bundle=new Bundle();
        bundle.putLong("coupon_id", couponId);
        startActivity(CardUseActivity.class,bundle,false);
    }

}
