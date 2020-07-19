package com.campray.lesswalletandroid.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.campray.lesswalletandroid.LessWalletApplication;
import com.campray.lesswalletandroid.R;
import com.campray.lesswalletandroid.db.entity.Coupon;
import com.campray.lesswalletandroid.db.entity.CouponStyle;
import com.campray.lesswalletandroid.db.entity.Product;
import com.campray.lesswalletandroid.db.entity.SpecAttr;
import com.campray.lesswalletandroid.db.entity.UserAttr;
import com.campray.lesswalletandroid.db.entity.UserAttrValue;
import com.campray.lesswalletandroid.listener.OperationListener;
import com.campray.lesswalletandroid.model.CouponModel;
import com.campray.lesswalletandroid.model.ProductModel;
import com.campray.lesswalletandroid.model.UserModel;
import com.campray.lesswalletandroid.ui.base.MenuActivity;
import com.campray.lesswalletandroid.util.AppException;
import com.campray.lesswalletandroid.util.BasicTimesUtils;
import com.campray.lesswalletandroid.util.ImageUtil;
import com.campray.lesswalletandroid.util.ResourcesUtils;
import com.campray.lesswalletandroid.view.InnerCornerView;
import com.google.android.gms.wallet.TransactionInfo;
import com.google.android.gms.wallet.WalletConstants;
import com.google.gson.JsonObject;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 扫描卡卷二维码后确认或支付页面
 * Created by Phills on 11/2/2017.
 */

public class TicketPaymentActivity extends MenuActivity {
    //Coupon控件

