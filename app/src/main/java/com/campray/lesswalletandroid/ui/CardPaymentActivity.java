package com.campray.lesswalletandroid.ui;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.campray.lesswalletandroid.util.UniversalImageLoader;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 扫描卡卷二维码后确认或支付页面
 * Created by Phills on 11/2/2017.
 */

public class CardPaymentActivity extends MenuActivity {
    //Coupon控件
    @BindView(R.id.gl_coupon_top)
    GridLayout gl_coupon_top;
    @BindView(R.id.iv_coupon_shading)
    ImageView iv_coupon_shading;
    @BindView(R.id.iv_coupon_img)
    ImageView iv_coupon_img;
    @BindView(R.id.iv_coupon_logo)
    ImageView iv_coupon_logo;
    @BindView(R.id.tv_price)
    TextView tv_price;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_desc)
    TextView tv_desc;
    @BindView(R.id.tv_merchant)
    TextView tv_merchant;
    @BindView(R.id.tv_expired)
    TextView tv_expired;
    @BindView(R.id.tv_number)
    TextView tv_number;
    @BindView(R.id.rl_coupon_layout)
    LinearLayout rl_coupon_layout;

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
                    showCoupon(product);
                } else {
                    toast(exe.toString(getApplicationContext()));
                    finish();
                }
            }
        });
    }

    /**
     * 根据获取的卡卷信息，显示卡卷图像
     * @param product
     */
    private void showCoupon(final Product product){
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

        //设置背景色
        gl_coupon_top.setBackgroundColor(Color.parseColor(bgColor));
        //加载底纹
        UniversalImageLoader imageLoader=new UniversalImageLoader();
        imageLoader.load(iv_coupon_shading,shadingUrl,0,null);
        //加载自定义图片
        imageLoader.load(iv_coupon_img,customPicUrl,0,null);
        //加载商家logo
        imageLoader.load(iv_coupon_logo,logoUrl,0,null);

        tv_price.setText(benefit);
        tv_title.setText(product.getTitle());
        tv_desc.setText(product.getShortDesc());
        tv_merchant.setText(product.getMerchant().getName());
        tv_expired.setText(timeStr);
        tv_number.setText(product.getPrice()+"");
        gl_dialog.setVisibility(View.VISIBLE);

//        rl_coupon_layout.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
////        WrapView wrapView=new WrapView(tv_desc);
////        if(descHeight==0){
////            descHeight=tv_desc.getHeight();
////        }
//                if(tv_desc.getVisibility()==View.GONE) {
//                    tv_desc.setVisibility(View.VISIBLE);
////            ObjectAnimator moveAnimator = ObjectAnimator.ofInt(tv_desc, "height",descHeight);
////            moveAnimator.setDuration(200);
////            ObjectAnimator moveAnimator1 = ObjectAnimator.ofInt(tv_desc, "height",descHeight,descHeight-100,descHeight+20,descHeight-50,descHeight+10,descHeight-20,descHeight);
////            moveAnimator1.setDuration(200);
////            AnimatorSet animSet = new AnimatorSet();
////            animSet.play(moveAnimator1).after(moveAnimator);
////            animSet.start();
//
//                }
//                else{
////            ObjectAnimator moveAnimator = ObjectAnimator.ofInt(tv_desc, "height",0);
////            moveAnimator.setDuration(200);
////            moveAnimator.start();
//                    tv_desc.setVisibility(View.GONE);
//                }
//            }
//        });
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
                    boolean needUpdate=false;
                    Product productBean=coupon.getProduct();
                    List<SpecAttr> specAttrBeanList = productBean.getSpecAttr();
                    for (SpecAttr specAttrBean:specAttrBeanList) {
                        if(specAttrBean.getSpecificationAttributeId()==8){//如果是底纹
                            if(TextUtils.isEmpty(specAttrBean.getFileUrl())) {
                                String[] strArr = shadingUrl.split("\\.");
                                iv_coupon_shading.setDrawingCacheEnabled(true);
                                File shadingFile = ImageUtil.saveImage(iv_coupon_shading.getDrawingCache(), strArr[strArr.length - 1]);
                                specAttrBean.setFileUrl(Uri.fromFile(shadingFile).toString());
                                iv_coupon_shading.setDrawingCacheEnabled(false);
                                iv_coupon_shading.destroyDrawingCache();
                                needUpdate=true;
                            }
                        }
                        else if(specAttrBean.getSpecificationAttributeId()==9){//如果是自定义图片
                            if(TextUtils.isEmpty(specAttrBean.getFileUrl())) {
                                String[] strArr = customPicUrl.split("\\.");
                                iv_coupon_img.setDrawingCacheEnabled(true);
                                File imageFile = ImageUtil.saveImage(iv_coupon_img.getDrawingCache(), strArr[strArr.length - 1]);
                                specAttrBean.setFileUrl(Uri.fromFile(imageFile).toString());
                                iv_coupon_img.setDrawingCacheEnabled(false);
                                iv_coupon_img.destroyDrawingCache();
                                needUpdate=true;
                            }
                        }
                        else if(specAttrBean.getSpecificationAttributeId()==10){//如果是商用家logo
                            if(TextUtils.isEmpty(specAttrBean.getFileUrl())) {
                                String[] strArr = logoUrl.split("\\.");
                                iv_coupon_logo.setDrawingCacheEnabled(true);
                                File logoFile = ImageUtil.saveImage(iv_coupon_logo.getDrawingCache(), strArr[strArr.length - 1]);
                                specAttrBean.setFileUrl(Uri.fromFile(logoFile).toString());
                                iv_coupon_logo.setDrawingCacheEnabled(false);
                                iv_coupon_logo.destroyDrawingCache();
                                needUpdate=true;
                            }
                        }
                    }
                    if(needUpdate) {
                        ProductModel.getInstance().updaeProduct(productBean);
                    }

                    toast("优惠卷已经成功收入您的钱包中!");
                    finish();
                }
                else{
                    btn_yes.setEnabled(true);
                    toast("获取优惠卷失败，错误原因: "+getResources().getString(ResourcesUtils.getStringId(getApplicationContext(),exception.getErrorCode())));
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
