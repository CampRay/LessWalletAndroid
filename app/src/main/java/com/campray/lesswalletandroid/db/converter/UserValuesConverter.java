package com.campray.lesswalletandroid.db.converter;

import android.text.TextUtils;

import com.campray.lesswalletandroid.db.entity.UserValue;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.List;

/**
 * 转换器类：把用户选择或输入的产品属性对象转换成Json字符串形式，以便保存到数据库中
 * Created by Phills on 11/29/2017.
 */

public class UserValuesConverter implements PropertyConverter<List<UserValue>,String> {

    @Override
    public List<UserValue> convertToEntityProperty(String databaseValue) {
        if(TextUtils.isEmpty(databaseValue)) {
            return null;
        }
        else{
            Gson gson=new Gson();
            List<UserValue> userValueList = gson.fromJson(databaseValue, new TypeToken<List<UserValue>>() {}.getType());
            return userValueList;
        }
    }

    @Override
    public String convertToDatabaseValue(List<UserValue> entityProperty) {
        if(entityProperty==null||entityProperty.size()==0) {
            return "";
        }
        else{
            Gson gson=new Gson();
            return gson.toJson(entityProperty);
        }
    }
}
