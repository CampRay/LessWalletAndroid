package com.campray.lesswalletandroid.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.braintreepayments.api.AndroidPay;
import com.braintreepayments.api.BraintreeFragment;
import com.braintreepayments.api.DataCollector;
import com.braintreepayments.api.PayPal;
import com.braintreepayments.api.PaymentMethod;
import com.braintreepayments.api.ThreeDSecure;
import com.braintreepayments.api.Venmo;
import com.braintreepayments.api.dropin.AddCardActivity;
import com.braintreepayments.api.dropin.DropInActivity;
import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.api.dropin.DropInResult;
import com.braintreepayments.api.dropin.adapters.SupportedPaymentMethodsAdapter;
import com.braintreepayments.api.dropin.adapters.VaultedPaymentMethodsAdapter;
import com.braintreepayments.api.dropin.interfaces.AnimationFinishedListener;
import com.braintreepayments.api.dropin.utils.PaymentMethodType;
import com.braintreepayments.api.exceptions.AuthenticationException;
import com.braintreepayments.api.exceptions.AuthorizationException;
import com.braintreepayments.api.exceptions.ConfigurationException;
import com.braintreepayments.api.exceptions.DownForMaintenanceException;
import com.braintreepayments.api.exceptions.GoogleApiClientException;
import com.braintreepayments.api.exceptions.InvalidArgumentException;
import com.braintreepayments.api.exceptions.ServerException;
import com.braintreepayments.api.exceptions.UnexpectedException;
import com.braintreepayments.api.exceptions.UpgradeRequiredException;
import com.braintreepayments.api.interfaces.BraintreeCancelListener;
import com.braintreepayments.api.interfaces.BraintreeErrorListener;
import com.braintreepayments.api.interfaces.BraintreeResponseListener;
import com.braintreepayments.api.interfaces.ConfigurationListener;
import com.braintreepayments.api.interfaces.PaymentMethodNonceCreatedListener;
import com.braintreepayments.api.interfaces.PaymentMethodNoncesUpdatedListener;
import com.braintreepayments.api.models.AndroidPayCardNonce;
import com.braintreepayments.api.models.CardNonce;
import com.braintreepayments.api.models.Configuration;
import com.braintreepayments.api.models.PayPalRequest;
import com.braintreepayments.api.models.PaymentMethodNonce;
import com.braintreepayments.api.models.PostalAddress;
import com.campray.lesswalletandroid.LessWalletApplication;
import com.campray.lesswalletandroid.R;
import com.campray.lesswalletandroid.db.entity.Country;
import com.campray.lesswalletandroid.db.entity.Currency;
import com.campray.lesswalletandroid.db.entity.Language;
import com.campray.lesswalletandroid.db.entity.ProductAttribute;
import com.campray.lesswalletandroid.db.entity.Slider;
import com.campray.lesswalletandroid.db.entity.User;
import com.campray.lesswalletandroid.listener.OperationListener;
import com.campray.lesswalletandroid.model.CountryModel;
import com.campray.lesswalletandroid.model.CurrencyModel;
import com.campray.lesswalletandroid.model.LanguageModel;
import com.campray.lesswalletandroid.model.ProductAttributeModel;
import com.campray.lesswalletandroid.model.SliderModel;
import com.campray.lesswalletandroid.model.UserModel;
import com.campray.lesswalletandroid.service.MsgPushService;
import com.campray.lesswalletandroid.ui.base.BaseActivity;
import com.campray.lesswalletandroid.util.AppException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.view.animation.AnimationUtils.loadAnimation;

/**
 * 显示支付方式页面
 * @author :Phills
 * @project:SplashActivity
 * @date :2018-06-15
 */
