package com.campray.lesswalletandroid.ui;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.campray.lesswalletandroid.R;
import com.campray.lesswalletandroid.db.entity.Coupon;
import com.campray.lesswalletandroid.db.entity.CouponStyle;
import com.campray.lesswalletandroid.event.RefreshEvent;
import com.campray.lesswalletandroid.model.BaseModel;
import com.campray.lesswalletandroid.model.CouponModel;
import com.campray.lesswalletandroid.model.ProductModel;
import com.campray.lesswalletandroid.qrcode.encode.QRCodeEncoder;
import com.campray.lesswalletandroid.ui.base.MenuActivity;
import com.campray.lesswalletandroid.util.ImageUtil;
import com.google.zxing.BarcodeFormat;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
    @BindView(R.id.tv_uservalues)
    TextView tv_uservalues;

    @BindView(R.id.iv_qrcode)
    ImageView iv_qrcode;

    @BindView(R.id.ll_uservalues)
    LinearLayout ll_uservalues;
    @BindView(R.id.ll_benefit)
    LinearLayout ll_benefit;
    @BindView(R.id.ll_validty)
    LinearLayout ll_validty;

    private long couponId=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_use);
        tv_navi_title.setText("Coupon Details");
        couponId=this.getBundle().getLong("coupon_id");
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
        final Coupon coupon=CouponModel.getInstance().getCouponById(couponId);
        if(coupon!=null) {
            CouponStyle couponStyle = coupon.getCouponStyle();
            if (couponStyle != null) {
                if (couponStyle.getValidityDay() > 0) {
                    tv_validity.setText(couponStyle.getValidityDay() + getResources().getString(R.string.coupon_validity_day));
                } else if (couponStyle.getValidityMonth() > 0) {
                    tv_validity.setText(couponStyle.getValidityMonth() + getResources().getString(R.string.coupon_validity_month));
                } else if (couponStyle.getValidityYear() > 0) {
                    tv_validity.setText(couponStyle.getValidityYear() + getResources().getString(R.string.coupon_validity_year));
                } else {
                    tv_validity.setText(getResources().getString(R.string.coupon_validity_nolimit));
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
                else{
                    ll_benefit.setVisibility(View.GONE);
                }
            }
            //如果是电子票
            if(coupon.getProduct().getProductTypeId()==2) {
                tv_uservalues.setText(Html.fromHtml(coupon.getUserValuesInfo()));
                ll_uservalues.setVisibility(View.VISIBLE);
            }

            if (coupon.getProduct().getPrice() > 0) {
                tv_price.setText(coupon.getProduct().getPriceStr());
            } else {
                tv_price.setText(getResources().getString(R.string.coupon_free));
            }
            tv_title.setText(coupon.getProduct().getTitle());
            tv_merchant.setText(coupon.getProduct().getMerchant().getName());
            tv_shortdesc.setText(coupon.getProduct().getShortDesc());
            String expired = (TextUtils.isEmpty(coupon.getStartTimeLocal()) ? "" : coupon.getStartTimeLocal()) + (TextUtils.isEmpty(coupon.getEndTimeLocal()) ? " _" : " - " + coupon.getEndTimeLocal());
            tv_expired.setText(expired);

            //显示html文本（只有html样式,不带图片）
            if (!TextUtils.isEmpty(coupon.getProduct().getFullDesc())) {
                final Html.ImageGetter imageGetter = new Html.ImageGetter() {
                    public Drawable getDrawable(String source) {
                        if(!source.startsWith("http:")){
                            source= BaseModel.HOST+source;
                        }
//                        Bitmap bitmap=ImageUtil.GetImageInputStream(source);
//                        Drawable drawable = new BitmapDrawable(getResources(),bitmap);
                        Drawable drawable = ProductModel.getInstance().httpRequestImage(source);
                        if(drawable!=null) {
                            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                        }
                        return drawable;
                    }
                };

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    tv_desc.setText(Html.fromHtml(coupon.getProduct().getFullDesc(),Html.FROM_HTML_MODE_COMPACT,imageGetter,null));
                }
                else{
                    tv_desc.setText(Html.fromHtml(coupon.getProduct().getFullDesc()));
                }
            }
            try {
                String couponStr = "coupon:" + coupon.getOrderId();
                //如果是电子票
                if(coupon.getProduct().getProductTypeId()==2) {
                    couponStr = "ticket:" + coupon.getOrderId();
                }
                Bitmap qrCodeBitmap = QRCodeEncoder.encodeAsBitmap(Base64.encodeToString(couponStr.getBytes("UTF-8"), Base64.DEFAULT), BarcodeFormat.QR_CODE, 200);
                iv_qrcode.setImageBitmap(qrCodeBitmap);
            } catch (Exception e) {
                toast("Failed to load QR Code.");
            }
        }
        else{
            toast(getResources().getString(R.string.text_coupon_is_deleded));
            finish();
        }
    }

    /**
     * 注册消息接收事件，接收EventBus通過EventBus.getDefault().post(T)方法发出的事件通知，onMessageEvent(T)方法就会自动接收并处理些事件通知
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(RefreshEvent event){
        finish();
    }
}
