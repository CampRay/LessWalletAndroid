package com.campray.lesswalletandroid.db.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 网站发布的货币
 * Created by Phills on 10/25/2017.
 */
@Entity
public class Currency {
    @Id(autoincrement = false)
    private Long id;
    @NotNull
    private String name;
    @NotNull
    private String currencyCode;
    @NotNull
    private String rate;
    @NotNull
    private String customFormatting;
    @NotNull
    private int displayOrder;
    @NotNull
    private boolean isDefault=false;

    @Generated(hash = 776738853)
    public Currency(Long id, @NotNull String name, @NotNull String currencyCode,
            @NotNull String rate, @NotNull String customFormatting,
            int displayOrder, boolean isDefault) {
        this.id = id;
        this.name = name;
        this.currencyCode = currencyCode;
        this.rate = rate;
        this.customFormatting = customFormatting;
        this.displayOrder = displayOrder;
        this.isDefault = isDefault;
    }
    @Generated(hash = 1387171739)
    public Currency() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getCurrencyCode() {
        return this.currencyCode;
    }
    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }
    public String getRate() {
        return this.rate;
    }
    public void setRate(String rate) {
        this.rate = rate;
    }
    public String getCustomFormatting() {
        return this.customFormatting;
    }
    public void setCustomFormatting(String customFormatting) {
        this.customFormatting = customFormatting;
    }
    public int getDisplayOrder() {
        return this.displayOrder;
    }
    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }
    public boolean getIsDefault() {
        return this.isDefault;
    }
    public void setIsDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }


}