public class PaymentMethodsActivity extends BaseActivity implements BraintreeCancelListener,
        BraintreeErrorListener, PaymentMethodNonceCreatedListener {

    private View mBottomSheet;
    private ViewSwitcher mLoadingViewSwitcher;
    private TextView mSupportedPaymentMethodsHeader;
    @VisibleForTesting
    protected ListView mSupportedPaymentMethodListView;

    protected BraintreeFragment mBraintreeFragment;

    private boolean mSheetSlideUpPerformed;
    private boolean mSheetSlideDownPerformed;

    private String clientToken=null;
    private List<Map<String, Object>> paymentMethodList=new ArrayList<Map<String, Object>>();

    long productId=0;
    float price=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_method);

        mBottomSheet = findViewById(R.id.ll_dropin_bottom_sheet);
        mLoadingViewSwitcher = findViewById(R.id.vs_loading_view);
        mSupportedPaymentMethodsHeader = findViewById(R.id.tv_supported_payment_methods_header);
        mSupportedPaymentMethodListView = findViewById(R.id.lv_supported_payment_methods);
        productId=this.getIntent().getLongExtra("productId",0);
        price=this.getIntent().getFloatExtra("price",0);
        if (savedInstanceState != null) {
            mSheetSlideUpPerformed = savedInstanceState.getBoolean("SLIDE_UP",false);
        }
        //向上弹出支付方式选择窗口
        slideUp();
        mLoadingViewSwitcher.setDisplayedChild(0);
        getClientToken();
        showSupportedPaymentMethods();
    }

    private void getClientToken(){
        //获取Paypal的Braintree支付的CLient Token
        UserModel.getInstance().getPaypalClientToken(new OperationListener<String>() {
            @Override
            public void done(String token, AppException exception) {
                if(exception==null) {
                    clientToken=token;
                     try{
                        mBraintreeFragment = BraintreeFragment.newInstance(PaymentMethodsActivity.this, clientToken);
                         mLoadingViewSwitcher.setDisplayedChild(1);
                    } catch (InvalidArgumentException e) {
                        onError(e);
                    }
                }
                else{
                    toast("Get client token failed.");
                    finish();
                }
            }
        });
    }

    private void showSupportedPaymentMethods() {
        Map<String, Object> listItem1 = new HashMap<String, Object>();
        listItem1.put("icon", com.braintreepayments.api.dropin.R.drawable.bt_ic_paypal);
        listItem1.put("name", getResources().getString(com.braintreepayments.api.dropin.R.string.bt_descriptor_paypal));
        paymentMethodList.add(listItem1);
        Map<String, Object> listItem2 = new HashMap<String, Object>();
        listItem2.put("icon", com.braintreepayments.api.dropin.R.drawable.bt_ic_unknown);
        listItem2.put("name", getResources().getString(com.braintreepayments.api.dropin.R.string.bt_descriptor_unknown));
        paymentMethodList.add(listItem2);
        SimpleAdapter simpleAdapter=new SimpleAdapter(this,paymentMethodList,
                com.braintreepayments.api.dropin.R.layout.bt_payment_method_list_item,
                new String[]{"icon","name"},new int[]{com.braintreepayments.api.dropin.R.id.bt_payment_method_icon,com.braintreepayments.api.dropin.R.id.bt_payment_method_type});
        mSupportedPaymentMethodListView.setAdapter(simpleAdapter);
        mSupportedPaymentMethodListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mLoadingViewSwitcher.setDisplayedChild(0);
                switch (position) {
                    case 0:
                        try {
                            PayPal.requestOneTimePayment(mBraintreeFragment, getPayPalRequest(price + ""));
                        }
                        catch (Exception e){
                            setResult(RESULT_FIRST_USER, new Intent().putExtra("EXTRA_ERROR", e));
                            finish();
                        }
                        break;
                    case 1:
                        Intent intent = new Intent(PaymentMethodsActivity.this, AddCardActivity.class);
                        intent.putExtra(DropInRequest.EXTRA_CHECKOUT_REQUEST, "");
                        startActivityForResult(intent, 1);
                        break;
                }
                mLoadingViewSwitcher.setDisplayedChild(1);
            }
        });

    }

    @Override
    public void onCancel(int requestCode) {
        mLoadingViewSwitcher.setDisplayedChild(1);
    }

    @Override
    public void onError(final Exception error) {
        slideDown(new AnimationFinishedListener() {
            @Override
            public void onAnimationFinished() {
                setResult(RESULT_FIRST_USER, new Intent().putExtra("EXTRA_ERROR", error));
                finish();
            }
        });
    }

    @Override
    public void onPaymentMethodNonceCreated(final PaymentMethodNonce paymentMethodNonce) {
        mLoadingViewSwitcher.setDisplayedChild(0);
        slideDown(new AnimationFinishedListener() {
            @Override
            public void onAnimationFinished() {
                mBraintreeFragment.sendAnalyticsEvent("sdk.exit.success");

                Intent intent = new Intent();
                intent.putExtra("NONCE", paymentMethodNonce);
                intent.putExtra("productId",productId);
                intent.putExtra("price",price);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }



    private PayPalRequest getPayPalRequest(@Nullable String amount) {
        PayPalRequest request = new PayPalRequest(amount);
        request.currencyCode("HKD");
        request.displayName(amount+"HKD");
        request.intent(PayPalRequest.INTENT_AUTHORIZE);
        return request;
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("SLIDE_UP", mSheetSlideUpPerformed);
    }


    public void onBackgroundClicked(View v) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        if (!mSheetSlideDownPerformed) {
            mSheetSlideDownPerformed = true;

            slideDown(new AnimationFinishedListener() {
                @Override
                public void onAnimationFinished() {
                    finish();
                }
            });
        }
    }

    private void slideUp() {
        if (!mSheetSlideUpPerformed) {
            //mBraintreeFragment.sendAnalyticsEvent("appeared");

            mSheetSlideUpPerformed = true;
            mBottomSheet.startAnimation(loadAnimation(this, R.anim.bt_slide_in_up));
        }
    }

    private void slideDown(final AnimationFinishedListener listener) {
        Animation slideOutAnimation = loadAnimation(this, R.anim.bt_slide_out_down);
        slideOutAnimation.setFillAfter(true);
        if (listener != null) {
            slideOutAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}

                @Override
                public void onAnimationEnd(Animation animation) {
                    listener.onAnimationFinished();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
        mBottomSheet.startAnimation(slideOutAnimation);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
