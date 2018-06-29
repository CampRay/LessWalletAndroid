package com.campray.lesswalletandroid.db.entity;

/**
 * Coupon显示样式属性
 * Created by Phills on 12/1/2017.
 */

public class CouponStyle {
    private String benefit;

    private String bgColor="#137656";

    private String shadingUrl;

    private String pictureUrl;

    private String logoUrl;

    private int cardLevel=0;

    public String getBenefit() {
        return benefit;
    }

    public void setBenefit(String benefit) {
        this.benefit = benefit;
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
}
