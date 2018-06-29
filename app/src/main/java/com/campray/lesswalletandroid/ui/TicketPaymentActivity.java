package com.campray.lesswalletandroid.ui;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.campray.lesswalletandroid.R;
import com.campray.lesswalletandroid.db.entity.Coupon;
import com.campray.lesswalletandroid.db.entity.Product;
import com.campray.lesswalletandroid.db.entity.SpecAttr;
import com.campray.lesswalletandroid.listener.OperationListener;
import com.campray.lesswalletandroid.model.CouponModel;
import com.campray.lesswalletandroid.model.ProductModel;
import com.campray.lesswalletandroid.ui.base.MenuActivity;
import com.campray.lesswalletandroid.util.AppException;
import com.campray.lesswalletandroid.util.ImageUtil;
import com.campray.lesswalletandroid.util.ResourcesUtils;
import com.campray.lesswalletandroid.view.InnerCornerView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 扫描卡卷二维码后确认或支付页面
 * Created by Phills on 11/2/2017.
 */

public class TicketPaymentActivity extends MenuActivity {
    //Coupon控件

    @BindView(R.id.iv_coupon_img)
    ImageView iv_coupon_img;
    @BindView(R.id.tv_price)
    TextView tv_price;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_desc)
    TextView tv_desc;
    //Ticket控件
    @BindView(R.id.rl_ticket_layout)
    RelativeLayout rl_ticket_layout;
    @BindView(R.id.ll_top_layout)
    LinearLayout ll_top_layout;
    @BindView(R.id.icv_tleft)
    InnerCornerView icv_tleft;
    @BindView(R.id.icv_tright)
    InnerCornerView icv_tright;
    @BindView(R.id.tv_top_bg)
    TextView tv_top_bg;
    @BindView(R.id.tv_date)
    TextView tv_date;
    @BindView(R.id.tv_time)
    TextView tv_time;
    @BindView(R.id.tv_seat)
    TextView tv_seat;
    @BindView(R.id.tv_row)
    TextView tv_row;
    @BindView(R.id.tv_section)
    TextView tv_section;

    @BindView(R.id.gl_dialog)
    GridLayout gl_dialog;
    @BindView(R.id.btn_yes)
    TextView btn_yes;
    @BindView(R.id.btn_no)
    TextView btn_no;

    private int typeId=1;
    private Product curProduct=null;
    private String shadingUrl = null;
    private String customPicUrl = null;
    private String logoUrl = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_payment);
        long pid = this.getBundle().getLong("pid");
        long tid = this.getBundle().getLong("tid");
        //根据扫码获取的ID，从服务器获取对应的电子卡卷信息
        getCouponFromServer(pid);
    }

    /**
     * 从服务器获取下载Coupon
     * @param pid
     */
    private void getCouponFromServer(long pid){
        ProductModel.getInstance().getProductFromServer(pid, new OperationListener<Product>() {
            @Override
            public void done(Product product, AppException exe) {
                if (exe == null) {
                    curProduct=product;
                    showTicket(product);
                } else {
                    toast(exe.toString(getApplicationContext()));
                    finish();
                }
            }
        });
    }

    /**
     * 根据获取的电子票信息，显示电子票图像
     * @param product
     */
    private void showTicket(final Product product){
        String benefit = "";
        String bgColor= "#137656";
        String timeStr = (TextUtils.isEmpty(product.getStartTimeLocal())?"":product.getStartTimeLocal()) + (TextUtils.isEmpty(product.getEndTimeLocal()) ? "" : " - " + product.getEndTimeLocal());
        List<SpecAttr> specAttrList = product.getSpecAttr();
        for (SpecAttr specAttrBean:specAttrList) {
            String selectValue = specAttrBean.getColorSquaresRgb();
            String customValue = specAttrBean.getValueRaw();
            String value=(TextUtils.isEmpty(selectValue)&& TextUtils.isEmpty(customValue))? specAttrBean.getSpecificationAttributeName(): (TextUtils.isEmpty(selectValue)?customValue : selectValue);
            //如果是背景色
            if(specAttrBean.getSpecificationAttributeId()==7){
                bgColor=value;
            }
            else if(specAttrBean.getSpecificationAttributeId()==8){//如果是底纹
                shadingUrl=value;
            }
            else if(specAttrBean.getSpecificationAttributeId()==9){//如果是自定义图片
                customPicUrl=value;
            }
            else if(specAttrBean.getSpecificationAttributeId()==10){//如果是商用家logo
                logoUrl=value;
            }
            else{
                benefit=value;
            }

        }

        ll_top_layout=(LinearLayout)this.findViewById(R.id.ll_top_layout);
        icv_tleft=(InnerCornerView)this.findViewById(R.id.icv_tleft);
        icv_tright=(InnerCornerView)this.findViewById(R.id.icv_tright);
        tv_top_bg=(TextView)this.findViewById(R.id.tv_top_bg);
        tv_date=(TextView)this.findViewById(R.id.tv_date);
        tv_time=(TextView)this.findViewById(R.id.tv_time);
        tv_seat=(TextView)this.findViewById(R.id.tv_seat);
        tv_row=(TextView)this.findViewById(R.id.tv_row);
        tv_section=(TextView)this.findViewById(R.id.tv_section);
        iv_coupon_img=(ImageView) this.findViewById(R.id.iv_coupon_img);
        tv_title=(TextView)this.findViewById(R.id.tv_title);
        tv_desc=(TextView)this.findViewById(R.id.tv_desc);
        tv_price=(TextView)this.findViewById(R.id.tv_price);
        gl_dialog=(GridLayout)this.findViewById(R.id.gl_dialog);
        btn_yes=(TextView)this.findViewById(R.id.btn_yes);
        btn_no=(TextView)this.findViewById(R.id.btn_no);

        //设置背景色
        int topColor=Color.parseColor(bgColor);
        try {
            LayerDrawable drawable = (LayerDrawable) ll_top_layout.getBackground();
            GradientDrawable item = (GradientDrawable)drawable.findDrawableByLayerId(R.id.top_item);
            item.setColor(topColor);
            icv_tleft.setCornerColor(topColor);
            icv_tright.setCornerColor(topColor);
            tv_top_bg.setBackgroundColor(topColor);

            //加载自定义图片
            Picasso.with(this).load(customPicUrl).memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE).into(iv_coupon_img);
            tv_title.setText(product.getTitle());
            tv_desc.setText(product.getShortDesc());
            tv_price.setText(product.getPrice()+"");
            gl_dialog.setVisibility(View.VISIBLE);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }



    /**
     * 点击Yes按钮确认支付或接收Coupon
     */
    @OnClick(R.id.btn_yes)
    public void onClickButtonYes(){
        btn_yes.setEnabled(false);
        CouponModel.getInstance().confirmCoupon(curProduct.getProductId(), new OperationListener<Coupon>() {
            @Override
            public void done(Coupon coupon, AppException exception) {
                if(exception==null){
                    Product productBean=coupon.getProduct();
                    List<SpecAttr> specAttrBeanList = productBean.getSpecAttr();
                    for (SpecAttr specAttrBean:specAttrBeanList) {
                        if(specAttrBean.getSpecificationAttributeId()==9){//如果是自定义图片
                            if(TextUtils.isEmpty(specAttrBean.getFileUrl())) {
                                String[] strArr = customPicUrl.split("\\.");
                                iv_coupon_img.setDrawingCacheEnabled(true);
                                File imageFile = ImageUtil.saveImage(iv_coupon_img.getDrawingCache(), strArr[strArr.length - 1]);
                                specAttrBean.setFileUrl(Uri.fromFile(imageFile).toString());
                                iv_coupon_img.setDrawingCacheEnabled(false);
                                iv_coupon_img.destroyDrawingCache();
                                ProductModel.getInstance().updaeProduct(productBean);
                            }
                        }
                    }

                    toast("电子票已经成功收入您的钱包中!");
                    finish();
                }
                else{
                    btn_yes.setEnabled(true);
                    toast("获取电子票失败，错误原因: "+getResources().getString(ResourcesUtils.getStringId(getApplicationContext(),exception.getErrorCode())));
                }
            }
        });

    }

    @OnClick(R.id.btn_no)
    public void onClickButtonNo(){
        btn_no.setEnabled(false);
        ProductModel.getInstance().deleteProductById(curProduct.getProductId());
        finish();
    }


}
