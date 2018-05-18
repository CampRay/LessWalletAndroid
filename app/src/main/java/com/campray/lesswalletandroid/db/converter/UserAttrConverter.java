package com.campray.lesswalletandroid.db.converter;

import android.text.TextUtils;

import com.campray.lesswalletandroid.db.entity.SpecAttr;
import com.campray.lesswalletandroid.db.entity.UserAttr;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.List;

/**
 * 转换器类：把规格属性对象转换成Json字符串形式，以便保存到数据库中
 * Created by Phills on 11/29/2017.
 */

public class UserAttrConverter implements PropertyConverter<List<UserAttr>,String> {

    @Override
    public List<UserAttr> convertToEntityProperty(String databaseValue) {
        if(TextUtils.isEmpty(databaseValue)) {
            return null;
        }
        else{
            Gson gson=new Gson();
            List<UserAttr> userAttrList = gson.fromJson(databaseValue, new TypeToken<List<UserAttr>>() {}.getType());
            return userAttrList;
        }
    }

    @Override
    public String convertToDatabaseValue(List<UserAttr> entityProperty) {
        if(entityProperty==null||entityProperty.size()==0) {
            return "";
        }
        else{
            Gson gson=new Gson();
            return gson.toJson(entityProperty);
        }
    }
}
