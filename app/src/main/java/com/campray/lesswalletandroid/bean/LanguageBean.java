package com.campray.lesswalletandroid.bean;

import com.campray.lesswalletandroid.db.entity.Language;

/**
 * Created by Phills on 10/25/2017.
 */
public class LanguageBean {
    private long id;
    private String name;
    private String languageCulture;
    private String uniqueSeoCode;
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

    public String getLanguageCulture() {
        return languageCulture;
    }

    public void setLanguageCulture(String languageCulture) {
        this.languageCulture = languageCulture;
    }

    public String getUniqueSeoCode() {
        return uniqueSeoCode;
    }

    public void setUniqueSeoCode(String uniqueSeoCode) {
        this.uniqueSeoCode = uniqueSeoCode;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    public LanguageBean fromLanguage(Language language){
        if(language!=null) {
            id = language.getId();
            name = language.getName();
            languageCulture = language.getLanguageCulture();
            uniqueSeoCode = language.getUniqueSeoCode();
            displayOrder = language.getDisplayOrder();
        }
        return this;
    }
}
