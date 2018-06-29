package com.campray.lesswalletandroid.ui;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.braintreepayments.api.dropin.DropInActivity;
import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.api.dropin.DropInResult;
import com.braintreepayments.api.models.BraintreePaymentResult;
import com.braintreepayments.api.models.GooglePaymentRequest;
import com.braintreepayments.api.models.IdealResult;
import com.braintreepayments.api.models.PaymentMethodNonce;
import com.campray.lesswalletandroid.LessWalletApplication;
import com.campray.lesswalletandroid.R;
import com.campray.lesswalletandroid.db.entity.Coupon;
import com.campray.lesswalletandroid.db.entity.Product;
import com.campray.lesswalletandroid.db.entity.SpecAttr;
import com.campray.lesswalletandroid.listener.OperationListener;
import com.campray.lesswalletandroid.model.CouponModel;
import com.campray.lesswalletandroid.model.ProductModel;
import com.campray.lesswalletandroid.model.UserModel;
import com.campray.lesswalletandroid.ui.base.MenuActivity;
import com.campray.lesswalletandroid.util.AppException;
import com.campray.lesswalletandroid.util.ImageUtil;
import com.campray.lesswalletandroid.util.ResourcesUtils;
import com.google.android.gms.wallet.Cart;
import com.google.android.gms.wallet.LineItem;
import com.google.android.gms.wallet.TransactionInfo;
import com.google.android.gms.wallet.WalletConstants;
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

public class CouponPaymentActivity extends MenuActivity {
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

