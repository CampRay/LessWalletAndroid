package com.campray.lesswalletandroid.db.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;

/**
 * 商品属性表
 * Created by Phills on 10/25/2017.
 */
@Entity
public class ProductAttribute {
    @Id(autoincrement =false )
    private Long id;
    @NotNull
    private String name;
    @Generated(hash = 1440644790)
    public ProductAttribute(Long id, @NotNull String name) {
        this.id = id;
        this.name = name;
    }
    @Generated(hash = 295476675)
    public ProductAttribute() {
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



}
