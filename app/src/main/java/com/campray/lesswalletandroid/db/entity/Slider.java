package com.campray.lesswalletandroid.db.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;

/**
 * 轮播图片
 * Created by Phills on 5/25/2018.
 */
@Entity
public class Slider {
    @Id(autoincrement =false )
    private Long id;
    private String picUrl;
    private String text;
    private String link;
    @Generated(hash = 1497131014)
    public Slider(Long id, String picUrl, String text, String link) {
        this.id = id;
        this.picUrl = picUrl;
        this.text = text;
        this.link = link;
    }
    @Generated(hash = 932120237)
    public Slider() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getPicUrl() {
        return this.picUrl;
    }
    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }
    public String getText() {
        return this.text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public String getLink() {
        return this.link;
    }
    public void setLink(String link) {
        this.link = link;
    }
}
