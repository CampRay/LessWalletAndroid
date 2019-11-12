package com.campray.lesswalletandroid.db.entity;

/**
 * 用户购买电子票时选择或输入的属性值
 * Created by Phills on 7/25/2018.
 */
public class UserValue {
    private long ProductAttributeId;//对应的产品属性ID
    private String Value; //是否预选项

    public long getProductAttributeId() {
        return ProductAttributeId;
    }

    public void setProductAttributeId(long productAttributeId) {
        ProductAttributeId = productAttributeId;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }
}