    private String clientToken=null;
    private static final int DROP_IN_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_payment);
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
                    if(curProduct.getPrice()==0){
                        gl_dialog.setVisibility(View.VISIBLE);
                    }
                    else{
                        gl_dialog.setVisibility(View.GONE);
                        getClientToken();
                    }
                } else {
                    toast(exe.toString(getApplicationContext()));
                    finish();
                }
            }
        });
    }

    /**
     * 从服务器获取Braintree的Client Token
     */
    private void getClientToken(){
        //获取Paypal的Braintree支付的CLient Token
        UserModel.getInstance().getPaypalClientToken(new OperationListener<String>() {
            @Override
            public void done(String token, AppException exception) {
                if(exception==null) {
                    clientToken=token;
                    gl_dialog.setVisibility(View.VISIBLE);
                }
                else{
                    toast("Get payment token failed,please try again.");
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
        Picasso.with(this).load(shadingUrl).memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE).into(iv_coupon_shading);
        //加载自定义图片
        Picasso.with(this).load(customPicUrl).memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE).into(iv_coupon_img);
        //加载商家logo
        Picasso.with(this).load(logoUrl).memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE).into(iv_coupon_logo);


        tv_price.setText(benefit);
        tv_title.setText(product.getTitle());
        tv_desc.setText(product.getShortDesc());
        tv_merchant.setText(product.getMerchant().getName());
        tv_expired.setText(timeStr);
        tv_number.setText(product.getPrice()+"");
        gl_dialog.setVisibility(View.VISIBLE);

        rl_coupon_layout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
//        WrapView wrapView=new WrapView(tv_desc);
//        if(descHeight==0){
//            descHeight=tv_desc.getHeight();
//        }
                if(tv_desc.getVisibility()==View.GONE) {
                    tv_desc.setVisibility(View.VISIBLE);
//            ObjectAnimator moveAnimator = ObjectAnimator.ofInt(tv_desc, "height",descHeight);
//            moveAnimator.setDuration(200);
//            ObjectAnimator moveAnimator1 = ObjectAnimator.ofInt(tv_desc, "height",descHeight,descHeight-100,descHeight+20,descHeight-50,descHeight+10,descHeight-20,descHeight);
//            moveAnimator1.setDuration(200);
//            AnimatorSet animSet = new AnimatorSet();
//            animSet.play(moveAnimator1).after(moveAnimator);
//            animSet.start();

                }
                else{
//            ObjectAnimator moveAnimator = ObjectAnimator.ofInt(tv_desc, "height",0);
//            moveAnimator.setDuration(200);
//            moveAnimator.start();
                    tv_desc.setVisibility(View.GONE);
                }
            }
        });
    }



    /**
     * 点击Yes按钮确认支付或接收Coupon
     */
    @OnClick(R.id.btn_yes)
    public void onClickButtonYes(){
        btn_yes.setEnabled(false);
        if(curProduct.getPrice()>0) {
            Cart card=Cart.newBuilder()
                    .setCurrencyCode(curProduct.getCurrencyCode())
                    .setTotalPrice(curProduct.getPrice()+"")
                    .addLineItem(LineItem.newBuilder()
                            .setCurrencyCode(curProduct.getCurrencyCode())
                            .setDescription(curProduct.getTitle())
                            .setQuantity("1")
                            .setUnitPrice(curProduct.getPrice()+"")
                            .setTotalPrice(curProduct.getPrice()+"")
                            .build())
                    .build();

            DropInRequest dropInRequest = new DropInRequest()
            .amount(curProduct.getPrice()+"")
            .clientToken(clientToken)
            .collectDeviceData(false)
            .requestThreeDSecureVerification(false)
            .androidPayCart(card)
            .androidPayShippingAddressRequired(false)
            .androidPayPhoneNumberRequired(false);

            startActivityForResult(dropInRequest.getIntent(this), DROP_IN_REQUEST);
//            Intent intent=new Intent(this, PaymentMethodsActivity.class);
//            intent.putExtra("productId",curProduct.getProductId());
//            intent.putExtra("price",curProduct.getPrice());
//            startActivityForResult(intent, DROP_IN_REQUEST);
            btn_yes.setEnabled(true);
        }else{
            CouponModel.getInstance().confirmCoupon(curProduct.getProductId(), new OperationListener<Coupon>() {
                @Override
                public void done(Coupon coupon, AppException exception) {
                    if (exception == null) {
                        saveCouponImage(coupon);
                        toast("优惠卷已经成功收入您的钱包中!");
                        finish();
                    } else {
                        btn_yes.setEnabled(true);
                        toast("获取优惠卷失败，错误原因: " + getResources().getString(ResourcesUtils.getStringId(getApplicationContext(), exception.getErrorCode())));
                    }
                }
            });
        }
    }

    /**
     * 保存处理Coupon图片
     * @param coupon
     */
    private void saveCouponImage(Coupon coupon){
        boolean needUpdate = false;
        Product productBean = coupon.getProduct();
        List<SpecAttr> specAttrBeanList = productBean.getSpecAttr();
        for (SpecAttr specAttrBean : specAttrBeanList) {
            if (specAttrBean.getSpecificationAttributeId() == 8) {//如果是底纹
                if (TextUtils.isEmpty(specAttrBean.getFileUrl())) {
                    String[] strArr = shadingUrl.split("\\.");
                    //保存图片到手机存储空间
                    iv_coupon_shading.setDrawingCacheEnabled(true);
                    File shadingFile = ImageUtil.saveImage(iv_coupon_shading.getDrawingCache(), strArr[strArr.length - 1]);
                    specAttrBean.setFileUrl(Uri.fromFile(shadingFile).toString());
                    iv_coupon_shading.setDrawingCacheEnabled(false);
                    iv_coupon_shading.destroyDrawingCache();
                    needUpdate = true;
                }
            } else if (specAttrBean.getSpecificationAttributeId() == 9) {//如果是自定义图片
                if (TextUtils.isEmpty(specAttrBean.getFileUrl())) {
                    String[] strArr = customPicUrl.split("\\.");
                    iv_coupon_img.setDrawingCacheEnabled(true);
                    File imageFile = ImageUtil.saveImage(iv_coupon_img.getDrawingCache(), strArr[strArr.length - 1]);
                    specAttrBean.setFileUrl(Uri.fromFile(imageFile).toString());
                    iv_coupon_img.setDrawingCacheEnabled(false);
                    iv_coupon_img.destroyDrawingCache();
                    needUpdate = true;
                }
            } else if (specAttrBean.getSpecificationAttributeId() == 10) {//如果是商用家logo
                if (TextUtils.isEmpty(specAttrBean.getFileUrl())) {
                    String[] strArr = logoUrl.split("\\.");
                    iv_coupon_logo.setDrawingCacheEnabled(true);
                    File logoFile = ImageUtil.saveImage(iv_coupon_logo.getDrawingCache(), strArr[strArr.length - 1]);
                    specAttrBean.setFileUrl(Uri.fromFile(logoFile).toString());
                    iv_coupon_logo.setDrawingCacheEnabled(false);
                    iv_coupon_logo.destroyDrawingCache();
                    needUpdate = true;
                }
            }
        }
        if (needUpdate) {
            ProductModel.getInstance().updaeProduct(productBean);
        }

    }

    @OnClick(R.id.btn_no)
    public void onClickButtonNo(){
        btn_no.setEnabled(false);
        if(curProduct.getCoupons()==null||curProduct.getCoupons().size()==0) {
            ProductModel.getInstance().deleteProductById(curProduct.getProductId());
        }
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == DROP_IN_REQUEST) {
                //使用DropIn时的处理
                DropInResult result = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
                PaymentMethodNonce paymentMethodNonce =result.getPaymentMethodNonce();
                long productId=curProduct.getProductId();
//                PaymentMethodNonce paymentMethodNonce = data.getParcelableExtra("NONCE");
//                long productId=data.getLongExtra("productId",0);
                if(productId>0) {
                    //支付Coupon
                    CouponModel.getInstance().paypalCoupon(productId, paymentMethodNonce.getNonce(), new OperationListener<Coupon>() {
                        @Override
                        public void done(Coupon coupon, AppException exception) {
                            if (exception == null) {
                                saveCouponImage(coupon);
                                toast("优惠卷已经成功收入您的钱包中!");
                                finish();
                            } else {
                                btn_yes.setEnabled(true);
                                int errorId=ResourcesUtils.getStringId(getApplicationContext(), exception.getErrorCode());
                                if(errorId>0) {
                                    toast("获取优惠卷失败，错误原因: " + getResources().getString(errorId));
                                }
                                else{
                                    toast("获取优惠卷失败，错误原因: " + exception.getErrorCode());
                                }
                            }
                        }
                    });
                }
                else{
                    toast("支付异常，错误原因:支付商品ID不匹配! ");
                }
            }

        }
        else if (resultCode != RESULT_CANCELED) {
            //使用DropIn时的异常处理
            Exception exe=(Exception) data.getSerializableExtra(DropInActivity.EXTRA_ERROR);
            //Exception exe=(Exception) data.getSerializableExtra("EXTRA_ERROR");
            if(exe!=null) {
                new AlertDialog.Builder(this).setMessage(exe.getMessage())
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
            }
        }
    }
}
