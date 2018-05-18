package com.campray.lesswalletandroid.db.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 各表字段对应的多语言内容表
 * Created by Phills on 10/25/2017.
 */
@Entity
public class LocalizedProperty {
    @Id(autoincrement = true)
    private Long id;
    @NotNull
    private Long entityId;
    @NotNull
    private Long languageId;
    @NotNull
    private String localeKeyGroup;
    @NotNull
    private String localeKey;
    @NotNull
    private String localeValue;
    @Generated(hash = 1779740073)
    public LocalizedProperty(Long id, @NotNull Long entityId,
            @NotNull Long languageId, @NotNull String localeKeyGroup,
            @NotNull String localeKey, @NotNull String localeValue) {
        this.id = id;
        this.entityId = entityId;
        this.languageId = languageId;
        this.localeKeyGroup = localeKeyGroup;
        this.localeKey = localeKey;
        this.localeValue = localeValue;
    }
    @Generated(hash = 790579200)
    public LocalizedProperty() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getEntityId() {
        return this.entityId;
    }
    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }
    public Long getLanguageId() {
        return this.languageId;
    }
    public void setLanguageId(Long languageId) {
        this.languageId = languageId;
    }
    public String getLocaleKeyGroup() {
        return this.localeKeyGroup;
    }
    public void setLocaleKeyGroup(String localeKeyGroup) {
        this.localeKeyGroup = localeKeyGroup;
    }
    public String getLocaleKey() {
        return this.localeKey;
    }
    public void setLocaleKey(String localeKey) {
        this.localeKey = localeKey;
    }
    public String getLocaleValue() {
        return this.localeValue;
    }
    public void setLocaleValue(String localeValue) {
        this.localeValue = localeValue;
    }


}
