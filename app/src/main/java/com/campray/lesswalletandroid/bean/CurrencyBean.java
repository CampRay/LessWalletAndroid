package com.campray.lesswalletandroid.bean;

import com.campray.lesswalletandroid.db.entity.Currency;

/**
 * Created by Phills on 10/25/2017.
 */
public class CurrencyBean {
    private long id;
    private String name;
    private String currencyCode;
    private String rate;
    private String customFormatting;
    private int displayOrder;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getCustomFormatting() {
        return customFormatting;
    }

    public void setCustomFormatting(String customFormatting) {
        this.customFormatting = customFormatting;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    public CurrencyBean fromCurrency(Currency currency){
        if(currency!=null) {
            id = currency.getId();
            name = currency.getName();
            currencyCode = currency.getCurrencyCode();
            rate = currency.getRate();
            customFormatting = currency.getCustomFormatting();
            displayOrder = currency.getDisplayOrder();
        }
        return this;
    }
}
