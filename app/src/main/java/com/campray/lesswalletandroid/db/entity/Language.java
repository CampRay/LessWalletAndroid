package com.campray.lesswalletandroid.db.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Phills on 10/25/2017.
 */
@Entity
public class Language {
    @Id(autoincrement = false)
    private Long id;
    @NotNull
    private String name;
    @NotNull
    private String languageCulture;
    @NotNull
    private String uniqueSeoCode;
    @NotNull
    private int displayOrder;
    @Generated(hash = 804495434)
    public Language(Long id, @NotNull String name, @NotNull String languageCulture,
            @NotNull String uniqueSeoCode, int displayOrder) {
        this.id = id;
        this.name = name;
        this.languageCulture = languageCulture;
        this.uniqueSeoCode = uniqueSeoCode;
        this.displayOrder = displayOrder;
    }
    @Generated(hash = 1478671802)
    public Language() {
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
    public String getLanguageCulture() {
        return this.languageCulture;
    }
    public void setLanguageCulture(String languageCulture) {
        this.languageCulture = languageCulture;
    }
    public String getUniqueSeoCode() {
        return this.uniqueSeoCode;
    }
    public void setUniqueSeoCode(String uniqueSeoCode) {
        this.uniqueSeoCode = uniqueSeoCode;
    }
    public int getDisplayOrder() {
        return this.displayOrder;
    }
    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }


}
