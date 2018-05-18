package com.campray.lesswalletandroid.db.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Phills on 10/25/2017.
 */
@Entity
public class Country {
    @Id(autoincrement =false )
    private Long id;
    @NotNull
    private String name;
    @NotNull
    private String twoLetterIsoCode;
    @NotNull
    private String threeLetterIsoCode;
    @NotNull
    private int displayOrder;
    @Generated(hash = 1597076636)
    public Country(Long id, @NotNull String name, @NotNull String twoLetterIsoCode,
            @NotNull String threeLetterIsoCode, int displayOrder) {
        this.id = id;
        this.name = name;
        this.twoLetterIsoCode = twoLetterIsoCode;
        this.threeLetterIsoCode = threeLetterIsoCode;
        this.displayOrder = displayOrder;
    }
    @Generated(hash = 668024697)
    public Country() {
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
    public String getTwoLetterIsoCode() {
        return this.twoLetterIsoCode;
    }
    public void setTwoLetterIsoCode(String twoLetterIsoCode) {
        this.twoLetterIsoCode = twoLetterIsoCode;
    }
    public String getThreeLetterIsoCode() {
        return this.threeLetterIsoCode;
    }
    public void setThreeLetterIsoCode(String threeLetterIsoCode) {
        this.threeLetterIsoCode = threeLetterIsoCode;
    }
    public int getDisplayOrder() {
        return this.displayOrder;
    }
    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }


}
