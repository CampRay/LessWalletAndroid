package com.campray.lesswalletandroid.db.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 商家信息表实体类
 * Created by Phills on 10/25/2017.
 */
@Entity
public class Merchant {
    @Id(autoincrement = false)
    private Long id;
    @NotNull
    private String name;
    @NotNull
    private String desc;
    //商家Logo
    private String pictureUrl;
    //商家Logo在本地存储路径
    private String picturePath;
    @Generated(hash = 950038278)
    public Merchant(Long id, @NotNull String name, @NotNull String desc,
            String pictureUrl, String picturePath) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.pictureUrl = pictureUrl;
        this.picturePath = picturePath;
    }
    @Generated(hash = 91174753)
    public Merchant() {
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
    public String getDesc() {
        return this.desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
    public String getPictureUrl() {
        return this.pictureUrl;
    }
    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }
    public String getPicturePath() {
        return this.picturePath;
    }
    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }


}
