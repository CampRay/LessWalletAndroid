package com.campray.lesswalletandroid.db.entity;

import java.util.List;

/**
 * 电子票的用户属性
 * Created by Phills on 10/25/2017.
 */
public class UserAttr {
    private long Id; //唯一ID
    private long ProductId; //对应的商品ID
    private long ProductAttributeId; //对应的用户属性表ID
    private String Name;  //用户属性名称
    private String TextPrompt;  //自定义的显示文本
    private boolean IsRequired; //是否必填项
    //DropdownList = 1, RadioList = 2, Checkboxes = 3,TextBox = 4, MultilineTextbox = 10,Datepicker = 20,FileUpload = 30,ColorSquares = 40,ImageSquares = 45,ReadonlyCheckboxes = 50,
    private int AttributeControlType; //页面显示的表单控件类型
    private String DefaultValue;   //默认显示值
    private boolean HasCondition; //是否有设定表单控件的页面显示条件
    private List<UserAttrValue> Values; //用户属性的预设值集合

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public long getProductId() {
        return ProductId;
    }

    public void setProductId(long productId) {
        ProductId = productId;
    }

    public long getProductAttributeId() {
        return ProductAttributeId;
    }

    public void setProductAttributeId(long productAttributeId) {
        ProductAttributeId = productAttributeId;
    }

    public String getTextPrompt() {
        return TextPrompt;
    }

    public void setTextPrompt(String textPrompt) {
        TextPrompt = textPrompt;
    }

    public boolean isRequired() {
        return IsRequired;
    }

    public void setRequired(boolean required) {
        IsRequired = required;
    }

    public int getAttributeControlType() {
        return AttributeControlType;
    }

    public void setAttributeControlType(int attributeControlType) {
        AttributeControlType = attributeControlType;
    }

    public String getDefaultValue() {
        return DefaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        DefaultValue = defaultValue;
    }

    public boolean isHasCondition() {
        return HasCondition;
    }

    public void setHasCondition(boolean hasCondition) {
        HasCondition = hasCondition;
    }

    public List<UserAttrValue> getValues() {
        return Values;
    }

    public void setValues(List<UserAttrValue> values) {
        Values = values;
    }
}
