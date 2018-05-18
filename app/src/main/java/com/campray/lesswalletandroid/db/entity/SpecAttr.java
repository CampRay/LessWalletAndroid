package com.campray.lesswalletandroid.db.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;

/**
 * 卡卷商品的规格属性
 * Created by Phills on 10/25/2017.
 */
public class SpecAttr {
    private long SpecificationAttributeId;
    private String SpecificationAttributeName;
    private String ValueRaw;
    private String ColorSquaresRgb;
    private String fileUrl;//保存在本地的图片路径

    public long getSpecificationAttributeId() {
        return SpecificationAttributeId;
    }

    public void setSpecificationAttributeId(long specificationAttributeId) {
        SpecificationAttributeId = specificationAttributeId;
    }

    public String getSpecificationAttributeName() {
        return SpecificationAttributeName;
    }

    public void setSpecificationAttributeName(String specificationAttributeName) {
        SpecificationAttributeName = specificationAttributeName;
    }

    public String getValueRaw() {
        return ValueRaw;
    }

    public void setValueRaw(String valueRaw) {
        ValueRaw = valueRaw;
    }

    public String getColorSquaresRgb() {
        return ColorSquaresRgb;
    }

    public void setColorSquaresRgb(String colorSquaresRgb) {
        ColorSquaresRgb = colorSquaresRgb;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }
}
