package com.campray.lesswalletandroid.ui;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.campray.lesswalletandroid.R;
import com.campray.lesswalletandroid.db.entity.Coupon;
import com.campray.lesswalletandroid.model.CouponModel;
import com.campray.lesswalletandroid.qrcode.encode.QRCodeEncoder;
import com.campray.lesswalletandroid.ui.base.MenuActivity;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

import java.net.URL;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Phills on 11/2/2017.
 */

public class CouponUseActivity extends MenuActivity {
    @BindView(R.id.iv_left)
    ImageView iv_left;
    @BindView(R.id.tv_navi_title)
    TextView tv_navi_title;
    @BindView(R.id.tv_merchant)
    TextView tv_merchant;
    @BindView(R.id.tv_desc)
    TextView tv_desc;
    @BindView(R.id.tv_expired)
    TextView tv_expired;
    @BindView(R.id.tv_number)
    TextView tv_number;
    @BindView(R.id.iv_qrcode)
    ImageView iv_qrcode;
    @BindView(R.id.tv_agreement)
    TextView tv_agreement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_use);
        tv_navi_title.setText("WiCoupon");
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
        tv_merchant.setText(coupon.getProduct().getMerchant().getName());
        tv_desc.setText(coupon.getProduct().getShortDesc());
        String expired = (TextUtils.isEmpty(coupon.getStartTimeLocal()) ? "" : coupon.getStartTimeLocal()) + (TextUtils.isEmpty(coupon.getEndTimeLocal()) ? "" : " - " + coupon.getEndTimeLocal());
        tv_expired.setText(expired);
        tv_number.setText("no."+coupon.getProduct().getNumPrefix()+coupon.getCid().substring(10));
        //显示html文本（只有html样式,不带图片）
        if(!TextUtils.isEmpty(coupon.getProduct().getAgreement())) {
            tv_agreement.setText(Html.fromHtml(coupon.getProduct().getAgreement()));
        }
        try {
            Bitmap qrCodeBitmap=QRCodeEncoder.encodeAsBitmap("UseCoupon:"+coupon.getOrderId(), BarcodeFormat.QR_CODE,200);
            iv_qrcode.setImageBitmap(qrCodeBitmap);
        } catch (WriterException e) {
            toast("Failed to load QR Code.");
        }

    }

}
