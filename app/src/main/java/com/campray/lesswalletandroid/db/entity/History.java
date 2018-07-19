package com.campray.lesswalletandroid.db.entity;

import android.text.TextUtils;

import com.campray.lesswalletandroid.util.TimeUtil;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;

import java.util.Date;
import java.util.TimeZone;

/**
 * 历史记录
 * Created by Phills on 10/25/2017.
 */
@Entity
public class History {
    @Id(autoincrement = false)
    private Long id;
    @NotNull
    private int type;
    @NotNull
    private int customerId;
    @NotNull
    private String comment;
    @NotNull
    private String createdTime;
    @NotNull
    private boolean readed;

    public String getCreatedTimeLocal() {
        if (!TextUtils.isEmpty(createdTime)) {
            Date date = TimeUtil.stringToDate(createdTime, TimeUtil.FORMAT_DATE_TIME_SECOND, TimeZone.getTimeZone("UTC"));
            if(TimeUtil.isToday(createdTime,TimeUtil.FORMAT_DATE_TIME_SECOND, TimeZone.getTimeZone("UTC"))){
                return TimeUtil.dateToString(date, TimeUtil.FORMAT_TIME);
            }
            else {
                return TimeUtil.dateToString(date, TimeUtil.FORMAT_DATE);
            }
        }
        return "";
    }


    @Generated(hash = 1868980738)
    public History(Long id, int type, int customerId, @NotNull String comment,
            @NotNull String createdTime, boolean readed) {
        this.id = id;
        this.type = type;
        this.customerId = customerId;
        this.comment = comment;
        this.createdTime = createdTime;
        this.readed = readed;
    }
    @Generated(hash = 869423138)
    public History() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public int getType() {
        return this.type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public int getCustomerId() {
        return this.customerId;
    }
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
    public String getComment() {
        return this.comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    public String getCreatedTime() {
        return this.createdTime;
    }
    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }
    public boolean getReaded() {
        return this.readed;
    }
    public void setReaded(boolean readed) {
        this.readed = readed;
    }


}
