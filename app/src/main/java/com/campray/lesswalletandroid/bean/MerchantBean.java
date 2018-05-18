package com.campray.lesswalletandroid.bean;

import com.campray.lesswalletandroid.db.entity.Country;
import com.campray.lesswalletandroid.db.entity.Merchant;

/**
 * Created by Phills on 10/25/2017.
 */
public class MerchantBean {
    private long id;
    private String name;
    private String desc;
    private String pictureUrl;
    private String picturePath;

    public MerchantBean fromMerchant(Merchant merchant){
        if(merchant!=null) {
            id = merchant.getId();
            name = merchant.getName();
            desc = merchant.getDesc();
            pictureUrl = merchant.getPictureUrl();
            picturePath = merchant.getPicturePath();
        }
        return this;
    }

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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

}
