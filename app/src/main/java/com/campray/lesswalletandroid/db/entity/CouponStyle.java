package com.campray.lesswalletandroid.db.entity;

import android.text.TextUtils;

import com.campray.lesswalletandroid.model.CurrencyModel;

/**
 * Coupon显示样式属性
 * Created by Phills on 12/1/2017.
 */

public class CouponStyle {
    private String benefitCustomized;

    private String benefitCash;

    private String benefitDiscount;

    private String benefitFree;


    private String bgColor="#00000000";

    private String shadingUrl;

    private String pictureUrl;

    private String logoUrl;

    //会员级别：数字越高，级别越高
    private int cardLevel=0;

    private int validityDay=0;
    private int validityMonth=0;
    private int validityYear=0;

    public String getBenefitCustomized() {
        return benefitCustomized;
    }

    public void setBenefitCustomized(String benefitCustomized) {
        this.benefitCustomized = benefitCustomized;
    }

    public String getBenefitCash() {
        if(!TextUtils.isEmpty(benefitCash)) {
            Currency currency = CurrencyModel.getInstance().getDefaultCurrency();
            String fmtStr = currency.getCustomFormatting().replace("0.00", "");
            return fmtStr+benefitCash;
        }
        return "";
    }

    public void setBenefitCash(String benefitCash) {
        this.benefitCash = benefitCash;
    }

    public String getBenefitDiscount() {
        return benefitDiscount;
    }

    public void setBenefitDiscount(String benefitDiscount) {
        this.benefitDiscount = benefitDiscount;
    }

    public String getBenefitFree() {
        return benefitFree;
    }

    public void setBenefitFree(String benefitFree) {
        this.benefitFree = benefitFree;
    }

    public String getBgColor() {
        return bgColor;
    }

    public void setBgColor(String bgColor) {
        this.bgColor = bgColor;
    }

    public String getShadingUrl() {
        return shadingUrl;
    }

    public void setShadingUrl(String shadingUrl) {
        this.shadingUrl = shadingUrl;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public int getCardLevel() {
        return cardLevel;
    }

    public void setCardLevel(int cardLevel) {
        this.cardLevel = cardLevel;
    }

    public int getValidityDay() {
        return validityDay;
    }

    public void setValidityDay(int validityDay) {
        this.validityDay = validityDay;
    }

    public int getValidityMonth() {
        return validityMonth;
    }

    public void setValidityMonth(int validityMonth) {
        this.validityMonth = validityMonth;
    }

    public int getValidityYear() {
        return validityYear;
    }

    public void setValidityYear(int validityYear) {
        this.validityYear = validityYear;
    }
}
