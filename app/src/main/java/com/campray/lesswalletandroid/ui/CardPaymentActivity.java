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
    @BindView(R.id.rl_card_top)
    RelativeLayout rl_card_top;
    @BindView(R.id.iv_card_shading)
    ImageView iv_card_shading;
    @BindView(R.id.iv_card_img)
    ImageView iv_card_img;
    @BindView(R.id.iv_card_logo)
    ImageView iv_card_logo;
    @BindView(R.id.tv_price)
    TextView tv_price;
    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.tv_merchant)
    TextView tv_merchant;
    @BindView(R.id.tv_number)
    TextView tv_number;
    @BindView(R.id.ll_card_layout)
    LinearLayout ll_card_layout;

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
        setContentView(R.layout.activity_card_payment);
        long pid = this.getBundle().getLong("pid");
        long tid = this.getBundle().getLong("tid");
        //根据扫码获取的ID，从服务器获取对应的电子卡卷信息
        getCardFromServer(pid);
    }

    /**
     * 从服务器获取下载Card
     * @param pid
     */
    private void getCardFromServer(long pid){
        ProductModel.getInstance().getProductFromServer(pid, new OperationListener<Product>() {
            @Override
            public void done(Product product, AppException exe) {
                if (exe == null) {
                    curProduct=product;
                    showCard(product);
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
    private void showCard(final Product product){
        try {
            String benefit = "";
            String bgColor = "#137656";
            String timeStr = (TextUtils.isEmpty(product.getStartTimeLocal()) ? "" : product.getStartTimeLocal()) + (TextUtils.isEmpty(product.getEndTimeLocal()) ? "" : " - " + product.getEndTimeLocal());
            List<SpecAttr> specAttrList = product.getSpecAttr();
            for (SpecAttr specAttrBean : specAttrList) {
                String selectValue = specAttrBean.getColorSquaresRgb();
                String customValue = specAttrBean.getValueRaw();
                String value = (TextUtils.isEmpty(selectValue) && TextUtils.isEmpty(customValue)) ? specAttrBean.getSpecificationAttributeName() : (TextUtils.isEmpty(selectValue) ? customValue : selectValue);
                //如果是背景色
                if (specAttrBean.getSpecificationAttributeId() == 7) {
                    bgColor = value;
                } else if (specAttrBean.getSpecificationAttributeId() == 8) {//如果是底纹
                    shadingUrl = value;
                } else if (specAttrBean.getSpecificationAttributeId() == 9) {//如果是自定义图片
                    customPicUrl = value;
                } else if (specAttrBean.getSpecificationAttributeId() == 10) {//如果是商用家logo
                    logoUrl = value;
                } else {
                    benefit = value;
                }

            }
            //获取Card的自定义布局
            //LayerDrawable layerDrawable=(LayerDrawable)ll_card_layout.getBackground();
            //设置卡的背景色
            GradientDrawable gradientDrawable = (GradientDrawable) rl_card_top.getBackground();
            gradientDrawable.setColor(Color.parseColor(bgColor));

            //加载底纹
            UniversalImageLoader imageLoader = new UniversalImageLoader();
            imageLoader.load(iv_card_shading, shadingUrl, 0, null);
            //加载自定义图片
            //imageLoader.load(iv_card_img, customPicUrl, 0, null);
            //加载商家logo
            imageLoader.load(iv_card_logo, logoUrl, 0, null);

            tv_price.setText(benefit);
            tv_title.setText(product.getTitle());
            tv_merchant.setText(product.getMerchant().getName());
            tv_number.setText(product.getPrice() + "");
            gl_dialog.setVisibility(View.VISIBLE);
        }
        catch (Exception e){}

    }



    /**
     * 点击Yes按钮确认支付或接收Card
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
                                iv_card_shading.setDrawingCacheEnabled(true);
                                File shadingFile = ImageUtil.saveImage(iv_card_shading.getDrawingCache(), strArr[strArr.length - 1]);
                                specAttrBean.setFileUrl(Uri.fromFile(shadingFile).toString());
                                iv_card_shading.setDrawingCacheEnabled(false);
                                iv_card_shading.destroyDrawingCache();
                                needUpdate=true;
                            }
                        }
                        else if(specAttrBean.getSpecificationAttributeId()==9){//如果是自定义图片
                            if(TextUtils.isEmpty(specAttrBean.getFileUrl())) {
                                String[] strArr = customPicUrl.split("\\.");
                                iv_card_img.setDrawingCacheEnabled(true);
                                File imageFile = ImageUtil.saveImage(iv_card_img.getDrawingCache(), strArr[strArr.length - 1]);
                                specAttrBean.setFileUrl(Uri.fromFile(imageFile).toString());
                                iv_card_img.setDrawingCacheEnabled(false);
                                iv_card_img.destroyDrawingCache();
                                needUpdate=true;
                            }
                        }
                        else if(specAttrBean.getSpecificationAttributeId()==10){//如果是商用家logo
                            if(TextUtils.isEmpty(specAttrBean.getFileUrl())) {
                                String[] strArr = logoUrl.split("\\.");
                                iv_card_logo.setDrawingCacheEnabled(true);
                                File logoFile = ImageUtil.saveImage(iv_card_logo.getDrawingCache(), strArr[strArr.length - 1]);
                                specAttrBean.setFileUrl(Uri.fromFile(logoFile).toString());
                                iv_card_logo.setDrawingCacheEnabled(false);
                                iv_card_logo.destroyDrawingCache();
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
