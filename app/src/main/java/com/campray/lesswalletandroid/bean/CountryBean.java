package com.campray.lesswalletandroid.bean;

import com.campray.lesswalletandroid.db.entity.Country;

/**
 * Created by Phills on 10/25/2017.
 */
public class CountryBean {
    private long id;
    private String name;
    private String twoLetterIsoCode;
    private String threeLetterIsoCode;
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

    public String getTwoLetterIsoCode() {
        return twoLetterIsoCode;
    }

    public void setTwoLetterIsoCode(String twoLetterIsoCode) {
        this.twoLetterIsoCode = twoLetterIsoCode;
    }

    public String getThreeLetterIsoCode() {
        return threeLetterIsoCode;
    }

    public void setThreeLetterIsoCode(String threeLetterIsoCode) {
        this.threeLetterIsoCode = threeLetterIsoCode;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    public CountryBean fromCountry(Country country){
        if(country!=null) {
            id = country.getId();
            name = country.getName();
            twoLetterIsoCode = country.getTwoLetterIsoCode();
            threeLetterIsoCode = country.getThreeLetterIsoCode();
            displayOrder = country.getDisplayOrder();
        }
        return this;
    }
}
