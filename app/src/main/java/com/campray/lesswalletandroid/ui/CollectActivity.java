package com.campray.lesswalletandroid.ui;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.campray.lesswalletandroid.LessWalletApplication;
import com.campray.lesswalletandroid.R;
import com.campray.lesswalletandroid.db.entity.Country;
import com.campray.lesswalletandroid.db.entity.Currency;
import com.campray.lesswalletandroid.db.entity.User;
import com.campray.lesswalletandroid.listener.OperationListener;
import com.campray.lesswalletandroid.model.CountryModel;
import com.campray.lesswalletandroid.model.CurrencyModel;
import com.campray.lesswalletandroid.model.UserModel;
import com.campray.lesswalletandroid.qrcode.encode.QRCodeEncoder;
import com.campray.lesswalletandroid.ui.base.MenuActivity;
import com.campray.lesswalletandroid.ui.base.ParentWithNaviActivity;
import com.campray.lesswalletandroid.util.AppException;
import com.campray.lesswalletandroid.util.ImageUtil;
import com.campray.lesswalletandroid.util.ResourcesUtils;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 二维码收款页面
 * @author :Phills
 * @project:RegisterActivity
 * @date :2018-04-25-18:23
 */
public class CollectActivity extends MenuActivity {
    @BindView(R.id.iv_qrcode)
    ImageView iv_qrcode;
    @BindView(R.id.tv_amount)
    TextView tv_amount;
    @BindView(R.id.tv_set)
    TextView tv_set;
    @BindView(R.id.tv_export)
    TextView tv_export;

    @BindView(R.id.rl_setting)
    RelativeLayout rl_setting;

    @BindView(R.id.et_amount)
    EditText et_amount;
    @BindView(R.id.sp_currency)
    Spinner sp_currency;

    //生成的Qrcode图片
    private Bitmap qrCodeBitmap=null;

    //货币选择框显示数据集合
    private List<String> displayDataList=new ArrayList<String>();
    private List<Currency> currencyList=new ArrayList<Currency>();
    //设置货币下拉列表数据源
    private void setCurrencyDisplayData() {
        currencyList= CurrencyModel.getInstance().getAllCurrencies();
        if(currencyList==null) {
            currencyList.add(new Currency(1L,"US Dollar","USD","1","US$",1));

        }
        for (Currency bean : currencyList) {
            displayDataList.add(bean.getName());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);
        try {
            String collectStr="collect:"+ LessWalletApplication.INSTANCE().getAccount().getId();
            qrCodeBitmap= QRCodeEncoder.encodeAsBitmap(collectStr, BarcodeFormat.QR_CODE,200);
            iv_qrcode.setImageBitmap(qrCodeBitmap);
        } catch (WriterException e) {
            toast("Failed to create QR Code.");
        }

        //设置货币列表
        setCurrencyDisplayData();
        // 声明一个ArrayAdapter用于存放简单数据
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.item_spinner_text,
                displayDataList);
        sp_currency.setAdapter(adapter);
        Currency userCurrency=LessWalletApplication.INSTANCE().getAccount().getCurrenty();
        if(userCurrency!=null) {
            //设置默认使用的货币
            for (int i = 0; i < currencyList.size(); i++) {
                Currency item = currencyList.get(i);
                if (userCurrency.getCurrencyCode().equals(item.getCurrencyCode())) {
                    sp_currency.setSelection(i);
                    break;
                }
            }
        }

    }

    /**
     * 点击打开设置收款金额按钮
     * @param view
     */
    @OnClick(R.id.tv_set)
    public void onSetClick(View view){
        rl_setting.setVisibility(View.VISIBLE);
    }

    /**
     * 点击打开导出二维码按钮
     * @param view
     */
    @OnClick(R.id.tv_export)
    public void onExportClick(View view){
        Uri uri=ImageUtil.saveBmp2Gallery(qrCodeBitmap,"QRCode_Collect_"+et_amount.getText().toString()+"_"+LessWalletApplication.INSTANCE().getAccount().getId()+".jpg");
        if(uri==null){
            toast(getResources().getString(ResourcesUtils.getStringId(this.getApplicationContext(),"collect_export_failed")));
        }
        else{
            toast(getResources().getString(ResourcesUtils.getStringId(this.getApplicationContext(),"collect_export_success")));
        }
    }

    /**
     * 点击设置收款金额对话框的确认按钮
     * @param view
     */
    @OnClick(R.id.btn_set)
    public void onConfirmClick(View view){
        String amount=et_amount.getText().toString();
        String regex = "^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){1,2})?$";
        Pattern p = Pattern.compile(regex);
        if (TextUtils.isEmpty(amount)) {
            toast(getResources().getString(ResourcesUtils.getStringId(this.getApplicationContext(),"error_empty_amount")));
        }
        else if(!p.matcher(amount).find()){//判断金额是否匹配
            toast(getResources().getString(ResourcesUtils.getStringId(this.getApplicationContext(),"error_format_amount")));
        }
        else{
            try {
                int selIndex=sp_currency.getSelectedItemPosition();
                Currency currency=currencyList.get(selIndex);
                String currencyCode=currency.getCurrencyCode();
                String collectStr="collect:"+ LessWalletApplication.INSTANCE().getAccount().getId()+":"+ currencyCode+":"+amount;
                qrCodeBitmap= QRCodeEncoder.encodeAsBitmap(collectStr, BarcodeFormat.QR_CODE,200);
                iv_qrcode.setImageBitmap(qrCodeBitmap);
                tv_amount.setText(currency.getCustomFormatting()+amount);
                tv_amount.setVisibility(View.VISIBLE);
            } catch (WriterException e) {
                toast("Failed to create QR Code.");
            }
        }
        rl_setting.setVisibility(View.GONE);
    }

    /**
     * 点击取消按钮
     * @param view
     */
    @OnClick(R.id.btn_cancel)
    public void onCancelClick(View view){
        rl_setting.setVisibility(View.GONE);
        et_amount.setText("");
    }
}
