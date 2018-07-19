package com.campray.lesswalletandroid.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.campray.lesswalletandroid.LessWalletApplication;
import com.campray.lesswalletandroid.R;
import com.campray.lesswalletandroid.db.entity.Coupon;
import com.campray.lesswalletandroid.db.entity.CouponStyle;
import com.campray.lesswalletandroid.model.CouponModel;
import com.campray.lesswalletandroid.qrcode.encode.QRCodeEncoder;
import com.campray.lesswalletandroid.ui.base.MenuActivity;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Phills on 6/2/2018.
 */

public class CardUseActivity extends MenuActivity {
    @BindView(R.id.iv_left)
    ImageView iv_left;
    @BindView(R.id.tv_navi_title)
    TextView tv_navi_title;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_merchant)
    TextView tv_merchant;
    @BindView(R.id.tv_validity)
    TextView tv_validity;
    @BindView(R.id.tv_benefit)
    TextView tv_benefit;
    @BindView(R.id.tv_benefit_value)
    TextView tv_benefit_value;

    @BindView(R.id.tv_shortdesc)
    TextView tv_shortdesc;
    @BindView(R.id.tv_desc)
    TextView tv_desc;
    @BindView(R.id.tv_expired)
    TextView tv_expired;
    @BindView(R.id.tv_price)
    TextView tv_price;

    @BindView(R.id.iv_qrcode)
    ImageView iv_qrcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_use);
        tv_navi_title.setText("Membership Card");
        long couponId=this.getBundle().getLong("coupon_id");
        loadData(couponId);
    }

    /**
     * 点击iv_left按钮的事件方法
     * @param view
     */
    @OnClick(R.id.iv_left)
    public void onBackClick(View view){
        this.finish();
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

            if (!TextUtils.isEmpty(couponStyle.getBenefitOne())) {
                tv_benefit.setText(R.string.coupon_benefit_onetime);
                tv_benefit_value.setText(couponStyle.getBenefitOne());
            } else if (!TextUtils.isEmpty(couponStyle.getBenefitPrepaidCash())) {
                tv_benefit.setText(R.string.coupon_benefit_precash);
                tv_benefit_value.setText(couponStyle.getBenefitPrepaidCash()+"/"+coupon.getCustomValues().get("2"));
            } else if (!TextUtils.isEmpty(couponStyle.getBenefitPrepaidService())) {
                tv_benefit.setText(R.string.coupon_benefit_preservice);
                tv_benefit_value.setText(couponStyle.getBenefitPrepaidService()+"/"+coupon.getCustomValues().get("3"));
            } else if (!TextUtils.isEmpty(couponStyle.getBenefitBuyNGetOne())) {
                tv_benefit.setText(R.string.coupon_benefit_buyngetone);
                tv_benefit_value.setText(couponStyle.getBenefitBuyNGetOne()+"/"+coupon.getCustomValues().get("4"));
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
        String expired = (TextUtils.isEmpty(coupon.getStartTimeLocal()) ? "" : coupon.getStartTimeLocal()) + (TextUtils.isEmpty(coupon.getEndTimeLocal()) ? "" : " - " + coupon.getEndTimeLocal());
        tv_expired.setText(expired);

        //显示html文本（只有html样式,不带图片）
        if(!TextUtils.isEmpty(coupon.getProduct().getAgreement())) {
            tv_desc.setText(Html.fromHtml(coupon.getProduct().getFullDesc()));
        }
        try {
            String couponStr="card:"+coupon.getOrderId();
            Bitmap qrCodeBitmap=QRCodeEncoder.encodeAsBitmap(Base64.encodeToString(couponStr.getBytes("UTF-8"),Base64.DEFAULT), BarcodeFormat.QR_CODE,200);
            iv_qrcode.setImageBitmap(qrCodeBitmap);
        } catch (Exception e) {
            toast("Failed to load QR Code.");
        }

    }

}
