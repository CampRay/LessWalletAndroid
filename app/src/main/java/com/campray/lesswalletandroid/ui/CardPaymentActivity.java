package com.campray.lesswalletandroid.ui;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
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
import com.braintreepayments.api.models.PaymentMethodNonce;
import com.campray.lesswalletandroid.R;
import com.campray.lesswalletandroid.db.entity.Coupon;
import com.campray.lesswalletandroid.db.entity.CouponStyle;
import com.campray.lesswalletandroid.db.entity.Currency;
import com.campray.lesswalletandroid.db.entity.Product;
import com.campray.lesswalletandroid.db.entity.SpecAttr;
import com.campray.lesswalletandroid.listener.OperationListener;
import com.campray.lesswalletandroid.model.CouponModel;
import com.campray.lesswalletandroid.model.CurrencyModel;
import com.campray.lesswalletandroid.model.ProductModel;
import com.campray.lesswalletandroid.ui.base.MenuActivity;
import com.campray.lesswalletandroid.util.AppException;
import com.campray.lesswalletandroid.util.ImageUtil;
import com.campray.lesswalletandroid.util.ResourcesUtils;
import com.google.android.gms.wallet.Cart;
import com.google.android.gms.wallet.LineItem;
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
    @BindView(R.id.tv_validity)
    TextView tv_validity;
    @BindView(R.id.tv_class)
    TextView tv_class;
    @BindView(R.id.tv_shortdesc)
    TextView tv_shortdesc;

    @BindView(R.id.tv_merchant)
    TextView tv_merchant;
    @BindView(R.id.tv_number)
    TextView tv_number;
    @BindView(R.id.ll_card_front)
    LinearLayout ll_card_front;
    @BindView(R.id.ll_card_back)
    LinearLayout ll_card_back;
    @BindView(R.id.tv_more)
    TextView tv_more;

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
            CouponStyle couponStyle=product.getCouponStyle();
            if(couponStyle!=null){
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
                tv_class.setText(couponStyle.getCardLevel()+"");

                //设置背景色
                View view = findViewById(R.id.rl_card_top);
                GradientDrawable gd = (GradientDrawable) view.getBackground();
                gd.setColor(Color.parseColor(couponStyle.getBgColor()));

                View backview = findViewById(R.id.rl_card_back);
                GradientDrawable backgd = (GradientDrawable) backview.getBackground();
                backgd.setColor(Color.parseColor("#f2e4cf"));

                if (!TextUtils.isEmpty(couponStyle.getShadingUrl())) {
                    Picasso.with(this).load(couponStyle.getShadingUrl()).memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE).into(iv_card_shading);
                } else {
                    iv_card_shading.setVisibility(View.GONE);
                }
                if (!TextUtils.isEmpty(couponStyle.getPictureUrl())) {
                    Picasso.with(this).load(couponStyle.getPictureUrl()).memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE).into(iv_card_img);
                } else {
                    iv_card_img.setVisibility(View.GONE);
                }
                if (!TextUtils.isEmpty(couponStyle.getLogoUrl())) {
                    Picasso.with(this).load(couponStyle.getLogoUrl()).memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE).into(iv_card_logo);
                } else {
                    iv_card_logo.setVisibility(View.GONE);
                }
            }
            if(product.getPrice()>0){
                tv_price.setText(product.getPriceStr());
            }
            else{
                tv_price.setText(getResources().getString(R.string.coupon_free));
            }

            tv_title.setText(product.getTitle());
            tv_shortdesc.setText(product.getShortDesc());
            tv_merchant.setText(product.getMerchant().getName());

            //coard的对象点击事件,处理实现翻转动画
            ll_card_front.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    flipAnimatorXViewShow(v,ll_card_back,200);
                }
            });
            ll_card_back.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    flipAnimatorXViewShow(v,ll_card_front,200);
                }
            });
            tv_more.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(CardPaymentActivity.this, ProductDetailActivity.class);
                    intent.putExtra("id",product.getProductId());
                    startActivity(intent);
                }
            });
        }
        catch (Exception e){}

    }



    /**
     * 点击Yes按钮确认支付或接收Card
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
                        toast("会员卡已经成功收入您的钱包中!");
                        finish();
                    } else {
                        btn_yes.setEnabled(true);
                        toast("获取会员卡失败，错误原因: " + getResources().getString(ResourcesUtils.getStringId(getApplicationContext(), exception.getErrorCode())));
                    }
                }
            });
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

    /**
     * 保存处理Coupon图片
     * @param coupon
     */
    private void saveCouponImage(Coupon coupon){
        boolean needUpdate = false;
        Product productBean = coupon.getProduct();
        List<SpecAttr> specAttrBeanList = productBean.getSpecAttr();
        for (SpecAttr specAttrBean : specAttrBeanList) {
            if (specAttrBean.getSpecificationAttributeId() == 6) {//如果是底纹
                if (TextUtils.isEmpty(specAttrBean.getFileUrl())) {
                    shadingUrl=specAttrBean.getValueRaw();
                    String[] strArr = shadingUrl.split("\\.");
                    //保存图片到手机存储空间
                    iv_card_shading.setDrawingCacheEnabled(true);
                    File shadingFile = ImageUtil.saveImage(iv_card_shading.getDrawingCache(), strArr[strArr.length - 1]);
                    specAttrBean.setFileUrl(Uri.fromFile(shadingFile).toString());
                    iv_card_shading.setDrawingCacheEnabled(false);
                    iv_card_shading.destroyDrawingCache();
                    needUpdate = true;
                }
            } else if (specAttrBean.getSpecificationAttributeId() == 7) {//如果是自定义图片
                if (TextUtils.isEmpty(specAttrBean.getFileUrl())) {
                    customPicUrl=specAttrBean.getValueRaw();
                    String[] strArr = customPicUrl.split("\\.");
                    iv_card_img.setDrawingCacheEnabled(true);
                    File imageFile = ImageUtil.saveImage(iv_card_img.getDrawingCache(), strArr[strArr.length - 1]);
                    specAttrBean.setFileUrl(Uri.fromFile(imageFile).toString());
                    iv_card_img.setDrawingCacheEnabled(false);
                    iv_card_img.destroyDrawingCache();
                    needUpdate = true;
                }
            } else if (specAttrBean.getSpecificationAttributeId() == 8) {//如果是商用家logo
                if (TextUtils.isEmpty(specAttrBean.getFileUrl())) {
                    logoUrl=specAttrBean.getValueRaw();
                    String[] strArr = logoUrl.split("\\.");
                    iv_card_logo.setDrawingCacheEnabled(true);
                    File logoFile = ImageUtil.saveImage(iv_card_logo.getDrawingCache(), strArr[strArr.length - 1]);
                    specAttrBean.setFileUrl(Uri.fromFile(logoFile).toString());
                    iv_card_logo.setDrawingCacheEnabled(false);
                    iv_card_logo.destroyDrawingCache();
                    needUpdate = true;
                }
            }
        }
        if (needUpdate) {
            ProductModel.getInstance().updaeProduct(productBean);
        }

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
                                toast("会员卡已经成功收入您的钱包中!");
                                finish();
                            } else {
                                btn_yes.setEnabled(true);
                                int errorId=ResourcesUtils.getStringId(getApplicationContext(), exception.getErrorCode());
                                if(errorId>0) {
                                    toast("获取会员卡失败，错误原因: " + getResources().getString(errorId));
                                }
                                else{
                                    toast("获取会员卡失败，错误原因: " + exception.getErrorCode());
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

    /**
     * 翻转动画
     * @param oldView
     * @param newView
     * @param time
     */
    private void flipAnimatorXViewShow(final View oldView, final View newView, final long time) {

        ObjectAnimator animator1 = ObjectAnimator.ofFloat(oldView, "rotationY", 0, 90);
        final ObjectAnimator animator2 = ObjectAnimator.ofFloat(newView, "rotationY", -90, 0);

        animator1.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                setCameraDistance(oldView);
                setCameraDistance(newView);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                oldView.setVisibility(View.GONE);
                animator2.setDuration(time).start();
                newView.setVisibility(View.VISIBLE);
            }
            @Override
            public void onAnimationCancel(Animator animation) {}

            @Override
            public void onAnimationRepeat(Animator animation) {}
        });
        animator1.setDuration(time).start();
    }

    /**
     * 改变视角距离, 要不然翻转时视图会拉伸很长
     */
    private void setCameraDistance(View view) {
        int distance = 16000;
        float scale = this.getResources().getDisplayMetrics().density * distance;
        view.setCameraDistance(scale);
    }


}