    @BindView(R.id.iv_ticket_img)
    ImageView iv_ticket_img;
    @BindView(R.id.tv_price)
    TextView tv_price;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_shortdesc)
    TextView tv_shortdesc;
    @BindView(R.id.tv_merchant)
    TextView tv_merchant;
    @BindView(R.id.tv_expired)
    TextView tv_expired;
    @BindView(R.id.tv_stock)
    TextView tv_stock;

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
    @BindView(R.id.ll_form)
    LinearLayout ll_form;

    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_gender)
    TextView tv_gender;
    @BindView(R.id.tv_phone)
    TextView tv_phone;
    @BindView(R.id.tv_idnumber)
    TextView tv_idnumber;
    @BindView(R.id.tv_email)
    TextView tv_email;
    @BindView(R.id.tv_birthday)
    TextView tv_birthday;
    @BindView(R.id.tv_address)
    TextView tv_address;
    @BindView(R.id.tv_datetime)
    TextView tv_datetime;
    @BindView(R.id.tv_date)
    TextView tv_date;
    @BindView(R.id.tv_time)
    TextView tv_time;
    @BindView(R.id.tv_seat)
    TextView tv_seat;

    @BindView(R.id.et_quantity)
    EditText et_quantity;
    @BindView(R.id.et_realname)
    EditText et_realname;
    @BindView(R.id.rg_gender)
    RadioGroup rg_gender;
    @BindView(R.id.et_phone)
    EditText et_phone;
    @BindView(R.id.et_idnumber)
    EditText et_idnumber;
    @BindView(R.id.et_email)
    EditText et_email;
    @BindView(R.id.et_birthday)
    TextView et_birthday;


    @BindView(R.id.sp_address)
    Spinner sp_address;
    @BindView(R.id.sp_datetime)
    Spinner sp_datetime;
    @BindView(R.id.sp_date)
    Spinner sp_date;
    @BindView(R.id.sp_time)
    Spinner sp_time;
    @BindView(R.id.sp_seat)
    TextView sp_seat;

    @BindView(R.id.ll_quantity)
    LinearLayout ll_quantity;
    @BindView(R.id.ll_username)
    LinearLayout ll_username;
    @BindView(R.id.ll_gender)
    LinearLayout ll_gender;
    @BindView(R.id.ll_phone)
    LinearLayout ll_phone;
    @BindView(R.id.ll_idnumber)
    LinearLayout ll_idnumber;
    @BindView(R.id.ll_email)
    LinearLayout ll_email;
    @BindView(R.id.ll_birthday)
    LinearLayout ll_birthday;
    @BindView(R.id.ll_address)
    LinearLayout ll_address;
    @BindView(R.id.ll_datetime)
    LinearLayout ll_datetime;
    @BindView(R.id.ll_date)
    LinearLayout ll_date;
    @BindView(R.id.ll_time)
    LinearLayout ll_time;
    @BindView(R.id.ll_seat)
    LinearLayout ll_seat;

    @BindView(R.id.gl_dialog)
    GridLayout gl_dialog;
    @BindView(R.id.btn_yes)
    TextView btn_yes;
    @BindView(R.id.btn_no)
    TextView btn_no;

    private Product curProduct=null;
    private String shadingUrl = null;
    private String customPicUrl = null;

    private String clientToken=null;
    private static final int DROP_IN_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_payment);
        long pid = this.getBundle().getLong("pid");
        //根据扫码获取的ID，从服务器获取对应的电子卡卷信息
        getTicketFromServer(pid);
    }

    /**
     * 从服务器获取下载Coupon
     * @param pid
     */
    private void getTicketFromServer(long pid){
        ProductModel.getInstance().getProductFromServer(pid, new OperationListener<Product>() {
            @Override
            public void done(Product product, AppException exe) {
                if (exe == null) {
                    curProduct=product;
                    showTicket(product);
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
     * 根据获取的电子票信息，显示电子票图像
     * @param product
     */
    private void showTicket(final Product product){
        CouponStyle couponStyle=product.getCouponStyle();
        if(couponStyle!=null){
            //设置背景色
            int topColor=Color.parseColor(couponStyle.getBgColor());
            try {
                LayerDrawable drawable = (LayerDrawable) ll_top_layout.getBackground();
                GradientDrawable item = (GradientDrawable)drawable.findDrawableByLayerId(R.id.top_item);
                item.setColor(topColor);
                icv_tleft.setCornerColor(topColor);
                icv_tright.setCornerColor(topColor);
                tv_top_bg.setBackgroundColor(topColor);
            }
            catch (Exception e){
                e.printStackTrace();
            }

            if (!TextUtils.isEmpty(couponStyle.getPictureUrl())) {
//                Picasso.with(this).load(couponStyle.getPictureUrl()).memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE).into(iv_ticket_img);
                Picasso.get().load(couponStyle.getPictureUrl()).memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE).into(iv_ticket_img);
            }
            else if (!TextUtils.isEmpty(couponStyle.getLogoUrl())){
//                Picasso.with(this).load(couponStyle.getLogoUrl()).memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE).into(iv_ticket_img);
                Picasso.get().load(couponStyle.getLogoUrl()).memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE).into(iv_ticket_img);
            }
        }

        tv_title.setText(product.getTitle());
        tv_shortdesc.setText(product.getShortDesc());
        if(product.getUsePoint()){
            tv_price.setText(product.getPoints()+getResources().getString(R.string.coupon_point));
        }
        else {
            if (product.getPrice() > 0) {
                tv_price.setText(product.getPriceStr());
            } else {
                tv_price.setText(getResources().getString(R.string.coupon_free));
            }
        }
        tv_merchant.setText(product.getMerchant().getName());
        String timeStr = (TextUtils.isEmpty(product.getStartTimeLocal())?"":product.getStartTimeLocal()) + (TextUtils.isEmpty(product.getEndTimeLocal()) ? " -" : " - " + product.getEndTimeLocal());
        tv_expired.setText(timeStr);
        tv_stock.setText(product.getStockQuantity()+"");


        //处理要显示的用户输入或选择项
        List<UserAttr> userAttr=product.getUserAttr();
        if(userAttr.size()>0) gl_dialog.setVisibility(View.VISIBLE);
        for (UserAttr item:userAttr) {
            final List<UserAttrValue> values=item.getValues();
            int attributeId=(int)item.getProductAttributeId();
            switch (attributeId){
                case 1:
                    ll_username.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    ll_phone.setVisibility(View.VISIBLE);
                    break;
                case 3:
                    ll_idnumber.setVisibility(View.VISIBLE);
                    break;
                case 4:
                    ll_email.setVisibility(View.VISIBLE);
                    break;
                case 5:
                    ll_birthday.setVisibility(View.VISIBLE);
                    break;
                case 6:
                    for(UserAttrValue value:values) {
                        RadioButton radioButton = new RadioButton(this);
                        RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT);
                        radioButton.setLayoutParams(layoutParams);
                        radioButton.setButtonDrawable(R.drawable.radio_button_style);
                        radioButton.setText(value.getName());
                        radioButton.setTag(value.getId());
                        radioButton.setTextSize(12);
                        radioButton.setPadding(3, 0, 0, 0);
                        radioButton.setTextColor(getResources().getColorStateList(R.color.colorPrimary));//设置选中/未选中的文字颜色
                        radioButton.setChecked(value.isPreSelected());
                        rg_gender.addView(radioButton);
                    }
                    ll_gender.setVisibility(View.VISIBLE);
                    break;
                case 7:
                    if(!TextUtils.isEmpty(item.getTextPrompt())){
                        tv_datetime.setText(item.getTextPrompt());
                    }
                    List<String> dateTimeDataList = new ArrayList<String>();
                    for(UserAttrValue value:values) {
                        dateTimeDataList.add(value.getName());
                    }
                    //下拉列表数据的适配器
                    ArrayAdapter<String> datetimeAdapter = new ArrayAdapter<String>(this,R.layout.item_spinner_text_small,dateTimeDataList);
                    datetimeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sp_datetime.setAdapter(datetimeAdapter);
                    ll_datetime.setVisibility(View.VISIBLE);
                    sp_datetime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            String formStr=null;
                            try {
                                formStr = getFormData(false);
                                ProductModel.getInstance().getPriceAndStockFromServer(product.getProductId(), formStr, new OperationListener<JsonObject>() {
                                    @Override
                                    public void done(JsonObject obj, AppException exception) {
                                        if(obj!=null) {
                                            String price = obj.get("price").getAsString();
                                            String stock = obj.get("stock").getAsString();
                                            tv_price.setText(price);
                                            tv_stock.setText(stock);
                                        }
                                    }
                                });
                            }
                            catch (Exception exe){}
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {}
                    });
                    break;
                case 8:
                    if(!TextUtils.isEmpty(item.getTextPrompt())){
                        tv_date.setText(item.getTextPrompt());
                    }
                    List<String> dateDataList = new ArrayList<String>();
                    for(UserAttrValue value:values) {
                        dateDataList.add(value.getName());
                    }
                    //下拉列表数据的适配器
                    ArrayAdapter<String> dateAdapter = new ArrayAdapter<String>(this,R.layout.item_spinner_text_small,dateDataList);
                    dateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sp_date.setAdapter(dateAdapter);
                    ll_date.setVisibility(View.VISIBLE);

                    sp_date.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            String formStr=null;
                            try {
                                formStr = getFormData(false);
                                ProductModel.getInstance().getPriceAndStockFromServer(product.getProductId(), formStr, new OperationListener<JsonObject>() {
                                    @Override
                                    public void done(JsonObject obj, AppException exception) {
                                        if(obj!=null) {
                                            String price = obj.get("price").getAsString();
                                            String stock = obj.get("stock").getAsString();
                                            tv_price.setText(price);
                                            tv_stock.setText(stock);
                                        }
                                    }
                                });
                            }
                            catch (Exception exe){}
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {}
                    });
                    break;
                case 9:
                    if(!TextUtils.isEmpty(item.getTextPrompt())){
                        tv_time.setText(item.getTextPrompt());
                    }
                    List<String> timeDataList = new ArrayList<String>();
                    for(UserAttrValue value:values) {
                        timeDataList.add(value.getName());
                    }
                    //下拉列表数据的适配器
                    ArrayAdapter<String> timeAdapter = new ArrayAdapter<String>(this,R.layout.item_spinner_text_small,timeDataList);
                    timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sp_time.setAdapter(timeAdapter);
                    ll_time.setVisibility(View.VISIBLE);
                    sp_time.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            String formStr=null;
                            try {
                                formStr = getFormData(false);
                                ProductModel.getInstance().getPriceAndStockFromServer(product.getProductId(), formStr, new OperationListener<JsonObject>() {
                                    @Override
                                    public void done(JsonObject obj, AppException exception) {
                                        if(obj!=null) {
                                            String price = obj.get("price").getAsString();
                                            String stock = obj.get("stock").getAsString();
                                            tv_price.setText(price);
                                            tv_stock.setText(stock);
                                        }
                                    }
                                });
                            }
                            catch (Exception exe){}
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {}
                    });
                    break;
                case 10:
                    if(!TextUtils.isEmpty(item.getTextPrompt())){
                        tv_address.setText(item.getTextPrompt());
                    }
                    List<String> addressDataList = new ArrayList<String>();
                    for(UserAttrValue value:values) {
                        addressDataList.add(value.getName());
                    }
                    //下拉列表数据的适配器
                    ArrayAdapter<String> addressAdapter = new ArrayAdapter<String>(this,R.layout.item_spinner_text_small,addressDataList);
                    addressAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sp_address.setAdapter(addressAdapter);
                    ll_address.setVisibility(View.VISIBLE);
                    sp_address.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            String formStr=null;
                            try {
                                formStr = getFormData(false);
                                ProductModel.getInstance().getPriceAndStockFromServer(product.getProductId(), formStr, new OperationListener<JsonObject>() {
                                    @Override
                                    public void done(JsonObject obj, AppException exception) {
                                        String price = obj.get("price").getAsString();
                                        String stock = obj.get("stock").getAsString();
                                        tv_price.setText(price);
                                        tv_stock.setText(stock);
                                    }
                                });
                            }
                            catch (Exception exe){}
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {}
                    });
                    break;
                case 11:
                    if(!TextUtils.isEmpty(item.getTextPrompt())){
                        tv_seat.setText(item.getTextPrompt());
                    }
                    ll_seat.setVisibility(View.VISIBLE);
                    final String[] seatArr = new String[values.size()];
                    for(int i=0;i<values.size();i++) {
                        UserAttrValue value=values.get(i);
                        seatArr[i]=value.getName();
                    }
                    final boolean[] checkedItems = new boolean[values.size()];

                    final AlertDialog.Builder builder = new AlertDialog.Builder(this,AlertDialog.THEME_HOLO_LIGHT);
                    builder.setTitle(R.string.text_select_seat);
                    builder.setMultiChoiceItems(seatArr, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                            checkedItems[which] = isChecked;
                        }
                    });
                    builder.setPositiveButton(R.string.button_confirm, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            StringBuffer sbName=new StringBuffer();
                            StringBuffer sbId=new StringBuffer();
                            for (int i = 0; i < checkedItems.length; i++) {
                                if (checkedItems[i]) {
                                    sbName.append(values.get(i).getName()+",");
                                    sbId.append(values.get(i).getId()+",");
                                }
                            }
                            sbName.deleteCharAt(sbName.length()-1);
                            sbId.deleteCharAt(sbId.length()-1);
                            sp_seat.setText(sbName.toString());
                            sp_seat.setTag(sbId.toString());
                        }
                    });
                    builder.setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {}
                    });

                    sp_seat.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            builder.show();
                        }
                    });

                    break;
                default:

            }
        }
    }

    @OnClick(R.id.ll_top_layout)
    public void onClickForm(){
        if(ll_form.getVisibility()==View.GONE) {
            ll_form.setVisibility(View.VISIBLE);
        }
        else{
            ll_form.setVisibility(View.GONE);
        }
    }
    /**
     * 点击生日按钮
     */
    @OnClick(R.id.et_birthday)
    public void onClickButtonBirthday(){
        BasicTimesUtils.showDatePickerDialog(this,BasicTimesUtils.THEME_DEVICE_DEFAULT_LIGHT, getResources().getString(R.string.text_select_datetime), 2000, 1, 1, new BasicTimesUtils.OnDatePickerListener() {
            @Override
            public void onConfirm(int year, int month, int dayOfMonth) {
                toast(year + "-" + month + "-" + dayOfMonth);
                et_birthday.setText(year + "-" + month + "-" + dayOfMonth);
            }
            @Override
            public void onCancel() {}
        });
    }


    /**
     * 点击Yes按钮确认支付或接收Coupon
     */
    @OnClick(R.id.btn_yes)
    public void onClickButtonYes(){
        btn_yes.setEnabled(false);
        int quantity=Integer.parseInt(et_quantity.getText().toString());
        float total=curProduct.getPrice()*quantity;
        if(curProduct.getPrice()>0) {
            /**
            GooglePaymentRequest googlePaymentRequest = new GooglePaymentRequest()
                    .transactionInfo(TransactionInfo.newBuilder()
                            .setTotalPrice(curProduct.getPrice()+"")
                            .setTotalPriceStatus(WalletConstants.TOTAL_PRICE_STATUS_FINAL)
                            .setCurrencyCode(curProduct.getCurrencyCode())
                            .build()).billingAddressRequired(true);
            //Android Pay
            Cart cart = Cart.newBuilder()
                    .setCurrencyCode(curProduct.getCurrencyCode())
                    .setTotalPrice(total+"")
                    .addLineItem(LineItem.newBuilder()
                            .setCurrencyCode(curProduct.getCurrencyCode())
                            .setDescription(curProduct.getTitle())
                            .setQuantity(quantity+"")
                            .setUnitPrice(curProduct.getPrice()+"")
                            .setTotalPrice(total+"")
                            .build())
                    .build();

            DropInRequest dropInRequest = new DropInRequest().clientToken(clientToken).amount(total+"")
                    .googlePaymentRequest(googlePaymentRequest)
                    .requestThreeDSecureVerification(false)
                    .androidPayCart(cart).androidPayShippingAddressRequired(false).androidPayPhoneNumberRequired(false);
            startActivityForResult(dropInRequest.getIntent(this), DROP_IN_REQUEST);
            **/
//            Intent intent=new Intent(this, PaymentMethodsActivity.class);
//            intent.putExtra("productId",curProduct.getProductId());
//            intent.putExtra("price",curProduct.getPrice());
//            startActivityForResult(intent, DROP_IN_REQUEST);
        }else{

            String formStr=null;
            try{
                formStr=getFormData(true);
                CouponModel.getInstance().confirmCoupon(curProduct.getProductId(),quantity,formStr, new OperationListener<Coupon>() {
                    @Override
                    public void done(Coupon coupon, AppException exception) {
                        if (exception == null) {
                            saveCouponImage(coupon);
                            toast("Ticket已经成功收入您的钱包中!");
                            startActivity(MainActivity.class,null,true);
                        } else {
                            String errcode=exception.getErrorCode();
                            if(errcode.startsWith("E_")) {
                                toast("获取Ticket失败，错误原因: " + getResources().getString(ResourcesUtils.getStringId(getApplicationContext(), errcode)));
                            }
                            else{
                                toast("获取Ticket失败，错误原因: " + errcode);
                            }
                        }
                    }
                });
            }
            catch (Exception ae){
                toast(ae.getMessage());

            }
        }
        btn_yes.setEnabled(true);

    }

    @OnClick(R.id.btn_no)
    public void onClickButtonNo(){
        btn_no.setEnabled(false);
        ProductModel.getInstance().deleteProductById(curProduct.getProductId());
        startActivity(MainActivity.class,null,true);
    }

    /**
     * 获取用户填写或选择的表单数据，并生成json字符串
     * @param isFinal 是否是最终的表单数据结果
     * @return
     */
    private String getFormData(boolean isFinal) throws AppException{
        JsonObject jobj=new JsonObject();
        //处理要显示的用户输入或选择项
        List<UserAttr> userAttr=curProduct.getUserAttr();
        for (UserAttr item:userAttr) {
            int attributeId=(int)item.getProductAttributeId();
            switch (attributeId){
                case 1:
                    String realname = et_realname.getText().toString();
                    if (isFinal&&item.isRequired() && TextUtils.isEmpty(realname)) {
                        //toast("请填写您的真实姓名");
                        throw new AppException("E_3010",getResources().getString(R.string.E_3010));
                    }
                    else{
                        jobj.addProperty("product_attribute_"+item.getId(),realname);
                    }
                    break;
                case 2:
                    String phone = et_phone.getText().toString();
                    if (isFinal&&item.isRequired() && TextUtils.isEmpty(phone)) {
                        //toast("请填写您的联系电话");
                        throw new AppException("E_3011",getResources().getString(R.string.E_3011));
                    }
                    else{
                        jobj.addProperty("product_attribute_"+item.getId(),phone);
                    }
                    break;
                case 3:
                    String idnumber = et_idnumber.getText().toString();
                    if (isFinal&&item.isRequired() && TextUtils.isEmpty(idnumber)) {
                        //toast("请填写您的身份证号码");
                        throw new AppException("E_3012",getResources().getString(R.string.E_3012));
                    }
                    else{
                        jobj.addProperty("product_attribute_"+item.getId(),idnumber);
                    }
                    break;
                case 4:
                    String email = et_email.getText().toString();
                    if (isFinal&&item.isRequired() && TextUtils.isEmpty(email)) {
                        //toast("请填写您的电子邮箱地址");
                        throw new AppException("E_3013",getResources().getString(R.string.E_3013));
                    }
                    else{
                        jobj.addProperty("product_attribute_"+item.getId(),email);
                    }
                    break;
                case 5:
                    String birthday = et_birthday.getText().toString();
                    if (isFinal&&item.isRequired() && TextUtils.isEmpty(birthday)) {
                        //toast("请填写您的出生日期");
                        throw new AppException("E_3014",getResources().getString(R.string.E_3014));
                    }
                    else{
                        jobj.addProperty("product_attribute_"+item.getId(),birthday);
                    }
                    break;
                case 6:
                    if (isFinal&&item.isRequired() && rg_gender.getCheckedRadioButtonId()==-1) {
                        //toast("请选择您的性别");
                        throw new AppException("E_3015",getResources().getString(R.string.E_3015));
                    }
                    else{
                        RadioButton choise = (RadioButton) findViewById( rg_gender.getCheckedRadioButtonId());
                        jobj.addProperty("product_attribute_"+item.getId(),(long)choise.getTag());
                    }
                    break;
                case 7:
                    if (isFinal&&item.isRequired() && sp_datetime.getSelectedItem()==null) {
                        //toast("请选择日期时间");
                        throw new AppException("E_3016",getResources().getString(R.string.E_3016));
                    }
                    else{
                        jobj.addProperty("product_attribute_"+item.getId(),item.getValues().get(sp_datetime.getSelectedItemPosition()).getId());
                    }
                    break;
                case 8:
                    if (isFinal&&item.isRequired() && sp_date.getSelectedItem()==null) {
                        //toast("请选择日期以及开始时间");
                        throw new AppException("E_3019",getResources().getString(R.string.E_3019));
                    }
                    else{
                        jobj.addProperty("product_attribute_"+item.getId(),item.getValues().get(sp_date.getSelectedItemPosition()).getId());
                    }
                    break;
                case 9:
                    if (isFinal&&item.isRequired() && sp_time.getSelectedItem()==null) {
                        //toast("请选择日期以及开始时间");
                        throw new AppException("E_3019",getResources().getString(R.string.E_3019));
                    }
                    else{
                        jobj.addProperty("product_attribute_"+item.getId(),item.getValues().get(sp_time.getSelectedItemPosition()).getId());
                    }
                    break;
                case 10:
                    if (isFinal&&item.isRequired() && sp_address.getSelectedItem()==null) {
                        //toast("请选择地点");
                        throw new AppException("E_3017",getResources().getString(R.string.E_3017));
                    }
                    else{
                        jobj.addProperty("product_attribute_"+item.getId(),item.getValues().get(sp_address.getSelectedItemPosition()).getId());
                    }
                    break;
                case 11:
                    String idList=(String)sp_seat.getTag();
                    String[] idArr=idList.split(",");
                    int quantity=Integer.parseInt(et_quantity.getText().toString());
                    if (isFinal&&item.isRequired() && TextUtils.isEmpty(sp_seat.getText())) {
                        //toast("请选择座位");
                        throw new AppException("E_3018",getResources().getString(R.string.E_3018));
                    }
                    else if(idArr.length!=quantity){
                        //toast("您选择的座位数量要和购票数量一致");
                        throw new AppException("E_3009",getResources().getString(R.string.E_3009));
                    }
                    else{
                        jobj.addProperty("product_attribute_"+item.getId(),idList);
                    }
                    break;
                default:

            }
        }
        return jobj.toString();
    }

    /**
     * 支付处理结果
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == DROP_IN_REQUEST) {
                /**
                //使用DropIn时的处理
                DropInResult result = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
                PaymentMethodNonce paymentMethodNonce =result.getPaymentMethodNonce();
                long productId=curProduct.getProductId();
//                PaymentMethodNonce paymentMethodNonce = data.getParcelableExtra("NONCE");
//                long productId=data.getLongExtra("productId",0);
                if(productId>0) {
                    int quantity=Integer.parseInt(et_quantity.getText().toString());
                    String formStr=null;
                    try {
                        formStr = getFormData(true);
                        //支付Coupon
                        CouponModel.getInstance().paypalCoupon(productId, paymentMethodNonce.getNonce(),quantity,formStr, new OperationListener<Coupon>() {
                            @Override
                            public void done(Coupon coupon, AppException exception) {
                                if (exception == null) {
                                    saveCouponImage(coupon);
                                    toast("Ticket已经成功收入您的钱包中!");
                                    finish();
                                } else {
                                    btn_yes.setEnabled(true);
                                    int errorId = ResourcesUtils.getStringId(getApplicationContext(), exception.getErrorCode());
                                    if (errorId > 0) {
                                        toast("获取Ticket失败，错误原因: " + getResources().getString(errorId));
                                    } else {
                                        toast("获取Ticket失败，错误原因: " + exception.getErrorCode());
                                    }
                                }
                            }
                        });
                    }
                    catch (Exception ae){
                        toast(ae.getMessage());

                    }
                }
                else{
                    toast("支付异常，错误原因:支付商品ID不匹配! ");
                }
                 **/
            }

        }
        else if (resultCode != RESULT_CANCELED) {
            /**
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
             **/
        }
    }

    /**
     * 保存处理Ticket图片
     * @param coupon
     */
    private void saveCouponImage(Coupon coupon){
        boolean needUpdate = false;
        Product productBean = coupon.getProduct();
        List<SpecAttr> specAttrBeanList = productBean.getSpecAttr();
        for (SpecAttr specAttrBean : specAttrBeanList) {
            if (specAttrBean.getSpecificationAttributeId() == 7) {//如果是自定义图片
                if (TextUtils.isEmpty(specAttrBean.getFileUrl())) {
                    customPicUrl=specAttrBean.getValueRaw();
                    String[] strArr = customPicUrl.split("\\.");
                    iv_ticket_img.setDrawingCacheEnabled(true);
                    File folder= LessWalletApplication.INSTANCE().getPrivateDir();
                    File picFile=new File(folder,"p_"+productBean.getProductId()+"_"+specAttrBean.getSpecificationAttributeId());
                    File imageFile = ImageUtil.saveImage(iv_ticket_img.getDrawingCache(),picFile, strArr[strArr.length - 1]);
                    specAttrBean.setFileUrl(Uri.fromFile(imageFile).toString());
                    iv_ticket_img.setDrawingCacheEnabled(false);
                    iv_ticket_img.destroyDrawingCache();
                    needUpdate = true;
                }
            }
        }
        if (needUpdate) {
            ProductModel.getInstance().updaeProduct(productBean);
        }
    }


}
