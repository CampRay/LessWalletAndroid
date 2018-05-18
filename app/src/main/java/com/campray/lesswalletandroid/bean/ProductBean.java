package com.campray.lesswalletandroid.bean;

import android.text.TextUtils;

import com.campray.lesswalletandroid.db.entity.Product;
import com.campray.lesswalletandroid.db.entity.SpecAttr;
import com.campray.lesswalletandroid.util.TimeUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by Phills on 10/24/2017.
 */
public class ProductBean {

    private long productId;//对应的商品ID
    //卡卷类型：1 coupon; 2 Ticket; 3 Card;
    private int productTypeId;
    private int productTemplateId;//卡卷样式模板ID
    private String title; //显示标题
    private String shortDesc; //快速描述
    private String fullDesc;  //详细描述
    private String agreement;//卡卷使用协议
    private float price;//购买金额
    private long currencyId ;//商品货币ID
    private CurrencyBean currency ;//货币对象
    private String numPrefix;//卡卷编号前缀
    private String startTime;//生效时间
    private String endTime;//过期时间
    private String startTimeLocal;//生效时间-显示用户时区
    private String endTimeLocal;//过期时间-显示用户时区
    private long merchantId; //商家ID
    private MerchantBean merchant;
//    private String specAttrStr;
    private List<SpecAttrBean> specAttrBeanList;
    private boolean published;//是否已发布
    private boolean deleted;//是否已删除

    public ProductBean fromProduct(Product product){
        if(product!=null) {
            startTime = product.getStartTime();
            endTime = product.getEndTime();
            try {
                if (!TextUtils.isEmpty(startTime)) {
                    startTimeLocal = TimeUtil.dateToString(TimeUtil.stringToDate(startTime, TimeUtil.FORMAT_DATE_TIME_SECOND, TimeZone.getTimeZone("UTC")), TimeUtil.FORMAT_DATE);
                }
                if (!TextUtils.isEmpty(endTime)) {
                    endTimeLocal = TimeUtil.dateToString(TimeUtil.stringToDate(endTime, TimeUtil.FORMAT_DATE_TIME_SECOND, TimeZone.getTimeZone("UTC")), TimeUtil.FORMAT_DATE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            productId = product.getProductId();
            productTypeId = product.getProductTypeId();
            title = product.getTitle();
            shortDesc = product.getShortDesc();
            fullDesc = product.getFullDesc();
            agreement = product.getAgreement();
            price = product.getPrice();
            List<SpecAttr> specAttrList=product.getSpecAttr();
            if(specAttrList!=null){
                specAttrBeanList =new ArrayList<SpecAttrBean>();
                for (SpecAttr item:specAttrList) {
                    specAttrBeanList.add(new SpecAttrBean().fromSpecAttr(item));
                }
            }
            merchantId = product.getMerchantId();
            numPrefix = product.getNumPrefix();
            published=product.getPublished();
            deleted=product.getDeleted();

        }
        return this;
    }

    public Product toProduct(){
        if(productId==0){
            return null;
        }
        Product product=new Product();
        product.setProductId(productId);
        product.setProductTypeId(productTypeId);
        product.setProductTemplateId(productTemplateId);
        product.setTitle(title);
        product.setShortDesc(shortDesc);
        product.setFullDesc(fullDesc);
        product.setAgreement(agreement);
        product.setPrice(price);
        product.setMerchantId(merchantId);
        product.setNumPrefix(numPrefix);
        product.setStartTime(startTime);
        product.setEndTime(endTime);
        product.setPublished(published);
        product.setDeleted(deleted);
        if(specAttrBeanList!=null){
            List<SpecAttr> specAttrList =new ArrayList<SpecAttr>();
            for (SpecAttrBean item:specAttrBeanList) {
                specAttrList.add(item.toSpecAttr());
            }
            product.setSpecAttr(specAttrList);
        }
        return product;
    }

    public ProductBean fromProduct(Product product,boolean related){
        if(product!=null) {
            this.fromProduct(product);
            merchant = new MerchantBean().fromMerchant(product.getMerchant());
        }
        return this;
    }


    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public int getProductTypeId() {
        return productTypeId;
    }

    public void setProductTypeId(int productTypeId) {
        this.productTypeId = productTypeId;
    }

    public int getProductTemplateId() {
        return productTemplateId;
    }

    public void setProductTemplateId(int productTemplateId) {
        this.productTemplateId = productTemplateId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getFullDesc() {
        return fullDesc;
    }

    public void setFullDesc(String fullDesc) {
        this.fullDesc = fullDesc;
    }

    public String getAgreement() {
        return agreement;
    }

    public void setAgreement(String agreement) {
        this.agreement = agreement;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public CurrencyBean getCurrency() {
        return currency;
    }
    public void setCurrency(CurrencyBean currency) {
        this.currency = currency;
    }

    public String getNumPrefix() {
        return numPrefix;
    }

    public void setNumPrefix(String numPrefix) {
        this.numPrefix = numPrefix;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public MerchantBean getMerchant() {
        return merchant;
    }

    public void setMerchant(MerchantBean merchant) {
        this.merchant = merchant;
    }

    public long getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(long currencyId) {
        this.currencyId = currencyId;
    }

    public long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(long merchantId) {
        this.merchantId = merchantId;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public List<SpecAttrBean> getSpecAttrBeanList() {
        return specAttrBeanList;
    }

    public void setSpecAttrBeanList(List<SpecAttrBean> specAttrBeanList) {
        this.specAttrBeanList = specAttrBeanList;
    }

    public String getStartTimeLocal() {
        return startTimeLocal;
    }

    public void setStartTimeLocal(String startTimeLocal) {
        this.startTimeLocal = startTimeLocal;
    }

    public String getEndTimeLocal() {
        return endTimeLocal;
    }

    public void setEndTimeLocal(String endTimeLocal) {
        this.endTimeLocal = endTimeLocal;
    }
}
