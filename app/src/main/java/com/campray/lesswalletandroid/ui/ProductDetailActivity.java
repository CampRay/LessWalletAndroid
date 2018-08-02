package com.campray.lesswalletandroid.ui;

import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.campray.lesswalletandroid.R;
import com.campray.lesswalletandroid.db.entity.CouponStyle;
import com.campray.lesswalletandroid.db.entity.Product;
import com.campray.lesswalletandroid.model.ProductModel;
import com.campray.lesswalletandroid.ui.base.MenuActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Phills on 11/2/2017.
 */

public class ProductDetailActivity extends MenuActivity {
    @BindView(R.id.iv_left)
    ImageView iv_left;
    @BindView(R.id.tv_navi_title)
    TextView tv_navi_title;
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
    @BindView(R.id.tv_validity)
    TextView tv_validity;
    @BindView(R.id.tv_stock)
    TextView tv_stock;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.ll_benefit)
    LinearLayout ll_benefit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        tv_navi_title.setText("Coupon Information");
        long productId=this.getIntent().getLongExtra("id",0);
        String cash=this.getIntent().getStringExtra("cash");
        String service=this.getIntent().getStringExtra("service");
        String buy=this.getIntent().getStringExtra("buy");
        loadData(productId,cash,service,buy);
    }

    /**
     * 点击iv_left按钮的事件方法
     * @param view
     */
    @OnClick(R.id.iv_left)
    public void onBackClick(View view){
        this.finish();
    }

    private void loadData(long productId,String cash,String service,String buy){
        Product product= ProductModel.getInstance().getProductById(productId);
        CouponStyle couponStyle=product.getCouponStyle();
        if (couponStyle != null) {
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

            if (couponStyle.getValidityDay() > 0) {
                tv_validity.setText(couponStyle.getValidityDay() + getResources().getString(R.string.coupon_validity_day));
            } else if (couponStyle.getValidityMonth() > 0) {
                tv_validity.setText(couponStyle.getValidityMonth() + getResources().getString(R.string.coupon_validity_month));
            } else if (couponStyle.getValidityYear() > 0) {
                tv_validity.setText(couponStyle.getValidityYear() + getResources().getString(R.string.coupon_validity_year));
            }
            else{
                tv_validity.setText(getResources().getString(R.string.coupon_validity_nolimit));
            }
        }
        tv_title.setText(product.getTitle());
        tv_price.setText(product.getPriceStr());
        tv_stock.setText(product.getStockQuantity()+"");
        tv_shortdesc.setText(product.getShortDesc());
        String expired = (TextUtils.isEmpty(product.getStartTimeLocal()) ? "" : product.getStartTimeLocal()) + (TextUtils.isEmpty(product.getEndTimeLocal()) ? "" : " - " + product.getEndTimeLocal());
        tv_expired.setText(expired);

        //显示html文本（只有html样式,不带图片）
        if(!TextUtils.isEmpty(product.getFullDesc())) {
            tv_desc.setText(Html.fromHtml(product.getFullDesc()));
        }

    }

}
