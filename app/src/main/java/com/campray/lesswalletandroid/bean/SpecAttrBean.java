package com.campray.lesswalletandroid.bean;

import com.campray.lesswalletandroid.db.entity.SpecAttr;

/**
 * Created by Phills on 10/25/2017.
 */
public class SpecAttrBean {
    private long SpecificationAttributeId;
    private String SpecificationAttributeName;
    private String ValueRaw;
    private String ColorSquaresRgb;
    private String fileUrl;//保存在本地的图片路径

    public SpecAttrBean fromSpecAttr(SpecAttr specAttr){
        if(specAttr!=null) {
            SpecificationAttributeId = specAttr.getSpecificationAttributeId();
            SpecificationAttributeName = specAttr.getSpecificationAttributeName();
            ValueRaw = specAttr.getValueRaw();
            ColorSquaresRgb = specAttr.getColorSquaresRgb();
            fileUrl = specAttr.getFileUrl();
        }
        return this;
    }

    public SpecAttr toSpecAttr(){
        SpecAttr specAttr=new SpecAttr();
        specAttr.setSpecificationAttributeId(SpecificationAttributeId);
        specAttr.setSpecificationAttributeName(SpecificationAttributeName);
        specAttr.setValueRaw(ValueRaw);
        specAttr.setColorSquaresRgb(ColorSquaresRgb);
        specAttr.setFileUrl(fileUrl);
        return specAttr;
    }

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
